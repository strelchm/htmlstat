package ru.strelchm.htmlstat;

import ru.strelchm.htmlstat.api.DocumentStatProcessingException;

public enum AppMode {
    PARSING("--p"),
    HISTORY("--h"),
    CLEAR("--c");

    private final String key;

    AppMode(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }

    public static AppMode getByKey(String key) throws DocumentStatProcessingException {
        for (AppMode mode : values()) {
            if (mode.getKey().equals(key)) {
                return mode;
            }
        }

        throw new DocumentStatProcessingException("Unknown application mode: '" + key + "'");
    }
}