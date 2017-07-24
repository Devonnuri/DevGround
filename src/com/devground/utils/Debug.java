package com.devground.utils;

public class Debug {
    public static void log(String str) {
        if (com.devground.main.Main.DEBUG) {
            System.out.println(str);
        }
    }
}
