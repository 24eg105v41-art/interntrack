package com.interntrack.model;

public enum WorkMode {
    REMOTE("Remote"),
    HYBRID("Hybrid"),
    ONSITE("On-site");

    private final String label;

    WorkMode(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

    public static WorkMode parse(String value) {
        if (value == null || value.isBlank()) {
            return ONSITE;
        }
        try {
            return WorkMode.valueOf(value.trim().toUpperCase());
        } catch (IllegalArgumentException ex) {
            throw new IllegalArgumentException("Unknown work mode: " + value);
        }
    }
}
