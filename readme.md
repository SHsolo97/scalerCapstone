# E-commerce Microservices Project

This is a sample e-commerce application built with a microservices architecture using Spring Boot.

## Project Overview

The project is divided into several microservices, each responsible for a specific business capability. This architecture allows for independent development, deployment, and scaling of each service.

## Services

The following microservices are included in this project:

*   **cartService**: Manages the shopping cart functionality.
*   **emailService**: Handles sending emails for order confirmations, etc.
*   **orderService**: Manages customer orders.
*   **paymentService**: Processes payments for orders.
*   **productService**: Manages the product catalog.
*   **userService**: Manages user accounts and authentication.

## Prerequisites

Before you begin, ensure you have the following installed:

*   Java Development Kit (JDK) 17 or later
*   Maven
*   Docker (for running dependency services)

## Dependencies

This project requires the following services to be running:

*   **MySQL**: Used by `orderService`, `productService`, and `userService`.
*   **MongoDB**: Used by `cartService`.
*   **Redis**: Used by `cartService` for caching.
*   **Kafka**: Used by `cartService`, `emailService`, `orderService`, and `userService` for asynchronous communication.

You can use Docker to easily set up these services. A `docker-compose.yml` file is recommended for managing these services in a development environment.

## Deployment Instructions

### Environment Variables

Before running the services, you need to configure the necessary environment variables for each microservice.


### Local Deployment

To run the services locally for development and testing, you can use the Maven wrapper included in each service's directory.

For each service, navigate to its root directory in a separate terminal and run the following command:

```bash
./mvnw spring-boot:run
```

For example, to run the `productService`:

```bash
cd productService
./mvnw spring-boot:run
```

Repeat this process for each of the microservices you want to run.

### Production Deployment on AWS

For a production environment on AWS, you should package each microservice as a JAR file and then deploy them. A common approach is to use AWS Elastic Beanstalk for easy deployment and management.

1.  **Build the JAR file for each service:**

    For each microservice, navigate to its root directory and run the following command to build the executable JAR file:

    ```bash
    ./mvnw clean package
    ```

    This will create a `.jar` file in the `target` directory of each service (e.g., `productService/target/productService-0.0.1-SNAPSHOT.jar`).

2.  **Deploy to AWS Elastic Beanstalk:**

    *   Create a new Elastic Beanstalk application.
    *   For each microservice, create a new environment (e.g., `product-service-env`).
    *   Choose the "Java" platform.
    *   Upload the generated JAR file for the corresponding service.
    *   Configure the environment variables, database connections, and other settings as needed in the Elastic Beanstalk console.
    *   Launch the environment.

    Repeat this process for each microservice. You will also need to configure a service discovery mechanism (like Eureka or AWS Cloud Map) and an API Gateway (like Amazon API Gateway) to route requests to the appropriate service.