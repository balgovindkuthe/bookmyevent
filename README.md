# BookMyEvent - Event Management & Ticketing Platform (Backend)

Welcome to the backend repository of the **BookMyEvent** platform. This application provides robust REST APIs for creating events, managing ticket tiers, purchasing tickets, and scanning QR codes for entry check-in.

## 🚀 Technologies Used
* **Java 17** (Required for Spring Boot 3)
* **Spring Boot 3.2+** 
  * Spring Web (REST APIs)
  * Spring Data JPA (Hibernate)
  * Spring Security (JWT Authentication)
  * Spring Validation
* **MySQL 8+** (Relational Database)
* **Springdoc OpenAPI / Swagger UI** (API Documentation)
* **Lombok** (Boilerplate reduction)
* **JJWT** (JSON Web Token implementation)

## 📋 Prerequisites
Ensure you have the following installed on your local machine:
1. **Java Development Kit (JDK) 17 or higher**. *(Note: Java 8 is not supported by Spring Boot 3).*
2. **Maven 3.8+** (or use the provided IDE bundler).
3. **MySQL Server** running locally or remotely.

## 🛠️ Local Setup Instructions

1. **Clone the Repository** (or open the project folder in your IDE).
2. **Configure the Database**:
   Create a new MySQL database for the project (e.g., `bookmyevent_db`).
   Update the `src/main/resources/application.properties` file with your MySQL credentials:
   ```properties
   spring.datasource.url=jdbc:mysql://localhost:3306/bookmyevent_db?useSSL=false&serverTimezone=UTC
   spring.datasource.username=root
   spring.datasource.password=your_password
   ```
   *(Note: `spring.jpa.hibernate.ddl-auto=update` is set, so tables will be automatically generated upon startup).*

3. **Verify Java Version**:
   Ensure your IDE (IntelliJ, VSCode, Eclipse) and your system terminal are pointing to JDK 17.
   ```bash
   java -version
   ```

4. **Build the Project**:
   ```bash
   mvn clean install
   ```

5. **Run the Application**:
   Run the `BookMyEventApplication.java` main class from your IDE, or use Maven:
   ```bash
   mvn spring-boot:run
   ```

## 📖 API Documentation (Swagger UI)
Once the Spring Boot application is running (by default on port 8080), you can explore and test the REST APIs interactively via Swagger UI.

Navigate to: **[http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)**

You can use the `/api/v1/auth/register` and `/login` endpoints to generate a JWT token, and then click the **"Authorize"** button in Swagger UI to attach the Bearer token to all secured endpoints.

## 🔒 Security Summary
* **Authentication**: Stateless JSON Web Tokens (JWT).
* **Roles**: 
  * `CUSTOMER`: Can browse events and book tickets.
  * `ORGANIZER`: Can create events, manage tickets, scan QR codes, and view analytics.
  * `ADMIN`: Platform administrator (future expansion).
* Global exception handler normalizes all HTTP errors into a clean JSON standard.

## 🎫 Core Flows
1. **Organizer** creates an `Event` and defines `TicketTiers` (e.g., General Admission, VIP) with specific capacities.
2. **Customer** makes a `Booking`, which triggers a concurrency-safe database lock to decrement ticket inventory reliably, returning a generated `Ticket` object housing a secure UUID.
3. **Flutter App** renders the Ticket UUID as a QR Code.
4. **Organizer** uses the mobile app to scan the QR code via the `/api/v1/checkin/scan` endpoint, validating the UUID and preventing double-entry.


## Project Workflow
# BookMyEvent - Complete API Workflow Guide

This document outlines the step-by-step API flow for the two main actors in the BookMyEvent platform: the **Organizer** and the **Customer**.

We will interact with the following base URL: `http://localhost:8080`

---

## Part 1: The Organizer Flow
The Organizer sets up the platform, creates events, structures the pricing, and eventually scans tickets at the door.

### Step 1: Organizer Registration & Login
1. **Register as an Organizer:**
    * **Endpoint:** `POST /api/v1/auth/register`
    * **Body:**
      ```json
      {
        "name": "Jane Organizer",
        "email": "jane@events.com",
        "password": "securepassword123",
        "role": "ORGANIZER"
      }
      ```
    * **Result:** You receive a JWT Token in the response. **Save this token**, you will use it as the `Bearer` token for all subsequent Organizer requests.

### Step 2: Create a New Event
1. **Create an Event Container:**
    * **Endpoint:** `POST /api/v1/events`
    * **Auth:** Bearer Token (Jane's token)
    * **Body:**
      ```json
      {
        "title": "Summer Tech Conference 2026",
        "description": "The biggest tech conference of the year.",
        "location": "Convention Center",
        "eventDate": "2026-07-15T09:00:00",
        "capacity": 500
      }
      ```
    * **Result:** The event is created in `DRAFT` status. You will receive an `id` (e.g., `1`). Note this `eventId`.

### Step 3: Add Ticket Tiers (Pricing & Capacity)
An event is just an idea until it has tickets you can sell.
1. **Create General Pass Tier:**
    * **Endpoint:** `POST /api/v1/events/1/tiers`
    * **Auth:** Bearer Token
    * **Body:**
      ```json
      {
        "name": "General Pass",
        "price": 50.00,
        "capacity": 400
      }
      ```
    * **Result:** General Admission tier created. Note the tier `id` (e.g., `101`).
2. **Create VIP Tier:**
    * **Endpoint:** `POST /api/v1/events/1/tiers`
    * **Auth:** Bearer Token
    * **Body:**
      ```json
      {
        "name": "VIP Pass",
        "price": 150.00,
        "capacity": 100
      }
      ```
    * **Result:** VIP tier created. Note the tier `id` (e.g., `102`).

### Step 4: Publish the Event
Once t_tiers are added, the Organizer makes the event visible.
1. **Publish Event:**
    * **Endpoint:** `PATCH /api/v1/events/1/publish`
    * **Auth:** Bearer Token
    * **Body:** *(Empty)*
    * **Result:** The event status changes from `DRAFT` to `PUBLISHED`. Customers can now see it and buy tickets.

---

## Part 2: The Customer Flow
The Customer browses events, selects tickets, pays, and receives a digital ticket (QR code).

### Step 1: Customer Registration & Login
1. **Register as a Customer:**
    * **Endpoint:** `POST /api/v1/auth/register`
    * **Body:**
      ```json
      {
        "name": "John Doe",
        "email": "john@customer.com",
        "password": "customerpass",
        "role": "CUSTOMER"
      }
      ```
    * **Result:** You receive a new JWT Token. **Save this token**, it is now your Customer `Bearer` token.

### Step 2: Browse Events
1. **Get All Events:**
    * **Endpoint:** `GET /api/v1/events`
    * **Auth:** None required (Public access)
    * **Result:** John sees "Summer Tech Conference 2026" with `eventId: 1`.

### Step 3: Create a Booking
John decides to buy 2 VIP passes.
1. **Purchase Tickets:**
    * **Endpoint:** `POST /api/v1/bookings`
    * **Auth:** Bearer Token (John's token)
    * **Body:**
      ```json
      {
        "eventId": 1,
        "ticketTierId": 102,
        "quantity": 2
      }
      ```
    * **Result:** A Booking is created in `PENDING` status. The system safely deducts `2` tickets from the VIP capacity (Concurrency locked!). It returns a `bookingId` (e.g., `5001`).

### Step 4: Payment Simulation
*(In reality, the user would go to Stripe/Razorpay. Here, we mock the webhook callback from the payment provider.)*
1. **Mock Payment Webhook:**
    * **Endpoint:** `POST /api/v1/bookings/5001/payment-success`
    * **Auth:** Bearer Token (John's token / webhook secret)
    * **Body:**
      ```json
      {
        "paymentReference": "STRIPE_CH_987654321"
      }
      ```
    * **Result:** The booking becomes `CONFIRMED`. The system automatically generates 2 `Ticket` entities for John. Each ticket has a unique `qrCodeUuid` (e.g., `a1b2c3d4...`).

---

## Part 3: The Event Day (Check-In)
The day of the event arrives. John walks up to the gate with his mobile app displaying a QR code.

### Step 1: Scanning the Ticket
Jane the Organizer uses the scanner app, which reads John's `qrCodeUuid`.
1. **Validate QR Code:**
    * **Endpoint:** `POST /api/v1/checkin/scan`
    * **Auth:** Bearer Token (Jane's Organizer token)
    * **Body:**
      ```json
      {
        "qrCodeUuid": "a1b2c3d4-xxxx-yyyy-zzzz"
      }
      ```
    * **Result:**
        * The system looks up that UUID.
        * It checks if the ticket is `VALID`.
        * It marks the check-in time, changes the ticket status to `CHECKED_IN`, and returns a `Success: True` payload to the scanner app.

### Step 2: Preventing Double Entry
John's friend tries to screenshot the QR code and scan it again 10 minutes later.
1. **Validate QR Code again:**
    * **Endpoint:** `POST /api/v1/checkin/scan`
    * **Auth:** Bearer Token (Jane's Organizer token)
    * **Body:**
      ```json
      {
        "qrCodeUuid": "a1b2c3d4-xxxx-yyyy-zzzz"
      }
      ```
    * **Result:** The system throws a `TicketAlreadyUsedException`! The scanner app flashes red and denies entry.

---

## Part 4: Post-Event Analytics (Organizer)
Jane wants to see how much money she made and how many people turned up.
1. **View Analytics:**
    * **Endpoint:** `GET /api/v1/analytics/events/1`
    * **Auth:** Bearer Token (Jane's Organizer token)
    * **Result:**
      ```json
      {
        "eventId": 1,
        "eventTitle": "Summer Tech Conference 2026",
        "totalRevenue": 300.00,
        "totalCheckIns": 1,
        "ticketsSoldPerTier": {
          "VIP Pass": 2
        }
      }
      ```