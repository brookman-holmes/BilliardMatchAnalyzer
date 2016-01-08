package com.brookmanholmes.billiards.game;

/**
 * Created by Brookman Holmes on 10/30/2015.
 */
public class InvalidGameTypeException extends IllegalArgumentException {
    public InvalidGameTypeException() {
    }

    public InvalidGameTypeException(String s) {
        super(s);
    }

    public InvalidGameTypeException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidGameTypeException(Throwable cause) {
        super(cause);
    }
}
