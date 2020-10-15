package com.dsm.pick.utils.exception;

public class SchoolClassNotFoundException extends RuntimeException {
    public SchoolClassNotFoundException() {
        super("지정된 교실을 찾을 수 없음");
    }

    public SchoolClassNotFoundException(String message) {
        super(message);
    }
}