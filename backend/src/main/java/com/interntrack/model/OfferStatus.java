package com.interntrack.model;

public enum OfferStatus {
    PENDING("Pending"),
    ACCEPTED("Accepted"),
    DECLINED("Declined"),
    EXPIRED("Expired");

    private final String label;

    OfferStatus(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

    public static OfferStatus parse(String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("Offer status is required");
        }
        try {
            return OfferStatus.valueOf(value.trim().toUpperCase());
        } catch (IllegalArgumentException ex) {
            throw new IllegalArgumentException("Unknown offer status: " + value);
        }
    }
}
