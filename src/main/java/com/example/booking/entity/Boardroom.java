package com.example.booking.entity;

import com.example.booking.enums.RoomCapacityType;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "boardrooms")
public class Boardroom {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Boardroom name is required")
    @Size(max = 100, message = "Boardroom name cannot exceed 100 characters")
    @Column(nullable = false)
    private String name;

    @NotBlank(message = "Location is required")
    @Size(max = 200, message = "Location cannot exceed 200 characters")
    @Column(nullable = false)
    private String location;

    @Min(value = 1, message = "Capacity must be at least 1")
    @Column(nullable = false)
    private Integer capacity;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(columnDefinition = "TEXT")
    private String amenities;

    @Enumerated(EnumType.STRING)
    @Column(name = "capacity_type", nullable = false)
    private RoomCapacityType capacityType;

    @Column(name = "is_active")
    private boolean isActive = true;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // Relationships
    @OneToMany(mappedBy = "boardroom", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Booking> bookings;

    @OneToMany(mappedBy = "boardroom", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<BoardroomAdmin> adminAssignments;

    @OneToMany(mappedBy = "boardroom", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<AvailabilitySlot> availabilitySlots;

    // Constructors
    public Boardroom() {
    }

    public Boardroom(String name, String location, Integer capacity, String description) {
        this.name = name;
        this.location = location;
        this.capacity = capacity;
        this.description = description;
        this.capacityType = RoomCapacityType.getByCapacity(capacity);
    }

    // Lifecycle callbacks
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        if (capacityType == null && capacity != null) {
            capacityType = RoomCapacityType.getByCapacity(capacity);
        }
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
        if (capacity != null) {
            capacityType = RoomCapacityType.getByCapacity(capacity);
        }
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Integer getCapacity() {
        return capacity;
    }

    public void setCapacity(Integer capacity) {
        this.capacity = capacity;
        if (capacity != null) {
            this.capacityType = RoomCapacityType.getByCapacity(capacity);
        }
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAmenities() {
        return amenities;
    }

    public void setAmenities(String amenities) {
        this.amenities = amenities;
    }

    public RoomCapacityType getCapacityType() {
        return capacityType;
    }

    public void setCapacityType(RoomCapacityType capacityType) {
        this.capacityType = capacityType;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public List<Booking> getBookings() {
        return bookings;
    }

    public void setBookings(List<Booking> bookings) {
        this.bookings = bookings;
    }

    public List<BoardroomAdmin> getAdminAssignments() {
        return adminAssignments;
    }

    public void setAdminAssignments(List<BoardroomAdmin> adminAssignments) {
        this.adminAssignments = adminAssignments;
    }

    public List<AvailabilitySlot> getAvailabilitySlots() {
        return availabilitySlots;
    }

    public void setAvailabilitySlots(List<AvailabilitySlot> availabilitySlots) {
        this.availabilitySlots = availabilitySlots;
    }

    // Helper methods
    public String getCapacityTypeDisplayName() {
        return capacityType != null ? capacityType.getDisplayName() : "Unknown";
    }

    public String getCapacityBadgeClass() {
        if (capacityType == null) return "badge-secondary";
        
        switch (capacityType) {
            case SMALL:
                return "badge-success";
            case MEDIUM:
                return "badge-warning";
            case LARGE:
                return "badge-danger";
            default:
                return "badge-secondary";
        }
    }

    public boolean hasAdmins() {
        return adminAssignments != null && !adminAssignments.isEmpty();
    }

    public String getDisplayName() {
        return name + " (" + location + ")";
    }

    public String getShortDescription() {
        if (description == null || description.length() <= 100) {
            return description;
        }
        return description.substring(0, 97) + "...";
    }

    @Override
    public String toString() {
        return "Boardroom{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", location='" + location + '\'' +
                ", capacity=" + capacity +
                ", capacityType=" + capacityType +
                ", isActive=" + isActive +
                '}';
    }
}
