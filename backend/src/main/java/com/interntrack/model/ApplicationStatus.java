package com.interntrack.model;

/**
 * Application tracking statuses used by the abstract:
 * Applied, Shortlisted, Selected, Rejected.
 */
public enum ApplicationStatus {
    APPLIED("Applied", 1),
    SHORTLISTED("Shortlisted", 2),
    SELECTED("Selected", 3),
    REJECTED("Rejected", 0);

    private final String label;
    private final int stage;

    ApplicationStatus(String label, int stage) {
        this.label = label;
        this.stage = stage;
    }

    public String getLabel() {
        return label;
    }

    /** Ordered pipeline stage (0 = inactive / closed out). */
    public int getStage() {
        return stage;
    }

    public boolean isTerminal() {
        return this == REJECTED || this == SELECTED;
    }

    public static ApplicationStatus parse(String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("Status is required");
        }
        try {
            return ApplicationStatus.valueOf(value.trim().toUpperCase());
        } catch (IllegalArgumentException ex) {
            throw new IllegalArgumentException("Unknown status: " + value
                    + " (use APPLIED, SHORTLISTED, SELECTED or REJECTED)");
        }
    }
}
