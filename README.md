# Cab Booking Application - Microservices Project

A scalable and secure Cab Booking Application built using Spring Boot Microservices architecture. This system allows users to sign up, book rides, and receive notifications via SMS and email.

## Features

- **Customer and Driver Registration/Login**
- **Ride Booking, Completion, and Cancellation**
- **OTP Verification for Ride Start**
- **Fare Calculation using OpenRouteService API**
- **Real-time Notifications (SMS & Email)**
- **Kafka Integration for Event-Driven Communication**
- **JWT-based Authentication**
- **Modular Microservices Architecture**

## Microservices

1. **Customer Service** – Handles user sign-up/login, ride history, and ride status.
2. **Driver Service** – Registers drivers, manages driver status (AVAILABLE/BUSY).
3. **Ride Service** – Core ride booking logic, OTP generation, distance & fare calculation.
4. **Notification Service** – Sends SMS (Twilio) and emails (JavaMailSender) based on ride events.

## Tech Stack

- **Java**, **Spring Boot**, **Spring Security**, **Spring Data JPA**
- **PostgreSQL**, **Kafka**
- **OpenRouteService API**
- **Twilio SMS API**, **JavaMailSender**
- **JWT for secure authentication**

## Event Flow (Kafka Topics)

- **ride-events-topic**  
  Triggers notifications for:
  - Ride Booked
  - Ride Completed
  - Ride Cancelled

## APIs Overview

### Customer
- `POST /customer/signup`
- `POST /customer/login`

### Driver
- `POST /driver/register`

### Ride
- `POST /ride/book`
- `PUT /ride/complete`
- `PUT /ride/cancel`

## Prerequisites

- Java 17+
- Kafka & Zookeeper running
- **PostgreSQL Database**
- Twilio credentials
- OpenRouteService API key

## How to Run

1. Clone the repository
2. Start Kafka & Zookeeper
3. Start PostgreSQL server and configure DB credentials in each service's `application.properties`
4. Start each microservice individually
5. Hit the endpoints using Postman or Swagger
6. Book a ride and observe SMS/Email notifications

## License

This project is open-source and available under the [MIT License](LICENSE).
