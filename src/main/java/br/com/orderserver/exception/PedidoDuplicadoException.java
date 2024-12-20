package br.com.orderserver.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class PedidoDuplicadoException extends RuntimeException {
    public PedidoDuplicadoException(String message) {
        super(message);
    }
}