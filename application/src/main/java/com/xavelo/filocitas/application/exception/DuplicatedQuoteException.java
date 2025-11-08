package com.xavelo.filocitas.application.exception;

/**
 * Exception thrown when attempting to persist a quote that already exists in the system.
 * <p>
 * It captures contextual information such as the original payload and the quote text that triggered
 * the duplication so it can be echoed back to the caller, providing additional context about the
 * duplicated resource.
 */
public class DuplicatedQuoteException extends RuntimeException {

    private final String payload;
    private final String quoteText;

    public DuplicatedQuoteException(Throwable cause) {
        this(null, null, cause);
    }

    public DuplicatedQuoteException(String quoteText, Throwable cause) {
        this(null, quoteText, cause);
    }

    private DuplicatedQuoteException(String payload, String quoteText, Throwable cause) {
        super("Quote already exists", cause);
        this.payload = payload;
        this.quoteText = quoteText;
    }

    private DuplicatedQuoteException(String payload, String quoteText, DuplicatedQuoteException previous) {
        super(previous.getMessage(), previous);
        this.payload = payload;
        this.quoteText = quoteText;
    }

    public String getPayload() {
        return payload;
    }

    public String getQuoteText() {
        return quoteText;
    }

    public DuplicatedQuoteException withPayload(String payload) {
        if (payload == null || payload.equals(this.payload)) {
            return this;
        }
        return new DuplicatedQuoteException(payload, this.quoteText, this);
    }

    public DuplicatedQuoteException withQuoteText(String quoteText) {
        if (quoteText == null || quoteText.equals(this.quoteText)) {
            return this;
        }
        return new DuplicatedQuoteException(this.payload, quoteText, this);
    }
}
