
Built by https://www.blackbox.ai

---

# Boardroom Booking Platform

## Project Overview

The **Boardroom Booking Platform** is a corporate web application designed to manage boardroom bookings and availability effectively. Built on a Spring Boot backend with a Thymeleaf frontend and MySQL for data persistence, this platform provides user authentication, boardroom management, booking workflows, and an intuitive user interface, all tailored to meet the needs of corporate institutions.

## Installation

To set up the project locally, follow these steps:

1. **Clone the Repository:**
   ```bash
   git clone https://github.com/username/boardroom-booking-platform.git
   cd boardroom-booking-platform
   ```

2. **Configure MySQL:**
   - Create a MySQL database called `boardroom_booking`.
   - Update `src/main/resources/application.properties` with your MySQL credentials:
     ```properties
     spring.datasource.url=jdbc:mysql://localhost:3306/boardroom_booking
     spring.datasource.username=YOUR_USERNAME
     spring.datasource.password=YOUR_PASSWORD
     spring.jpa.hibernate.ddl-auto=update
     ```

3. **Add Dependencies:**
   By running:
   ```bash
   mvn clean install
   ```
   This will fetch all project dependencies defined in `pom.xml`.

4. **Run the Application:**
   Use the following command to start the Spring Boot application:
   ```bash
   mvn spring-boot:run
   ```

## Usage

Once the application is up and running, navigate to `http://localhost:8080` in your web browser. You can log in using your credentials or register as a new user. The main features include:

- Browse available boardrooms
- Book your chosen boardroom
- View booking history and manage personal bookings
- Admin functions for managing boardroom availability and details

## Features

- **User Management:** Supports user authentication and role-based access (User, Boardroom Admin, Global Admin).
- **Boardroom Management:** Admins can manage boardroom details, bookings, and availability.
- **Real-Time Availability View:** Users can see real-time availability for boardrooms.
- **Capacity Classification:** Boardrooms classified by size (Small, Medium, Large).
- **Dynamic Booking System:** Allows users to make and manage bookings effectively.
- **Elegant UI:** Modern user interface built with Thymeleaf and styled with custom CSS.

## Dependencies

The project relies on the following dependencies (as defined in `pom.xml`):

- `spring-boot-starter-web`
- `spring-boot-starter-data-jpa`
- `spring-boot-starter-thymeleaf`
- `spring-boot-starter-security`
- `mysql-connector-java`
- `spring-boot-starter-validation`
- `jackson-datatype-jsr310`

## Project Structure

The project structure is organized as follows:

```
BoardroomBooking/
├── pom.xml
├── src/
│   └── main/
│       ├── java/com/example/booking/
│       │   ├── BoardroomBookingApplication.java
│       │   ├── controller/
│       │   ├── entity/
│       │   ├── repository/
│       │   ├── service/
│       │   ├── dto/
│       │   ├── enums/
│       │   ├── exception/
│       │   └── config/
│       └── resources/
│           ├── application.properties
│           ├── data.sql
│           ├── templates/
│           └── static/
```

### Folder Breakdown:

- **controller/**: Contains REST controller classes handling HTTP requests.
- **entity/**: Holds the JPA entity classes representing the database tables.
- **repository/**: Spring Data JPA repository interfaces for CRUD operations.
- **service/**: Contains service classes with business logic.
- **dto/**: Data Transfer Objects used for communication between layers.
- **enums/**: Holds enums for capacity types and booking statuses.
- **exception/**: Global exception handling classes.
- **config/**: Configuration classes, including security settings and data initialization.

## Conclusion

The Boardroom Booking Platform serves as a comprehensive solution for managing boardroom bookings in a corporate setting, focusing on user experience and efficient management tools for administrators. For any contributions or improvements, please feel free to submit a pull request or open an issue in the repository.