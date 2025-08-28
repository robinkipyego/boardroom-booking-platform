package com.example.booking.repository;

import com.example.booking.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Find user by username (case-insensitive)
     */
    Optional<User> findByUsernameIgnoreCase(String username);

    /**
     * Find user by email (case-insensitive)
     */
    Optional<User> findByEmailIgnoreCase(String email);

    /**
     * Check if username exists (case-insensitive)
     */
    boolean existsByUsernameIgnoreCase(String username);

    /**
     * Check if email exists (case-insensitive)
     */
    boolean existsByEmailIgnoreCase(String email);

    /**
     * Find all active users
     */
    List<User> findByIsActiveTrue();

    /**
     * Find users by role
     */
    List<User> findByRole(User.UserRole role);

    /**
     * Find active users by role
     */
    List<User> findByRoleAndIsActiveTrue(User.UserRole role);

    /**
     * Find users by department (case-insensitive)
     */
    List<User> findByDepartmentIgnoreCaseAndIsActiveTrue(String department);

    /**
     * Search users by full name or username (case-insensitive)
     */
    @Query("SELECT u FROM User u WHERE " +
           "(LOWER(u.fullName) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(u.username) LIKE LOWER(CONCAT('%', :searchTerm, '%'))) AND " +
           "u.isActive = true")
    List<User> searchActiveUsers(@Param("searchTerm") String searchTerm);

    /**
     * Find users who are boardroom admins
     */
    @Query("SELECT DISTINCT u FROM User u JOIN u.adminAssignments ba WHERE ba.isActive = true AND u.isActive = true")
    List<User> findBoardroomAdmins();

    /**
     * Find users who are not assigned as admin to any boardroom
     */
    @Query("SELECT u FROM User u WHERE u.isActive = true AND " +
           "u.id NOT IN (SELECT ba.user.id FROM BoardroomAdmin ba WHERE ba.isActive = true)")
    List<User> findUsersNotAssignedAsAdmin();

    /**
     * Count active users by role
     */
    long countByRoleAndIsActiveTrue(User.UserRole role);

    /**
     * Find users with bookings in a specific date range
     */
    @Query("SELECT DISTINCT u FROM User u JOIN u.bookings b WHERE " +
           "b.bookingDate >= :startDate AND b.bookingDate <= :endDate AND " +
           "b.status IN ('CONFIRMED', 'IN_PROGRESS') AND u.isActive = true")
    List<User> findUsersWithBookingsInDateRange(@Param("startDate") java.time.LocalDate startDate, 
                                               @Param("endDate") java.time.LocalDate endDate);

    /**
     * Find top users by booking count
     */
    @Query("SELECT u FROM User u JOIN u.bookings b WHERE u.isActive = true " +
           "GROUP BY u ORDER BY COUNT(b) DESC")
    List<User> findTopUsersByBookingCount();
}
