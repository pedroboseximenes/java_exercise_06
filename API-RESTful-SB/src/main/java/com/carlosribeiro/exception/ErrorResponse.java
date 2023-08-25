package com.carlosribeiro.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.Map;

@AllArgsConstructor
@Getter
public class ErrorResponse {
    private LocalDateTime dateTime;
    private int errorCode;
    private String error;
    private String metodo;
    private String requestUri;    // URL: http://localhost:8080/produtos/1
                                  // URI: /produtos/1
    private Map<String, String> map;
    private String message;
}
