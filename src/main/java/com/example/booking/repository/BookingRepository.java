package com.example.booking.repository;

import com.example.booking.entity.Booking;
import com.example.booking.enums.BookingStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {

    /**
     * Find bookings by user ID
     */
    List<Booking> findByUserIdOrderByBookingDateDescStartTimeDesc(Long userId);

    /**
     * Find bookings by boardroom ID
     */
    List<Booking> findByBoardroomIdOrderByBookingDateDescStartTimeDesc(Long boardroomId);

    /**
     * Find bookings by status
     */
    List<Booking> findByStatusOrderByBookingDateDescStartTimeDesc(BookingStatus status);

    /**
     * Find bookings by user and status
     */
    List<Booking> findByUserIdAndStatusOrderByBookingDateDescStartTimeDesc(Long userId, BookingStatus status);

    /**
     * Find bookings by boardroom and status
     */
    List<Booking> findByBoardroomIdAndStatusOrderByBookingDateDescStartTimeDesc(Long boardroomId, BookingStatus status);

    /**
     * Find bookings for a specific date
     */
    List<Booking> findByBookingDateOrderByStartTimeAsc(LocalDate date);

    /**
     * Find bookings for a specific boardroom and date
     */
    List<Booking> findByBoardroomIdAndBookingDateOrderByStartTimeAsc(Long boardroomId, LocalDate date);

    /**
     * Find bookings in a date range
     */
    List<Booking> findByBookingDateBetweenOrderByBookingDateDescStartTimeDesc(LocalDate startDate, LocalDate endDate);

    /**
     * Find active bookings (confirmed or in progress)
     */
    @Query("SELECT b FROM Booking b WHERE b.status IN ('CONFIRMED', 'IN_PROGRESS') " +
           "ORDER BY b.bookingDate DESC, b.startTime DESC")
    List<Booking> findActiveBookings();

    /**
     * Find upcoming bookings for a user
     */
    @Query("SELECT b FROM Booking b WHERE b.user.id = :userId AND " +
           "b.bookingDate >= :currentDate AND b.status IN ('CONFIRMED', 'IN_PROGRESS') " +
           "ORDER BY b.bookingDate ASC, b.startTime ASC")
    List<Booking> findUpcomingBookingsByUserId(@Param("userId") Long userId, @Param("currentDate") LocalDate currentDate);

    /**
     * Find upcoming bookings for a boardroom
     */
    @Query("SELECT b FROM Booking b WHERE b.boardroom.id = :boardroomId AND " +
           "b.bookingDate >= :currentDate AND b.status IN ('CONFIRMED', 'IN_PROGRESS') " +
           "ORDER BY b.bookingDate ASC, b.startTime ASC")
    List<Booking> findUpcomingBookingsByBoardroomId(@Param("boardroomId") Long boardroomId, @Param("currentDate") LocalDate currentDate);

    /**
     * Find past bookings for a user
     */
    @Query("SELECT b FROM Booking b WHERE b.user.id = :userId AND " +
           "b.bookingDate < :currentDate " +
           "ORDER BY b.bookingDate DESC, b.startTime DESC")
    List<Booking> findPastBookingsByUserId(@Param("userId") Long userId, @Param("currentDate") LocalDate currentDate);

    /**
     * Check for conflicting bookings
     */
    @Query("SELECT b FROM Booking b WHERE b.boardroom.id = :boardroomId AND " +
           "b.bookingDate = :date AND b.status IN ('CONFIRMED', 'IN_PROGRESS') AND " +
           "((b.startTime < :endTime AND b.endTime > :startTime))")
    List<Booking> findConflictingBookings(@Param("boardroomId") Long boardroomId,
                                        @Param("date") LocalDate date,
                                        @Param("startTime") LocalTime startTime,
                                        @Param("endTime") LocalTime endTime);

    /**
     * Check for conflicting bookings excluding a specific booking
     */
    @Query("SELECT b FROM Booking b WHERE b.boardroom.id = :boardroomId AND " +
           "b.bookingDate = :date AND b.status IN ('CONFIRMED', 'IN_PROGRESS') AND " +
           "b.id != :excludeBookingId AND " +
           "((b.startTime < :endTime AND b.endTime > :startTime))")
    List<Booking> findConflictingBookingsExcluding(@Param("boardroomId") Long boardroomId,
                                                  @Param("date") LocalDate date,
                                                  @Param("startTime") LocalTime startTime,
                                                  @Param("endTime") LocalTime endTime,
                                                  @Param("excludeBookingId") Long excludeBookingId);

    /**
     * Find bookings that need to be marked as completed
     */
    @Query("SELECT b FROM Booking b WHERE b.status = 'CONFIRMED' AND " +
           "b.bookingDate < :currentDate")
    List<Booking> findBookingsToComplete(@Param("currentDate") LocalDate currentDate);

    /**
     * Find bookings currently in progress
     */
    @Query("SELECT b FROM Booking b WHERE b.status = 'CONFIRMED' AND " +
           "b.bookingDate = :currentDate AND " +
           "b.startTime <= :currentTime AND b.endTime > :currentTime")
    List<Booking> findBookingsInProgress(@Param("currentDate") LocalDate currentDate, 
                                       @Param("currentTime") LocalTime currentTime);

    /**
     * Find pending bookings that need approval
     */
    @Query("SELECT b FROM Booking b WHERE b.status = 'PENDING' AND " +
           "b.bookingDate >= :currentDate " +
           "ORDER BY b.createdAt ASC")
    List<Booking> findPendingBookings(@Param("currentDate") LocalDate currentDate);

    /**
     * Find bookings by user in a specific date range
     */
    @Query("SELECT b FROM Booking b WHERE b.user.id = :userId AND " +
           "b.bookingDate BETWEEN :startDate AND :endDate " +
           "ORDER BY b.bookingDate DESC, b.startTime DESC")
    List<Booking> findBookingsByUserInDateRange(@Param("userId") Long userId,
                                              @Param("startDate") LocalDate startDate,
                                              @Param("endDate") LocalDate endDate);

    /**
     * Find bookings by boardroom in a specific date range
     */
    @Query("SELECT b FROM Booking b WHERE b.boardroom.id = :boardroomId AND " +
           "b.bookingDate BETWEEN :startDate AND :endDate " +
           "ORDER BY b.bookingDate DESC, b.startTime DESC")
    List<Booking> findBookingsByBoardroomInDateRange(@Param("boardroomId") Long boardroomId,
                                                   @Param("startDate") LocalDate startDate,
                                                   @Param("endDate") LocalDate endDate);

    /**
     * Count bookings by status
     */
    long countByStatus(BookingStatus status);

    /**
     * Count bookings by user and status
     */
    long countByUserIdAndStatus(Long userId, BookingStatus status);

    /**
     * Count bookings by boardroom and status
     */
    long countByBoardroomIdAndStatus(Long boardroomId, BookingStatus status);

    /**
     * Find most active users by booking count
     */
    @Query("SELECT b.user.id, COUNT(b) as bookingCount FROM Booking b " +
           "WHERE b.status IN ('CONFIRMED', 'COMPLETED') " +
           "GROUP BY b.user.id ORDER BY bookingCount DESC")
    List<Object[]> findMostActiveUsers();

    /**
     * Find most booked boardrooms
     */
    @Query("SELECT b.boardroom.id, COUNT(b) as bookingCount FROM Booking b " +
           "WHERE b.status IN ('CONFIRMED', 'COMPLETED') " +
           "GROUP BY b.boardroom.id ORDER BY bookingCount DESC")
    List<Object[]> findMostBookedBoardrooms();

    /**
     * Find bookings for calendar view (specific month)
     */
    @Query("SELECT b FROM Booking b WHERE " +
           "YEAR(b.bookingDate) = :year AND MONTH(b.bookingDate) = :month AND " +
           "b.status IN ('CONFIRMED', 'IN_PROGRESS') " +
           "ORDER BY b.bookingDate ASC, b.startTime ASC")
    List<Booking> findBookingsForCalendar(@Param("year") int year, @Param("month") int month);

    /**
     * Find bookings for a specific boardroom calendar view
     */
    @Query("SELECT b FROM Booking b WHERE b.boardroom.id = :boardroomId AND " +
           "YEAR(b.bookingDate) = :year AND MONTH(b.bookingDate) = :month AND " +
           "b.status IN ('CONFIRMED', 'IN_PROGRESS') " +
           "ORDER BY b.bookingDate ASC, b.startTime ASC")
    List<Booking> findBookingsForBoardroomCalendar(@Param("boardroomId") Long boardroomId,
                                                  @Param("year") int year, 
                                                  @Param("month") int month);

    /**
     * Find today's bookings
     */
    @Query("SELECT b FROM Booking b WHERE b.bookingDate = :today AND " +
           "b.status IN ('CONFIRMED', 'IN_PROGRESS') " +
           "ORDER BY b.startTime ASC")
    List<Booking> findTodaysBookings(@Param("today") LocalDate today);
}
