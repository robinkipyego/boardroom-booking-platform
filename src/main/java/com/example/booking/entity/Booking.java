package com.example.booking.entity;

import com.example.booking.enums.BookingStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
@Table(name = "bookings")
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Boardroom is required")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "boardroom_id", nullable = false)
    private Boardroom boardroom;

    @NotNull(message = "User is required")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @NotNull(message = "Booking date is required")
    @Column(name = "booking_date", nullable = false)
    private LocalDate bookingDate;

    @NotNull(message = "Start time is required")
    @Column(name = "start_time", nullable = false)
    private LocalTime startTime;

    @NotNull(message = "End time is required")
    @Column(name = "end_time", nullable = false)
    private LocalTime endTime;

    @NotBlank(message = "Purpose is required")
    @Column(nullable = false, columnDefinition = "TEXT")
    private String purpose;

    @Min(value = 1, message = "Attendee count must be at least 1")
    @Column(name = "attendee_count", nullable = false)
    private Integer attendeeCount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BookingStatus status = BookingStatus.PENDING;

    @Column(name = "special_requirements", columnDefinition = "TEXT")
    private String specialRequirements;

    @Column(name = "contact_number")
    private String contactNumber;

    @Column(name = "approved_by")
    private String approvedBy;

    @Column(name = "approved_at")
    private LocalDateTime approvedAt;

    @Column(name = "cancelled_reason", columnDefinition = "TEXT")
    private String cancelledReason;

    @Column(name = "cancelled_at")
    private LocalDateTime cancelledAt;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // Constructors
    public Booking() {
    }

    public Booking(Boardroom boardroom, User user, LocalDate bookingDate, 
                   LocalTime startTime, LocalTime endTime, String purpose, Integer attendeeCount) {
        this.boardroom = boardroom;
        this.user = user;
        this.bookingDate = bookingDate;
        this.startTime = startTime;
        this.endTime = endTime;
        this.purpose = purpose;
        this.attendeeCount = attendeeCount;
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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public LocalDate getBookingDate() {
        return bookingDate;
    }

    public void setBookingDate(LocalDate bookingDate) {
        this.bookingDate = bookingDate;
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

    public String getPurpose() {
        return purpose;
    }

    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }

    public Integer getAttendeeCount() {
        return attendeeCount;
    }

    public void setAttendeeCount(Integer attendeeCount) {
        this.attendeeCount = attendeeCount;
    }

    public BookingStatus getStatus() {
        return status;
    }

    public void setStatus(BookingStatus status) {
        this.status = status;
    }

    public String getSpecialRequirements() {
        return specialRequirements;
    }

    public void setSpecialRequirements(String specialRequirements) {
        this.specialRequirements = specialRequirements;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public String getApprovedBy() {
        return approvedBy;
    }

    public void setApprovedBy(String approvedBy) {
        this.approvedBy = approvedBy;
    }

    public LocalDateTime getApprovedAt() {
        return approvedAt;
    }

    public void setApprovedAt(LocalDateTime approvedAt) {
        this.approvedAt = approvedAt;
    }

    public String getCancelledReason() {
        return cancelledReason;
    }

    public void setCancelledReason(String cancelledReason) {
        this.cancelledReason = cancelledReason;
    }

    public LocalDateTime getCancelledAt() {
        return cancelledAt;
    }

    public void setCancelledAt(LocalDateTime cancelledAt) {
        this.cancelledAt = cancelledAt;
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

    public String getUserDisplayName() {
        return user != null ? user.getDisplayName() : "Unknown User";
    }

    public String getTimeSlot() {
        return startTime + " - " + endTime;
    }

    public String getStatusDisplayName() {
        return status != null ? status.getDisplayName() : "Unknown";
    }

    public String getStatusBadgeClass() {
        if (status == null) return "badge-secondary";
        
        switch (status) {
            case PENDING:
                return "badge-warning";
            case CONFIRMED:
                return "badge-success";
            case CANCELLED:
                return "badge-danger";
            case COMPLETED:
                return "badge-info";
            case IN_PROGRESS:
                return "badge-primary";
            default:
                return "badge-secondary";
        }
    }

    public boolean isActive() {
        return status != null && status.isActive();
    }

    public boolean canBeCancelled() {
        return status != null && status.canBeCancelled();
    }

    public boolean canBeModified() {
        return status != null && status.canBeModified();
    }

    public boolean isUpcoming() {
        LocalDateTime bookingDateTime = LocalDateTime.of(bookingDate, startTime);
        return bookingDateTime.isAfter(LocalDateTime.now()) && isActive();
    }

    public boolean isInProgress() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime bookingStart = LocalDateTime.of(bookingDate, startTime);
        LocalDateTime bookingEnd = LocalDateTime.of(bookingDate, endTime);
        
        return now.isAfter(bookingStart) && now.isBefore(bookingEnd) && isActive();
    }

    public boolean isPast() {
        LocalDateTime bookingDateTime = LocalDateTime.of(bookingDate, endTime);
        return bookingDateTime.isBefore(LocalDateTime.now());
    }

    public long getDurationInMinutes() {
        return java.time.Duration.between(startTime, endTime).toMinutes();
    }

    public String getShortPurpose() {
        if (purpose == null || purpose.length() <= 50) {
            return purpose;
        }
        return purpose.substring(0, 47) + "...";
    }

    public void approve(String approvedBy) {
        this.status = BookingStatus.CONFIRMED;
        this.approvedBy = approvedBy;
        this.approvedAt = LocalDateTime.now();
    }

    public void cancel(String reason) {
        this.status = BookingStatus.CANCELLED;
        this.cancelledReason = reason;
        this.cancelledAt = LocalDateTime.now();
    }

    public void complete() {
        this.status = BookingStatus.COMPLETED;
    }

    @Override
    public String toString() {
        return "Booking{" +
                "id=" + id +
                ", boardroomId=" + (boardroom != null ? boardroom.getId() : null) +
                ", userId=" + (user != null ? user.getId() : null) +
                ", bookingDate=" + bookingDate +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", status=" + status +
                ", attendeeCount=" + attendeeCount +
                '}';
    }
}
