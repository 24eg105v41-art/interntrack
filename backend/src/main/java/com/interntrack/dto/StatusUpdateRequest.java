package com.interntrack.dto;

import jakarta.validation.constraints.NotBlank;

/** e.g. {"status":"SHORTLISTED"} */
public class StatusUpdateRequest {

    @NotBlank(message = "status is required")
    private String status;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
