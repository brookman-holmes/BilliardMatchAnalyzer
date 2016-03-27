package com.brookmanholmes.billiards.turn;

/**
 * Created by Brookman Holmes on 11/5/2015.
 */
public class InvalidBallException extends RuntimeException {
    public InvalidBallException() {
    }

    public InvalidBallException(String message) {
        super(message);
    }

    public InvalidBallException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidBallException(Throwable cause) {
        super(cause);
    }

    public InvalidBallException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
