package edu.cmu.andrew.dhairyya.exceptions;

public class AppUnauthorizedException extends AppException {
    public AppUnauthorizedException(int errorCode, String errorMessage) {
        super(UNAUTHORIZED_EXCEPTION, errorCode, errorMessage);
    }

    public AppUnauthorizedException(int errorCode) {
        super(UNAUTHORIZED_EXCEPTION, errorCode);
    }
}
