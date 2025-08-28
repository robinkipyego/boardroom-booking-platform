package com.example.booking.repository;

import com.example.booking.entity.Boardroom;
import com.example.booking.enums.RoomCapacityType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface BoardroomRepository extends JpaRepository<Boardroom, Long> {

    /**
     * Find all active boardrooms
     */
    List<Boardroom> findByIsActiveTrueOrderByNameAsc();

    /**
     * Find boardrooms by capacity type
     */
    List<Boardroom> findByCapacityTypeAndIsActiveTrueOrderByCapacityAsc(RoomCapacityType capacityType);

    /**
     * Find boardrooms by minimum capacity
     */
    List<Boardroom> findByCapacityGreaterThanEqualAndIsActiveTrueOrderByCapacityAsc(Integer minCapacity);

    /**
     * Find boardrooms by capacity range
     */
    List<Boardroom> findByCapacityBetweenAndIsActiveTrueOrderByCapacityAsc(Integer minCapacity, Integer maxCapacity);

    /**
     * Find boardrooms by location (case-insensitive)
     */
    List<Boardroom> findByLocationIgnoreCaseAndIsActiveTrueOrderByNameAsc(String location);

    /**
     * Search boardrooms by name or location (case-insensitive)
     */
    @Query("SELECT b FROM Boardroom b WHERE " +
           "(LOWER(b.name) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(b.location) LIKE LOWER(CONCAT('%', :searchTerm, '%'))) AND " +
           "b.isActive = true ORDER BY b.name ASC")
    List<Boardroom> searchActiveBoardrooms(@Param("searchTerm") String searchTerm);

    /**
     * Find available boardrooms for a specific date and time slot
     */
    @Query("SELECT b FROM Boardroom b WHERE b.isActive = true AND " +
           "b.id NOT IN (" +
           "  SELECT booking.boardroom.id FROM Booking booking WHERE " +
           "  booking.bookingDate = :date AND " +
           "  booking.status IN ('CONFIRMED', 'IN_PROGRESS') AND " +
           "  ((booking.startTime < :endTime AND booking.endTime > :startTime))" +
           ") AND " +
           "b.id NOT IN (" +
           "  SELECT slot.boardroom.id FROM AvailabilitySlot slot WHERE " +
           "  slot.date = :date AND slot.isAvailable = false AND " +
           "  ((slot.startTime < :endTime AND slot.endTime > :startTime))" +
           ") ORDER BY b.name ASC")
    List<Boardroom> findAvailableBoardrooms(@Param("date") LocalDate date, 
                                          @Param("startTime") LocalTime startTime, 
                                          @Param("endTime") LocalTime endTime);

    /**
     * Find available boardrooms by capacity type for a specific date and time slot
     */
    @Query("SELECT b FROM Boardroom b WHERE b.isActive = true AND " +
           "b.capacityType = :capacityType AND " +
           "b.id NOT IN (" +
           "  SELECT booking.boardroom.id FROM Booking booking WHERE " +
           "  booking.bookingDate = :date AND " +
           "  booking.status IN ('CONFIRMED', 'IN_PROGRESS') AND " +
           "  ((booking.startTime < :endTime AND booking.endTime > :startTime))" +
           ") AND " +
           "b.id NOT IN (" +
           "  SELECT slot.boardroom.id FROM AvailabilitySlot slot WHERE " +
           "  slot.date = :date AND slot.isAvailable = false AND " +
           "  ((slot.startTime < :endTime AND slot.endTime > :startTime))" +
           ") ORDER BY b.capacity ASC")
    List<Boardroom> findAvailableBoardroomsByCapacityType(@Param("capacityType") RoomCapacityType capacityType,
                                                         @Param("date") LocalDate date, 
                                                         @Param("startTime") LocalTime startTime, 
                                                         @Param("endTime") LocalTime endTime);

    /**
     * Find boardrooms managed by a specific admin user
     */
    @Query("SELECT b FROM Boardroom b JOIN b.adminAssignments ba WHERE " +
           "ba.user.id = :userId AND ba.isActive = true AND b.isActive = true " +
           "ORDER BY b.name ASC")
    List<Boardroom> findBoardroomsByAdminUserId(@Param("userId") Long userId);

    /**
     * Find boardrooms without any admin assignments
     */
    @Query("SELECT b FROM Boardroom b WHERE b.isActive = true AND " +
           "b.id NOT IN (SELECT ba.boardroom.id FROM BoardroomAdmin ba WHERE ba.isActive = true) " +
           "ORDER BY b.name ASC")
    List<Boardroom> findBoardroomsWithoutAdmins();

    /**
     * Check if a boardroom is available for a specific time slot
     */
    @Query("SELECT CASE WHEN COUNT(b) > 0 THEN false ELSE true END FROM Booking b WHERE " +
           "b.boardroom.id = :boardroomId AND b.bookingDate = :date AND " +
           "b.status IN ('CONFIRMED', 'IN_PROGRESS') AND " +
           "((b.startTime < :endTime AND b.endTime > :startTime))")
    boolean isBoardroomAvailableForBooking(@Param("boardroomId") Long boardroomId,
                                         @Param("date") LocalDate date,
                                         @Param("startTime") LocalTime startTime,
                                         @Param("endTime") LocalTime endTime);

    /**
     * Check if a boardroom is blocked by admin for a specific time slot
     */
    @Query("SELECT CASE WHEN COUNT(slot) > 0 THEN true ELSE false END FROM AvailabilitySlot slot WHERE " +
           "slot.boardroom.id = :boardroomId AND slot.date = :date AND " +
           "slot.isAvailable = false AND " +
           "((slot.startTime < :endTime AND slot.endTime > :startTime))")
    boolean isBoardroomBlockedByAdmin(@Param("boardroomId") Long boardroomId,
                                    @Param("date") LocalDate date,
                                    @Param("startTime") LocalTime startTime,
                                    @Param("endTime") LocalTime endTime);

    /**
     * Count boardrooms by capacity type
     */
    long countByCapacityTypeAndIsActiveTrue(RoomCapacityType capacityType);

    /**
     * Find most booked boardrooms
     */
    @Query("SELECT b FROM Boardroom b JOIN b.bookings booking WHERE b.isActive = true " +
           "GROUP BY b ORDER BY COUNT(booking) DESC")
    List<Boardroom> findMostBookedBoardrooms();

    /**
     * Find boardrooms with upcoming bookings
     */
    @Query("SELECT DISTINCT b FROM Boardroom b JOIN b.bookings booking WHERE " +
           "b.isActive = true AND booking.bookingDate >= :currentDate AND " +
           "booking.status IN ('CONFIRMED', 'IN_PROGRESS') " +
           "ORDER BY b.name ASC")
    List<Boardroom> findBoardroomsWithUpcomingBookings(@Param("currentDate") LocalDate currentDate);

    /**
     * Find boardroom by name (case-insensitive)
     */
    Optional<Boardroom> findByNameIgnoreCaseAndIsActiveTrue(String name);
}
