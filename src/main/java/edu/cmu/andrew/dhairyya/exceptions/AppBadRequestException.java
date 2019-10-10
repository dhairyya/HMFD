package edu.cmu.andrew.dhairyya.exceptions;

public class AppBadRequestException extends AppException {
    public AppBadRequestException(int errorCode, String errorMessage) {
        super(BAD_REQUEST_EXCEPTION, errorCode, errorMessage);
    }

    public AppBadRequestException(int errorCode) {
        super(BAD_REQUEST_EXCEPTION, errorCode);
    }
}
