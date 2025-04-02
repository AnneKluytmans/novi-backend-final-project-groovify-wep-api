# Groovify - Web API for Vinylshops

You can find the github repository [here](https://github.com/AnneKluytmans/novi-backend-final-project-groovify-web-api)

## Table of contents

- [About The Project](#about-the-project)
- [Built With](#built-with)
- [Prerequisites](#prerequisites)
- [Installation Steps](#installation-steps)
- [Author](#author)

  <br>

<img src="src/main/resources/assets/groovifyLogo.png" alt="Groovify Logo" width="300"/>


## About The Project

**Groovify** is an innovative web API designed to support vinyl stores in efficiently managing their daily operations.
The API is built to optimize workflows for both customers and store employees. It enables customers to browse
collections, place orders and view their order details, while empowering employees to handle inventory and
process orders with ease.

### Features
- **Product Management**: Customers can explore the vinyl records collection, filtering by artist, genre, price, and more.
  Employees can efficiently manage stock levels, add, update, and remove vinyl records, and upload/download album covers 
- ensuring an accurate and up-to-date catalog.
- **Order Management**: Customers can easily place, update, and cancel orders. They can also view their order details along with 
- corresponding invoices. Employees can update order statuses and retrieve order and invoice details to ensure a smooth order process.
- **User Management**: Both customers and employees can create and manage accounts and update their personal details as needed.
  Employees have access to customer information when necessary for support or order processing.
- **Authentication and Authorization**: Role-based access control ensures that users can only perform actions within their permissions, 
- protecting sensitive data and maintaining system security.


## Built with
- Programming Language: Java 21
- Framework: Spring Boot 3.4.1
- Database: PostgreSQL
- API Documentation: Swagger and Postman
- Tools: Maven, IntelliJ IDEA

## Prerequisites
Before running the Spring Boot Web API, ensure you have the following installed:

- Java Development Kit (JDK): Corretto 21 ([Download](https://aws.amazon.com/corretto/))
- Maven: Version 4.0.0 ([Download](https://maven.apache.org/download.cgi))
- PostgreSQL: Latest version ([Download](https://www.postgresql.org/download/))
- IntelliJ IDEA or any other IDE supporting Java and Spring Boot ([Download](https://www.jetbrains.com/idea/download/))
- Postman: For API testing ([Download](https://www.postman.com/downloads/))

## Installation Steps
Follow these steps to set up and run the Spring Boot Web API.

1. Clone the [Groovify repository](https://github.com/AnneKluytmans/novi-backend-final-project-groovify-web-api)
    ```
    git@github.com:AnneKluytmans/novi-backend-final-project-groovify-web-api.git
    ```
    or
    ```
    git clone https://github.com/AnneKluytmans/novi-backend-final-project-groovify-web-api.git
    ```
2. Set Up Environment Variables
   Create a `.env` file in the root directory of the project and add the following:
    ```dotenv
    DB_URL=your_database_url
    DB_USERNAME=your_username
    DB_PASSWORD=your_password
    ```
   Alternatively, you can copy the provided `.env.example`file:
    ```
    cp .env.example .env
    ```
3.  Configure PostgreSQL Database
    - Create a PostgreSQL database
    - Fill the `.env` file with your database credentials to configure the database connection
4. Build the Project
   Use Maven to build the project:
    ```
    mvn clean install
    ```
5. Run the Application
   Start the Spring Boot application with the following command:
    ```
    mvn spring-boot:run
    ```
6. API Documentation and Testing
   - Swagger UI: Visit http://localhost:8080/swagger-ui.html to explore API endpoints.
   - Postman: Import the provided Postman collection for testing API requests.

## Author
This project was developed by [Anne Kluytmans](https://github.com/AnneKluytmans), a Software Development student
at [NOVI](https://www.novi.nl/).