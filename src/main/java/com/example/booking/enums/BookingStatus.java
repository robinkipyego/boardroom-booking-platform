package com.example.booking.enums;

public enum BookingStatus {
    PENDING("Pending Approval"),
    CONFIRMED("Confirmed"),
    CANCELLED("Cancelled"),
    COMPLETED("Completed"),
    IN_PROGRESS("In Progress");

    private final String displayName;

    BookingStatus(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public boolean isActive() {
        return this == CONFIRMED || this == IN_PROGRESS;
    }

    public boolean canBeCancelled() {
        return this == PENDING || this == CONFIRMED;
    }

    public boolean canBeModified() {
        return this == PENDING;
    }
}
