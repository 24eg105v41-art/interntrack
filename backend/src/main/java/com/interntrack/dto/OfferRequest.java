package com.interntrack.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public class OfferRequest {

    /** Optional link to one of this student's internships. */
    private Long internshipId;

    @NotBlank(message = "Company name is required")
    private String companyName;

    @NotBlank(message = "Role is required")
    private String roleTitle;

    @NotNull(message = "Stipend is required")
    @Min(value = 0, message = "Stipend cannot be negative")
    private Integer stipend;

    private LocalDate offerDate;

    private LocalDate joiningDate;

    /** Leave empty to use the default response window (7 days). */
    private LocalDate responseDeadline;

    @Size(max = 300, message = "Offer letter link can be at most 300 characters")
    private String letterLink;

    @Size(max = 1000, message = "Notes can be at most 1000 characters")
    private String notes;

    public Long getInternshipId() {
        return internshipId;
    }

    public void setInternshipId(Long internshipId) {
        this.internshipId = internshipId;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getRoleTitle() {
        return roleTitle;
    }

    public void setRoleTitle(String roleTitle) {
        this.roleTitle = roleTitle;
    }

    public Integer getStipend() {
        return stipend;
    }

    public void setStipend(Integer stipend) {
        this.stipend = stipend;
    }

    public LocalDate getOfferDate() {
        return offerDate;
    }

    public void setOfferDate(LocalDate offerDate) {
        this.offerDate = offerDate;
    }

    public LocalDate getJoiningDate() {
        return joiningDate;
    }

    public void setJoiningDate(LocalDate joiningDate) {
        this.joiningDate = joiningDate;
    }

    public LocalDate getResponseDeadline() {
        return responseDeadline;
    }

    public void setResponseDeadline(LocalDate responseDeadline) {
        this.responseDeadline = responseDeadline;
    }

    public String getLetterLink() {
        return letterLink;
    }

    public void setLetterLink(String letterLink) {
        this.letterLink = letterLink;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}
