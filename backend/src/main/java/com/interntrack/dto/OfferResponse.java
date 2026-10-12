package com.interntrack.dto;

import com.interntrack.model.Offer;
import com.interntrack.model.OfferStatus;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class OfferResponse {

    private Long offerId;
    private Long userId;
    private String companyName;
    private String roleTitle;
    private Integer stipend;
    private LocalDate offerDate;
    private LocalDate joiningDate;
    private LocalDate responseDeadline;
    private long daysToRespond;
    private boolean expired;
    private String status;
    private String statusLabel;
    private String letterLink;
    private String notes;
    private Long internshipId;
    private LocalDateTime createdAt;

    // admin-only view helper
    private String studentName;
    private String studentEmail;

    public static OfferResponse from(Offer o) {
        OfferResponse dto = new OfferResponse();
        dto.offerId = o.getOfferId();
        dto.userId = o.getUser() == null ? null : o.getUser().getUserId();
        dto.companyName = o.getCompanyName();
        dto.roleTitle = o.getRoleTitle();
        dto.stipend = o.getStipend();
        dto.offerDate = o.getOfferDate();
        dto.joiningDate = o.getJoiningDate();
        dto.responseDeadline = o.getResponseDeadline();
        dto.daysToRespond = o.getDaysToRespond();
        dto.expired = o.isExpired();
        dto.status = o.getStatus() == null ? OfferStatus.PENDING.name() : o.getStatus().name();
        dto.statusLabel = o.getStatus() == null ? OfferStatus.PENDING.getLabel() : o.getStatus().getLabel();
        dto.letterLink = o.getLetterLink();
        dto.notes = o.getNotes();
        dto.internshipId = o.getInternship() == null ? null : o.getInternship().getInternshipId();
        dto.createdAt = o.getCreatedAt();
        if (o.getUser() != null) {
            dto.studentName = o.getUser().getName();
            dto.studentEmail = o.getUser().getEmail();
        }
        return dto;
    }

    public Long getOfferId() {
        return offerId;
    }

    public Long getUserId() {
        return userId;
    }

    public String getCompanyName() {
        return companyName;
    }

    public String getRoleTitle() {
        return roleTitle;
    }

    public Integer getStipend() {
        return stipend;
    }

    public LocalDate getOfferDate() {
        return offerDate;
    }

    public LocalDate getJoiningDate() {
        return joiningDate;
    }

    public LocalDate getResponseDeadline() {
        return responseDeadline;
    }

    public long getDaysToRespond() {
        return daysToRespond;
    }

    public boolean isExpired() {
        return expired;
    }

    public String getStatus() {
        return status;
    }

    public String getStatusLabel() {
        return statusLabel;
    }

    public String getLetterLink() {
        return letterLink;
    }

    public String getNotes() {
        return notes;
    }

    public Long getInternshipId() {
        return internshipId;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public String getStudentName() {
        return studentName;
    }

    public String getStudentEmail() {
        return studentEmail;
    }
}
