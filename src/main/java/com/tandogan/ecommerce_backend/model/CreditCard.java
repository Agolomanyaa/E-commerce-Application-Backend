package com.tandogan.ecommerce_backend.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "credit_cards")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = "user")
@EqualsAndHashCode(exclude = "user")
public class CreditCard {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnore
    private User user;

    @Column(name = "card_no", nullable = false)
    private String cardNo;

    @Column(name = "expire_month", nullable = false)
    private Integer expireMonth;

    @Column(name = "expire_year", nullable = false)
    private Integer expireYear;

    @Column(name = "name_on_card", nullable = false)
    private String nameOnCard;
}