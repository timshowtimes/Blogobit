package com.timinsquare.blogobit2.util;

public class NotValidCredentials extends RuntimeException {
    public NotValidCredentials(String errorMessage) {
        super(errorMessage);
    }
}
