package ru.strelchm.htmlstat.api;

/**
 * Общий тип ошибки для конкретно данного приложения
 */
public class DocumentStatProcessingException extends Exception {
    public DocumentStatProcessingException(String message) {
        super(message);
    }

    public DocumentStatProcessingException(String message, Throwable cause) {
        super(message, cause);
    }
}
