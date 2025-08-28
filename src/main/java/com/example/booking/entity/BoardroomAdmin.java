package com.example.booking.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

@Entity
@Table(name = "boardroom_admins", 
       uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "boardroom_id"}))
public class BoardroomAdmin {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "User is required")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @NotNull(message = "Boardroom is required")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "boardroom_id", nullable = false)
    private Boardroom boardroom;

    @Column(name = "assigned_date", nullable = false)
    private LocalDateTime assignedDate;

    @Column(name = "is_active")
    private boolean isActive = true;

    @Column(name = "assigned_by")
    private String assignedBy;

    @Column(name = "notes", columnDefinition = "TEXT")
    private String notes;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // Constructors
    public BoardroomAdmin() {
    }

    public BoardroomAdmin(User user, Boardroom boardroom, String assignedBy) {
        this.user = user;
        this.boardroom = boardroom;
        this.assignedBy = assignedBy;
        this.assignedDate = LocalDateTime.now();
    }

    // Lifecycle callbacks
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        if (assignedDate == null) {
            assignedDate = LocalDateTime.now();
        }
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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Boardroom getBoardroom() {
        return boardroom;
    }

    public void setBoardroom(Boardroom boardroom) {
        this.boardroom = boardroom;
    }

    public LocalDateTime getAssignedDate() {
        return assignedDate;
    }

    public void setAssignedDate(LocalDateTime assignedDate) {
        this.assignedDate = assignedDate;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public String getAssignedBy() {
        return assignedBy;
    }

    public void setAssignedBy(String assignedBy) {
        this.assignedBy = assignedBy;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
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
    public String getUserDisplayName() {
        return user != null ? user.getDisplayName() : "Unknown User";
    }

    public String getBoardroomDisplayName() {
        return boardroom != null ? boardroom.getDisplayName() : "Unknown Boardroom";
    }

    public boolean canManageBoardroom() {
        return isActive && user != null && user.isEnabled();
    }

    public String getAssignmentSummary() {
        return String.format("%s is admin for %s (assigned on %s)", 
                getUserDisplayName(), 
                getBoardroomDisplayName(), 
                assignedDate != null ? assignedDate.toLocalDate().toString() : "Unknown");
    }

    @Override
    public String toString() {
        return "BoardroomAdmin{" +
                "id=" + id +
                ", userId=" + (user != null ? user.getId() : null) +
                ", boardroomId=" + (boardroom != null ? boardroom.getId() : null) +
                ", assignedDate=" + assignedDate +
                ", isActive=" + isActive +
                ", assignedBy='" + assignedBy + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BoardroomAdmin)) return false;
        
        BoardroomAdmin that = (BoardroomAdmin) o;
        
        if (user != null ? !user.getId().equals(that.user != null ? that.user.getId() : null) : that.user != null) return false;
        return boardroom != null ? boardroom.getId().equals(that.boardroom != null ? that.boardroom.getId() : null) : that.boardroom == null;
    }

    @Override
    public int hashCode() {
        int result = user != null ? user.getId().hashCode() : 0;
        result = 31 * result + (boardroom != null ? boardroom.getId().hashCode() : 0);
        return result;
    }
}
