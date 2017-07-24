package com.devground.exception;

import sun.plugin.dom.exception.InvalidStateException;

public class GameException extends InvalidStateException {
    public GameException(String msg, Object... args) {
        super(String.format(msg, args));
    }
}
