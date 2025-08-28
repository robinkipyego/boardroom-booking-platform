# Detailed Implementation Plan for Boardroom Booking Platform

This plan outlines the creation of a boardroom booking platform for a corporate institution using a Spring Boot backend, a Java-based (Thymeleaf) frontend, and MySQL for persistence. The solution includes boardroom-specific admin roles, calendar management, capacity-based classification, and supports 20 boardrooms.

---

## Updated Requirements

- **Boardroom-Specific Admins:** Each boardroom has dedicated admins who can manage details and availability
- **Calendar Management:** Admins can set availability on calendar, users can view real-time availability
- **User Booking:** Any user can book free boardrooms, view occupied slots
- **20 Boardrooms:** System pre-configured with 20 boardrooms
- **Capacity Classification:** Boardrooms classified by capacity (Small: 1-6, Medium: 7-15, Large: 16+)
- **Admin-Managed Descriptions:** Boardroom descriptions and details managed by respective admins

---

## Project Structure

```
BoardroomBooking/

├── pom.xml
├── src/
│   └── main/
│       ├── java/com/example/booking/
│       │   ├── BoardroomBookingApplication.java
│       │   ├── controller/
│       │   │   ├── BoardroomController.java
│       │   │   ├── BookingController.java
│       │   │   ├── UserController.java
│       │   │   ├── AdminController.java
│       │   │   └── CalendarController.java
│       │   ├── entity/
│       │   │   ├── Boardroom.java
│       │   │   ├── Booking.java
│       │   │   ├── User.java
│       │   │   ├── BoardroomAdmin.java
│       │   │   └── AvailabilitySlot.java
│       │   ├── repository/
│       │   │   ├── BoardroomRepository.java
│       │   │   ├── BookingRepository.java
│       │   │   ├── UserRepository.java
│       │   │   ├── BoardroomAdminRepository.java
│       │   │   └── AvailabilitySlotRepository.java
│       │   ├── service/
│       │   │   ├── BoardroomService.java
│       │   │   ├── BookingService.java
│       │   │   ├── UserService.java
│       │   │   ├── AdminService.java
│       │   │   └── CalendarService.java
│       │   ├── dto/
│       │   │   ├── BoardroomDTO.java
│       │   │   ├── BookingDTO.java
│       │   │   └── CalendarEventDTO.java
│       │   ├── enums/
│       │   │   ├── RoomCapacityType.java
│       │   │   └── BookingStatus.java
│       │   ├── exception/
│       │   │   └── GlobalExceptionHandler.java
│       │   └── config/
│       │       ├── WebSecurityConfig.java
│       │       └── DataInitializer.java
│       └── resources/
│           ├── application.properties
│           ├── data.sql
│           ├── templates/
│           │   ├── index.html
│           │   ├── boardrooms.html
│           │   ├── boardroom-detail.html
│           │   ├── booking.html
│           │   ├── calendar.html
│           │   ├── admin/
│           │   │   ├── admin-dashboard.html
│           │   │   ├── manage-boardroom.html
│           │   │   └── manage-availability.html
│           │   ├── user/
│           │   │   ├── my-bookings.html
│           │   │   └── booking-history.html
│           │   └── login.html
│           └── static/
│               ├── css/
│               │   ├── styles.css
│               │   ├── calendar.css
│               │   └── admin.css
│               └── js/
│                   ├── calendar.js
│                   └── booking.js
```

---

## Step-by-Step Implementation

### 1. Maven Dependencies (pom.xml)
- **Add Dependencies:**
  - spring-boot-starter-web (REST and MVC)
  - spring-boot-starter-data-jpa (ORM)
  - spring-boot-starter-thymeleaf (Java-based templates)
  - spring-boot-starter-security (Authentication/Authorization)
  - mysql-connector-java (MySQL integration)
  - spring-boot-starter-validation (Bean validation)
  - jackson-datatype-jsr310 (LocalDateTime JSON serialization)

### 2. Database Configuration (application.properties)
- **MySQL Configuration:**
  - Database: `boardroom_booking`
  - JPA settings for entity management
  - Connection pooling configuration
- **Initial Data:**
  - Configure to load 20 boardrooms on startup

### 3. Enhanced Entity Layer

#### a. Boardroom.java
- **Fields:**
  - `id`, `name`, `location`, `capacity`, `description`, `amenities`
  - `capacityType` (enum: SMALL, MEDIUM, LARGE)
  - `isActive` (boolean for admin control)
- **Relationships:**
  - OneToMany with BoardroomAdmin
  - OneToMany with Booking
  - OneToMany with AvailabilitySlot

#### b. User.java
- **Fields:**
  - `id`, `username`, `password`, `email`, `fullName`, `department`
  - `role` (enum: USER, GLOBAL_ADMIN)
- **Security Integration:**
  - Implements UserDetails for Spring Security

#### c. BoardroomAdmin.java (New Entity)
- **Fields:**
  - `id`, `user` (ManyToOne), `boardroom` (ManyToOne)
  - `assignedDate`, `isActive`
- **Purpose:**
  - Links specific users as admins to specific boardrooms

#### d. Booking.java
- **Fields:**
  - `id`, `boardroom`, `user`, `bookingDate`, `startTime`, `endTime`
  - `purpose`, `attendeeCount`, `status` (enum)
  - `createdAt`, `updatedAt`

#### e. AvailabilitySlot.java (New Entity)
- **Fields:**
  - `id`, `boardroom`, `date`, `startTime`, `endTime`
  - `isAvailable`, `adminNotes`
- **Purpose:**
  - Admins can block/unblock specific time slots

### 4. Enums

#### a. RoomCapacityType.java
```java
public enum RoomCapacityType {
    SMALL(1, 6, "Small Meeting Room"),
    MEDIUM(7, 15, "Medium Conference Room"),
    LARGE(16, 50, "Large Boardroom");
}
```

#### b. BookingStatus.java
```java
public enum BookingStatus {
    PENDING, CONFIRMED, CANCELLED, COMPLETED
}
```

### 5. Enhanced Service Layer

#### a. BoardroomService.java
- **Methods:**
  - `getAllBoardrooms()`, `getBoardroomsByCapacity()`
  - `updateBoardroomDetails()` (admin only)
  - `getBoardroomAvailability(date)`

#### b. AdminService.java (New Service)
- **Methods:**
  - `assignAdminToBoardroom()`, `removeAdminFromBoardroom()`
  - `updateBoardroomDescription()`, `setBoardroomAvailability()`
  - `getBoardroomsForAdmin(userId)`

#### c. CalendarService.java (New Service)
- **Methods:**
  - `getCalendarEvents(boardroomId, month, year)`
  - `checkAvailability(boardroomId, date, startTime, endTime)`
  - `blockTimeSlot()`, `unblockTimeSlot()`

### 6. Enhanced Controller Layer

#### a. BoardroomController.java
- **Endpoints:**
  - `GET /boardrooms` – List all with capacity filter
  - `GET /boardrooms/capacity/{type}` – Filter by capacity
  - `GET /boardroom/{id}` – Detailed view with calendar
  - `GET /boardroom/{id}/availability` – AJAX endpoint for calendar

#### b. AdminController.java (New Controller)
- **Endpoints:**
  - `GET /admin/dashboard` – Admin dashboard
  - `GET /admin/boardroom/{id}/manage` – Manage specific boardroom
  - `POST /admin/boardroom/{id}/update` – Update boardroom details
  - `POST /admin/boardroom/{id}/availability` – Set availability

#### c. CalendarController.java (New Controller)
- **Endpoints:**
  - `GET /calendar/{boardroomId}` – Calendar view
  - `GET /api/calendar/{boardroomId}/events` – JSON events for calendar
  - `POST /api/calendar/{boardroomId}/block` – Block time slot

### 7. Data Initialization (DataInitializer.java)
- **20 Boardrooms Setup:**
  - Create 20 boardrooms with varied capacities
  - Assign capacity types automatically
  - Create sample admin assignments
- **Sample Data:**
  - Default admin user
  - Sample regular users
  - Initial availability slots

### 8. Enhanced Frontend Templates

#### a. index.html
- **Features:**
  - Modern landing page with boardroom statistics
  - Quick capacity-based filtering
  - Recent bookings overview

#### b. boardrooms.html
- **Features:**
  - Grid layout with capacity badges
  - Real-time availability indicators
  - Filter by capacity type
  - Search functionality

#### c. boardroom-detail.html (New Template)
- **Features:**
  - Detailed boardroom information
  - Embedded calendar view
  - Booking form integration
  - Admin edit controls (if admin)

#### d. calendar.html (New Template)
- **Features:**
  - Monthly calendar view
  - Color-coded availability
  - Click-to-book functionality
  - Admin controls for blocking slots

#### e. Admin Templates:
- **admin-dashboard.html:** Overview of managed boardrooms
- **manage-boardroom.html:** Edit boardroom details and description
- **manage-availability.html:** Set availability calendar

### 9. Enhanced Security Configuration
- **Role-Based Access:**
  - USER: Can view and book available boardrooms
  - BOARDROOM_ADMIN: Can manage assigned boardrooms
  - GLOBAL_ADMIN: Can manage all boardrooms and assign admins
- **Method-Level Security:**
  - Protect admin endpoints
  - Validate boardroom admin permissions

### 10. Frontend Styling & JavaScript

#### a. styles.css
- **Modern Corporate Design:**
  - Clean typography with Google Fonts
  - Capacity-based color coding
  - Responsive grid layouts
  - Professional color scheme

#### b. calendar.css
- **Calendar Styling:**
  - Modern calendar grid
  - Availability color indicators
  - Hover effects and transitions

#### c. calendar.js
- **Interactive Features:**
  - AJAX calendar loading
  - Click-to-book functionality
  - Real-time availability updates

### 11. API Endpoints Summary

#### Public Endpoints:
- `GET /` – Home page
- `GET /login` – Login page
- `GET /boardrooms` – List boardrooms
- `GET /boardroom/{id}` – Boardroom details

#### User Endpoints:
- `POST /booking` – Create booking
- `GET /my-bookings` – User's bookings
- `DELETE /booking/{id}` – Cancel booking

#### Admin Endpoints:
- `GET /admin/dashboard` – Admin dashboard
- `POST /admin/boardroom/{id}/update` – Update boardroom
- `POST /admin/availability/{id}` – Manage availability

#### AJAX Endpoints:
- `GET /api/boardroom/{id}/availability` – Get availability
- `POST /api/booking/check` – Check booking conflicts

---

## Implementation Order

1. **Setup & Dependencies:** pom.xml, application.properties
2. **Core Entities:** User, Boardroom, Booking, BoardroomAdmin, AvailabilitySlot
3. **Repositories:** All repository interfaces
4. **Enums & DTOs:** RoomCapacityType, BookingStatus, DTOs
5. **Services:** Core business logic implementation
6. **Security Configuration:** Authentication and authorization
7. **Data Initialization:** 20 boardrooms and sample data
8. **Controllers:** REST endpoints and page controllers
9. **Frontend Templates:** All HTML templates
10. **Styling & JavaScript:** CSS and interactive features
11. **Testing:** Unit and integration tests

---

## Key Features Summary

✅ **20 Pre-configured Boardrooms** with capacity classification
✅ **Boardroom-Specific Admin Roles** with management permissions
✅ **Calendar-Based Availability Management** by admins
✅ **Real-Time Booking System** for users
✅ **Capacity-Based Filtering** (Small/Medium/Large)
✅ **Admin Dashboard** for boardroom management
✅ **Modern Responsive UI** with clean corporate design
✅ **Role-Based Security** with proper access controls
✅ **MySQL Integration** with proper data relationships

After plan approval, I will create a detailed TODO.md tracker to monitor implementation progress.
