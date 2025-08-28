package com.example.booking.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
@Table(name = "availability_slots")
public class AvailabilitySlot {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Boardroom is required")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "boardroom_id", nullable = false)
    private Boardroom boardroom;

    @NotNull(message = "Date is required")
    @Column(nullable = false)
    private LocalDate date;

    @NotNull(message = "Start time is required")
    @Column(name = "start_time", nullable = false)
    private LocalTime startTime;

    @NotNull(message = "End time is required")
    @Column(name = "end_time", nullable = false)
    private LocalTime endTime;

    @Column(name = "is_available", nullable = false)
    private boolean isAvailable = true;

    @Column(name = "admin_notes", columnDefinition = "TEXT")
    private String adminNotes;

    @Column(name = "blocked_by")
    private String blockedBy;

    @Column(name = "blocked_reason", columnDefinition = "TEXT")
    private String blockedReason;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // Constructors
    public AvailabilitySlot() {
    }

    public AvailabilitySlot(Boardroom boardroom, LocalDate date, LocalTime startTime, LocalTime endTime) {
        this.boardroom = boardroom;
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public AvailabilitySlot(Boardroom boardroom, LocalDate date, LocalTime startTime, 
                           LocalTime endTime, boolean isAvailable, String adminNotes) {
        this.boardroom = boardroom;
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
        this.isAvailable = isAvailable;
        this.adminNotes = adminNotes;
    }

    // Lifecycle callbacks
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boardroom getBoardroom() {
        return boardroom;
    }

    public void setBoardroom(Boardroom boardroom) {
        this.boardroom = boardroom;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public void setAvailable(boolean available) {
        isAvailable = available;
    }

    public String getAdminNotes() {
        return adminNotes;
    }

    public void setAdminNotes(String adminNotes) {
        this.adminNotes = adminNotes;
    }

    public String getBlockedBy() {
        return blockedBy;
    }

    public void setBlockedBy(String blockedBy) {
        this.blockedBy = blockedBy;
    }

    public String getBlockedReason() {
        return blockedReason;
    }

    public void setBlockedReason(String blockedReason) {
        this.blockedReason = blockedReason;
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

    // Helper methods
    public String getBoardroomDisplayName() {
        return boardroom != null ? boardroom.getDisplayName() : "Unknown Boardroom";
    }

    public String getTimeSlot() {
        return startTime + " - " + endTime;
    }

    public String getAvailabilityStatus() {
        return isAvailable ? "Available" : "Blocked";
    }

    public String getStatusBadgeClass() {
        return isAvailable ? "badge-success" : "badge-danger";
    }

    public long getDurationInMinutes() {
        return java.time.Duration.between(startTime, endTime).toMinutes();
    }

    public boolean isPast() {
        LocalDateTime slotDateTime = LocalDateTime.of(date, endTime);
        return slotDateTime.isBefore(LocalDateTime.now());
    }

    public boolean isToday() {
        return date.equals(LocalDate.now());
    }

    public boolean isFuture() {
        LocalDateTime slotDateTime = LocalDateTime.of(date, startTime);
        return slotDateTime.isAfter(LocalDateTime.now());
    }

    public boolean overlaps(LocalTime checkStartTime, LocalTime checkEndTime) {
        return startTime.isBefore(checkEndTime) && endTime.isAfter(checkStartTime);
    }

    public boolean overlaps(AvailabilitySlot other) {
        if (other == null || !this.date.equals(other.date)) {
            return false;
        }
        return overlaps(other.startTime, other.endTime);
    }

    public void block(String blockedBy, String reason) {
        this.isAvailable = false;
        this.blockedBy = blockedBy;
        this.blockedReason = reason;
    }

    public void unblock() {
        this.isAvailable = true;
        this.blockedBy = null;
        this.blockedReason = null;
    }

    public String getDisplayInfo() {
        StringBuilder info = new StringBuilder();
        info.append(getTimeSlot()).append(" - ").append(getAvailabilityStatus());
        
        if (!isAvailable && blockedReason != null && !blockedReason.trim().isEmpty()) {
            info.append(" (").append(blockedReason).append(")");
        }
        
        return info.toString();
    }

    @Override
    public String toString() {
        return "AvailabilitySlot{" +
                "id=" + id +
                ", boardroomId=" + (boardroom != null ? boardroom.getId() : null) +
                ", date=" + date +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", isAvailable=" + isAvailable +
                ", blockedBy='" + blockedBy + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AvailabilitySlot)) return false;
        
        AvailabilitySlot that = (AvailabilitySlot) o;
        
        if (boardroom != null ? !boardroom.getId().equals(that.boardroom != null ? that.boardroom.getId() : null) : that.boardroom != null) return false;
        if (date != null ? !date.equals(that.date) : that.date != null) return false;
        if (startTime != null ? !startTime.equals(that.startTime) : that.startTime != null) return false;
        return endTime != null ? endTime.equals(that.endTime) : that.endTime == null;
    }

    @Override
    public int hashCode() {
        int result = boardroom != null ? boardroom.getId().hashCode() : 0;
        result = 31 * result + (date != null ? date.hashCode() : 0);
        result = 31 * result + (startTime != null ? startTime.hashCode() : 0);
        result = 31 * result + (endTime != null ? endTime.hashCode() : 0);
        return result;
    }
}
