package com.axreng.backend.crawler.service.auxiliar;

public enum MagicNumbers {
    DEFAULT_LIMIT(-1);
    private final int value;
    MagicNumbers(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
