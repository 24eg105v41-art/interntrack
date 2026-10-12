package com.interntrack.dto;

import java.time.LocalDate;

/**
 * One item on the deadline board: an internship application deadline or an offer
 * response window, with an urgency level (OVERDUE / TODAY / SOON / UPCOMING).
 */
public class DeadlineResponse {

    private Long referenceId;
    private String type;
    private String title;
    private String subtitle;
    private LocalDate dueDate;
    private long daysLeft;
    private String urgency;

    public static DeadlineResponse of(Long referenceId, String type, String title, String subtitle,
                                      LocalDate dueDate, long daysLeft, String urgency) {
        DeadlineResponse dto = new DeadlineResponse();
        dto.referenceId = referenceId;
        dto.type = type;
        dto.title = title;
        dto.subtitle = subtitle;
        dto.dueDate = dueDate;
        dto.daysLeft = daysLeft;
        dto.urgency = urgency;
        return dto;
    }

    public Long getReferenceId() {
        return referenceId;
    }

    public String getType() {
        return type;
    }

    public String getTitle() {
        return title;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public long getDaysLeft() {
        return daysLeft;
    }

    public String getUrgency() {
        return urgency;
    }
}
