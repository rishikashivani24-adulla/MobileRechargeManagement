package com.recharge.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public class RechargePlan {

    @Positive(message = "Plan ID must be positive")
    private int planId;

    @NotBlank(message = "Plan name cannot be empty")
    @Size(min = 2, max = 50, message = "Plan name must be between 2 and 50 characters")
    private String planName;

    @Positive(message = "Price must be positive")
    private double price;

    @Positive(message = "Validity must be positive")
    private int validityDays;

    @NotBlank(message = "Data allowance cannot be empty")
    private String dataAllowance;

    @NotNull(message = "Active status is required")
    private Boolean active;

    public RechargePlan() {
    }

    public RechargePlan(int planId, String planName, double price,
                        int validityDays, String dataAllowance, Boolean active) {
        this.planId = planId;
        this.planName = planName;
        this.price = price;
        this.validityDays = validityDays;
        this.dataAllowance = dataAllowance;
        this.active = active;
    }

    public int getPlanId() {
        return planId;
    }

    public void setPlanId(int planId) {
        this.planId = planId;
    }

    public String getPlanName() {
        return planName;
    }

    public void setPlanName(String planName) {
        this.planName = planName;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getValidityDays() {
        return validityDays;
    }

    public void setValidityDays(int validityDays) {
        this.validityDays = validityDays;
    }

    public String getDataAllowance() {
        return dataAllowance;
    }

    public void setDataAllowance(String dataAllowance) {
        this.dataAllowance = dataAllowance;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    @Override
    public String toString() {
        return "RechargePlan{" +
                "planId=" + planId +
                ", planName='" + planName + '\'' +
                ", price=" + price +
                ", validityDays=" + validityDays +
                ", dataAllowance='" + dataAllowance + '\'' +
                ", active=" + active +
                '}';
    }
}
