package com.devground.utils;

import com.devground.exception.GameException;

public class NullCheck {
    public static boolean isNull(Object obj) {
        return obj == null;
    }

    public static void check(Object obj) {
        if(isNull(obj))
            throw new GameException("예상치 못한 값이 NULL이 되었습니다.");
    }

    public static Object setDefault(Object target, Object defaultValue) {
        return isNull(target) ? defaultValue : target;
    }

    public static void check(Object obj, String msg) {
        if(isNull(obj))
            throw new GameException(msg);
    }
}
