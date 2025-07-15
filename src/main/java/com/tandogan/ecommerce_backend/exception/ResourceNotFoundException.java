package com.tandogan.ecommerce_backend.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

// Bu annotation, bu exception fırlatıldığında Spring'in otomatik olarak
// HTTP 404 Not Found durum kodunu döndürmesini sağlar.
@ResponseStatus(HttpStatus.NOT_FOUND)
public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String message) {
        super(message);
    }
}