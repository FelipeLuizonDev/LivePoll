package com.livepoll.backend.exception;

public class EnqueteNotFoundException extends RuntimeException {

    public EnqueteNotFoundException(Long id) {
        super("Enquete não encontrada: " + id);
    }
}
