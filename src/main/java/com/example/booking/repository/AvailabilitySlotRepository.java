package com.example.booking.repository;

import com.example.booking.entity.AvailabilitySlot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface AvailabilitySlotRepository extends JpaRepository<AvailabilitySlot, Long> {

    /**
     * Find availability slots by boardroom ID
     */
    List<AvailabilitySlot> findByBoardroomIdOrderByDateAscStartTimeAsc(Long boardroomId);

    /**
     * Find availability slots by boardroom ID and date
     */
    List<AvailabilitySlot> findByBoardroomIdAndDateOrderByStartTimeAsc(Long boardroomId, LocalDate date);

    /**
     * Find availability slots by date
     */
    List<AvailabilitySlot> findByDateOrderByBoardroomIdAscStartTimeAsc(LocalDate date);

    /**
     * Find availability slots in date range
     */
    List<AvailabilitySlot> findByDateBetweenOrderByDateAscBoardroomIdAscStartTimeAsc(LocalDate startDate, LocalDate endDate);

    /**
     * Find blocked slots (not available)
     */
    List<AvailabilitySlot> findByIsAvailableFalseOrderByDateAscStartTimeAsc();

    /**
     * Find available slots
     */
    List<AvailabilitySlot> findByIsAvailableTrueOrderByDateAscStartTimeAsc();

    /**
     * Find blocked slots by boardroom
     */
    List<AvailabilitySlot> findByBoardroomIdAndIsAvailableFalseOrderByDateAscStartTimeAsc(Long boardroomId);

    /**
     * Find available slots by boardroom
     */
    List<AvailabilitySlot> findByBoardroomIdAndIsAvailableTrueOrderByDateAscStartTimeAsc(Long boardroomId);

    /**
     * Find blocked slots by boardroom and date
     */
    List<AvailabilitySlot> findByBoardroomIdAndDateAndIsAvailableFalseOrderByStartTimeAsc(Long boardroomId, LocalDate date);

    /**
     * Find available slots by boardroom and date
     */
    List<AvailabilitySlot> findByBoardroomIdAndDateAndIsAvailableTrueOrderByStartTimeAsc(Long boardroomId, LocalDate date);

    /**
     * Find slots blocked by a specific admin
     */
    List<AvailabilitySlot> findByBlockedByOrderByDateAscStartTimeAsc(String blockedBy);

    /**
     * Find overlapping slots for a specific boardroom and time range
     */
    @Query("SELECT slot FROM AvailabilitySlot slot WHERE slot.boardroom.id = :boardroomId AND " +
           "slot.date = :date AND " +
           "((slot.startTime < :endTime AND slot.endTime > :startTime))")
    List<AvailabilitySlot> findOverlappingSlots(@Param("boardroomId") Long boardroomId,
                                              @Param("date") LocalDate date,
                                              @Param("startTime") LocalTime startTime,
                                              @Param("endTime") LocalTime endTime);

    /**
     * Find blocked overlapping slots for a specific boardroom and time range
     */
    @Query("SELECT slot FROM AvailabilitySlot slot WHERE slot.boardroom.id = :boardroomId AND " +
           "slot.date = :date AND slot.isAvailable = false AND " +
           "((slot.startTime < :endTime AND slot.endTime > :startTime))")
    List<AvailabilitySlot> findBlockedOverlappingSlots(@Param("boardroomId") Long boardroomId,
                                                     @Param("date") LocalDate date,
                                                     @Param("startTime") LocalTime startTime,
                                                     @Param("endTime") LocalTime endTime);

    /**
     * Check if a time slot is blocked for a boardroom
     */
    @Query("SELECT CASE WHEN COUNT(slot) > 0 THEN true ELSE false END FROM AvailabilitySlot slot WHERE " +
           "slot.boardroom.id = :boardroomId AND slot.date = :date AND " +
           "slot.isAvailable = false AND " +
           "((slot.startTime < :endTime AND slot.endTime > :startTime))")
    boolean isTimeSlotBlocked(@Param("boardroomId") Long boardroomId,
                            @Param("date") LocalDate date,
                            @Param("startTime") LocalTime startTime,
                            @Param("endTime") LocalTime endTime);

    /**
     * Find slots for calendar view (specific month)
     */
    @Query("SELECT slot FROM AvailabilitySlot slot WHERE slot.boardroom.id = :boardroomId AND " +
           "YEAR(slot.date) = :year AND MONTH(slot.date) = :month " +
           "ORDER BY slot.date ASC, slot.startTime ASC")
    List<AvailabilitySlot> findSlotsForCalendar(@Param("boardroomId") Long boardroomId,
                                              @Param("year") int year,
                                              @Param("month") int month);

    /**
     * Find future blocked slots
     */
    @Query("SELECT slot FROM AvailabilitySlot slot WHERE slot.date >= :currentDate AND " +
           "slot.isAvailable = false ORDER BY slot.date ASC, slot.startTime ASC")
    List<AvailabilitySlot> findFutureBlockedSlots(@Param("currentDate") LocalDate currentDate);

    /**
     * Find past blocked slots
     */
    @Query("SELECT slot FROM AvailabilitySlot slot WHERE slot.date < :currentDate AND " +
           "slot.isAvailable = false ORDER BY slot.date DESC, slot.startTime DESC")
    List<AvailabilitySlot> findPastBlockedSlots(@Param("currentDate") LocalDate currentDate);

    /**
     * Find slots by boardroom in date range
     */
    @Query("SELECT slot FROM AvailabilitySlot slot WHERE slot.boardroom.id = :boardroomId AND " +
           "slot.date BETWEEN :startDate AND :endDate " +
           "ORDER BY slot.date ASC, slot.startTime ASC")
    List<AvailabilitySlot> findSlotsByBoardroomInDateRange(@Param("boardroomId") Long boardroomId,
                                                         @Param("startDate") LocalDate startDate,
                                                         @Param("endDate") LocalDate endDate);

    /**
     * Find blocked slots by boardroom in date range
     */
    @Query("SELECT slot FROM AvailabilitySlot slot WHERE slot.boardroom.id = :boardroomId AND " +
           "slot.date BETWEEN :startDate AND :endDate AND slot.isAvailable = false " +
           "ORDER BY slot.date ASC, slot.startTime ASC")
    List<AvailabilitySlot> findBlockedSlotsByBoardroomInDateRange(@Param("boardroomId") Long boardroomId,
                                                                @Param("startDate") LocalDate startDate,
                                                                @Param("endDate") LocalDate endDate);

    /**
     * Count blocked slots by boardroom
     */
    long countByBoardroomIdAndIsAvailableFalse(Long boardroomId);

    /**
     * Count available slots by boardroom
     */
    long countByBoardroomIdAndIsAvailableTrue(Long boardroomId);

    /**
     * Find slots with admin notes
     */
    @Query("SELECT slot FROM AvailabilitySlot slot WHERE slot.adminNotes IS NOT NULL AND " +
           "slot.adminNotes != '' ORDER BY slot.date ASC, slot.startTime ASC")
    List<AvailabilitySlot> findSlotsWithNotes();

    /**
     * Find today's blocked slots
     */
    @Query("SELECT slot FROM AvailabilitySlot slot WHERE slot.date = :today AND " +
           "slot.isAvailable = false ORDER BY slot.startTime ASC")
    List<AvailabilitySlot> findTodaysBlockedSlots(@Param("today") LocalDate today);

    /**
     * Find today's blocked slots by boardroom
     */
    @Query("SELECT slot FROM AvailabilitySlot slot WHERE slot.boardroom.id = :boardroomId AND " +
           "slot.date = :today AND slot.isAvailable = false ORDER BY slot.startTime ASC")
    List<AvailabilitySlot> findTodaysBlockedSlotsByBoardroom(@Param("boardroomId") Long boardroomId,
                                                           @Param("today") LocalDate today);

    /**
     * Delete old availability slots (cleanup)
     */
    @Query("DELETE FROM AvailabilitySlot slot WHERE slot.date < :cutoffDate")
    void deleteOldSlots(@Param("cutoffDate") LocalDate cutoffDate);

    /**
     * Find conflicting slots (overlapping time ranges for same boardroom and date)
     */
    @Query("SELECT slot1 FROM AvailabilitySlot slot1 WHERE EXISTS (" +
           "SELECT slot2 FROM AvailabilitySlot slot2 WHERE " +
           "slot1.id != slot2.id AND slot1.boardroom.id = slot2.boardroom.id AND " +
           "slot1.date = slot2.date AND " +
           "((slot1.startTime < slot2.endTime AND slot1.endTime > slot2.startTime))" +
           ") ORDER BY slot1.date ASC, slot1.startTime ASC")
    List<AvailabilitySlot> findConflictingSlots();

    /**
     * Find specific slot by boardroom, date and time
     */
    Optional<AvailabilitySlot> findByBoardroomIdAndDateAndStartTimeAndEndTime(Long boardroomId, 
                                                                            LocalDate date, 
                                                                            LocalTime startTime, 
                                                                            LocalTime endTime);

    /**
     * Find slots by blocked reason
     */
    @Query("SELECT slot FROM AvailabilitySlot slot WHERE slot.isAvailable = false AND " +
           "LOWER(slot.blockedReason) LIKE LOWER(CONCAT('%', :reason, '%')) " +
           "ORDER BY slot.date ASC, slot.startTime ASC")
    List<AvailabilitySlot> findSlotsByBlockedReason(@Param("reason") String reason);
}
