package com.spectralogic.ds3client.utils;

public final class Platform {
    private Platform() {}

    public static boolean isWindows() {
        return System.getProperty("os.name").contains("Windows");
    }
}
