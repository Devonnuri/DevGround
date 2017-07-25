package com.devground.exception;

import org.w3c.dom.DOMException;

public class GameException extends DOMException {
    public GameException(String msg, Object... args) {
        super(INVALID_STATE_ERR, String.format(msg, args));
    }
}
