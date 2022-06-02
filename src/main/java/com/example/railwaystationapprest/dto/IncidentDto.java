package com.example.railwaystationapprest.dto;

import com.example.railwaystationapprest.model.IncidentStatus;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class IncidentDto {
    @NotBlank()
    @Size(max = 1000)
    private String description;
    private IncidentStatus status;
    private long userId;
    private long journeyId;

    public IncidentDto() {
    }

    public IncidentDto(String description, IncidentStatus status, long userId, long journeyId) {
        this.description = description;
        this.status = status;
        this.userId = userId;
        this.journeyId = journeyId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public IncidentStatus getStatus() {
        return status;
    }

    public void setStatus(IncidentStatus status) {
        this.status = status;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public long getJourneyId() {
        return journeyId;
    }

    public void setJourneyId(long journeyId) {
        this.journeyId = journeyId;
    }
}
