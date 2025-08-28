package com.example.booking;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories
public class BoardroomBookingApplication {

    public static void main(String[] args) {
        try {
            System.out.println("Starting Boardroom Booking Platform...");
            SpringApplication.run(BoardroomBookingApplication.class, args);
            System.out.println("Boardroom Booking Platform started successfully!");
            System.out.println("Access the application at: http://localhost:8000");
        } catch (Exception e) {
            System.err.println("Failed to start Boardroom Booking Platform: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
