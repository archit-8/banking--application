
package com.example.demo.exception;

// Fixed spelling: InsufficientAmountException
public class InsufficientAmountException extends RuntimeException {
    public InsufficientAmountException(String message) {
        super(message);
    }
}
