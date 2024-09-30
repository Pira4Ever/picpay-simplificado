package edu.octavio.simplified_picpay.service.exception;

import java.io.Serial;

public class NotFoundException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 1L;

    public NotFoundException() {
        super("Resource not found");
    }
}
