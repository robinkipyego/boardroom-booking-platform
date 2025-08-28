package com.example.booking.enums;

public enum RoomCapacityType {
    SMALL(1, 6, "Small Meeting Room"),
    MEDIUM(7, 15, "Medium Conference Room"),
    LARGE(16, 50, "Large Boardroom");

    private final int minCapacity;
    private final int maxCapacity;
    private final String description;

    RoomCapacityType(int minCapacity, int maxCapacity, String description) {
        this.minCapacity = minCapacity;
        this.maxCapacity = maxCapacity;
        this.description = description;
    }

    public int getMinCapacity() {
        return minCapacity;
    }

    public int getMaxCapacity() {
        return maxCapacity;
    }

    public String getDescription() {
        return description;
    }

    public static RoomCapacityType getByCapacity(int capacity) {
        if (capacity >= SMALL.minCapacity && capacity <= SMALL.maxCapacity) {
            return SMALL;
        } else if (capacity >= MEDIUM.minCapacity && capacity <= MEDIUM.maxCapacity) {
            return MEDIUM;
        } else if (capacity >= LARGE.minCapacity && capacity <= LARGE.maxCapacity) {
            return LARGE;
        }
        return LARGE; // Default to LARGE for capacities > 50
    }

    public String getDisplayName() {
        return description + " (" + minCapacity + "-" + maxCapacity + " people)";
    }
}
