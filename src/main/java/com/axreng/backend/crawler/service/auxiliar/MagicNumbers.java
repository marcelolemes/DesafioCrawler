package com.axreng.backend.crawler.service.auxiliar;

public enum MagicNumbers {
    DEFAULT_LIMIT(-1),
    ZERO(0),
    REPETICOES_THREAD_LOOP(10);
    private final int value;

    MagicNumbers(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
