package com.tandogan.ecommerce_backend.service.impl;

import com.tandogan.ecommerce_backend.dto.request.CreateOrderRequest;
import com.tandogan.ecommerce_backend.dto.request.OrderItemRequest;
import com.tandogan.ecommerce_backend.dto.response.OrderDto;
import com.tandogan.ecommerce_backend.dto.response.OrderItemDto;
import com.tandogan.ecommerce_backend.exception.*;
import com.tandogan.ecommerce_backend.model.*;
import com.tandogan.ecommerce_backend.repository.*;
import com.tandogan.ecommerce_backend.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final AddressRepository addressRepository;
    private final ProductVariantRepository productVariantRepository;
    private final ProductRepository productRepository; // Product bilgilerine erişim için eklendi

    @Override
    @Transactional
    public OrderDto createOrder(Long userId, CreateOrderRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + userId));

        // DÜZELTME: Gelen istekteki ürün listesini kontrol et
        if (request.getProducts() == null || request.getProducts().isEmpty()) {
            throw new CartIsEmptyException("Cannot create order with an empty product list.");
        }

        Address address = addressRepository.findById(request.getAddressId())
                .orElseThrow(() -> new AddressNotFoundException("Address not found with id: " + request.getAddressId()));

        if (!address.getUser().getId().equals(userId)) {
            throw new SecurityException("User does not have permission to use this address.");
        }

        Order newOrder = new Order();
        newOrder.setUser(user);
        newOrder.setShippingAddress(formatAddress(address));
        newOrder.setOrderDate(LocalDateTime.now());
        newOrder.setStatus(Order.OrderStatus.PENDING);

        BigDecimal totalPrice = BigDecimal.ZERO;

        // DÜZELTME: Veritabanı sepeti yerine, request'ten gelen ürünler üzerinde döngü kur
        for (OrderItemRequest itemRequest : request.getProducts()) {
            ProductVariant variant = productVariantRepository.findById(itemRequest.getVariantId())
                    .orElseThrow(() -> new ResourceNotFoundException("Product Variant not found with id: " + itemRequest.getVariantId()));

            if (variant.getStock() < itemRequest.getCount()) {
                String productName = variant.getProduct() != null ? variant.getProduct().getName() : "N/A";
                throw new OutOfStockException("Not enough stock for product: " + productName + ". Available: " + variant.getStock() + ", Requested: " + itemRequest.getCount());
            }

            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(newOrder);
            orderItem.setVariant(variant);
            orderItem.setQuantity(itemRequest.getCount());
            orderItem.setPrice(variant.getProduct().getPrice()); // Varyantın bağlı olduğu ürünün fiyatını al
            newOrder.getOrderItems().add(orderItem);

            // Stoktan düş
            variant.setStock(variant.getStock() - itemRequest.getCount());
            productVariantRepository.save(variant);

            // Toplam fiyatı hesapla
            totalPrice = totalPrice.add(variant.getProduct().getPrice().multiply(BigDecimal.valueOf(itemRequest.getCount())));
        }

        newOrder.setTotalPrice(totalPrice);
        Order savedOrder = orderRepository.save(newOrder);

        // cartService.clearCart(userId) çağrısı kaldırıldı.

        return convertToDto(savedOrder);
    }

    private OrderDto convertToDto(Order order) {
        List<OrderItemDto> orderItemDtos = order.getOrderItems().stream()
                .map(item -> {
                    Product product = item.getVariant().getProduct();
                    return OrderItemDto.builder()
                            .productId(product.getId())
                            .productName(product.getName())
                            .variantInfo(item.getVariant().getColor() + " / " + item.getVariant().getSize())
                            .productImageUrl(product.getImages().stream().findFirst().map(ProductImage::getUrl).orElse("no-image.jpg"))
                            .quantity(item.getQuantity())
                            .price(item.getPrice())
                            .build();
                })
                .collect(Collectors.toList());

        return OrderDto.builder()
                .id(order.getId())
                .userId(order.getUser().getId())
                .orderItems(orderItemDtos)
                .shippingAddress(order.getShippingAddress())
                .orderDate(order.getOrderDate())
                .totalPrice(order.getTotalPrice())
                .status(order.getStatus().name())
                .build();
    }

    @Override
    public List<OrderDto> getOrdersByUserId(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new UserNotFoundException("User not found with id: " + userId);
        }

        List<Order> orders = orderRepository.findByUserIdOrderByOrderDateDesc(userId);
        return orders.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    private String formatAddress(Address address) {
        return String.format("%s: %s, %s/%s",
                address.getTitle(),
                address.getNeighborhood(),
                address.getDistrict(),
                address.getCity());
    }
}