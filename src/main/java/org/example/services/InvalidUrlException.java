package org.example.services;

import java.io.IOException;

public class InvalidUrlException extends IOException {
    public InvalidUrlException(String message) {
        super(message);
    }
}
