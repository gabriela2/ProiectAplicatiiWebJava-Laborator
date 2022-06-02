package com.example.railwaystationapprest.dto;


import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

public class TicketDto {
    @NotBlank
    @Min(0)
    private double finalPrice;
    @NotBlank
    private long userId;
    @NotBlank
    private long journeyId;

    public TicketDto() {
    }

    public TicketDto(double finalPrice, long userId, long journeyId) {
        this.finalPrice = finalPrice;
        this.userId = userId;
        this.journeyId = journeyId;
    }

    public double getFinalPrice() {
        return finalPrice;
    }

    public void setFinalPrice(double finalPrice) {
        this.finalPrice = finalPrice;
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
