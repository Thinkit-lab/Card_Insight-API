# Card Verification Microservice

## Overview

The Card Verification Microservice is a Spring Boot application that provides card details verification and statistics retrieval. It interacts with a 3rd party API (binlist.net) to fetch information about card numbers.

## Features

- **Card Verification:**
  - Endpoint: `GET [/api/v1/card-scheme/verify/{bin}]
  - Response:
    ```json
    {
    "status": "Success",
    "code": "MwI00",
    "message": "Request completed successfully",
    "data": {
        "success": true,
        "payload": {
            "scheme": "visa",
            "type": "debit",
            "bank": "Jyske Bank"
        }
    }
}
    ```

- **Card Statistics:**
  - Endpoint: `GET /api/v1/card-scheme/stats?start={start}&limit={limit}`
  - Response:
    ```json
    {
    "status": "Success",
    "code": "MwI00",
    "message": "Request completed successfully",
    "data": {
        "success": true,
        "start": 1,
        "limit": 10,
        "size": null,
        "payload": [
            {
                "45717360": "2"
            }
        ]
    }
}
    ```

- **User Authentication:**
  - Signup and login features for customer authentication.

## Technologies Used

- Spring Boot
- Spring Web
- Spring Data JPA
- WebClient
- Spring Security with Jwt
- H2 Database (for simplicity, can be replaced with other databases in production)
- JUnit 5 for testing

## Swagger

- http://localhost:8080/swagger-ui/index.html#/

## Configuration

- Configure application properties in `src/main/resources/application.yml`.

## Database

- The application uses an H2 in-memory database for development. Configure a production database in a production profile.

## Security

- The endpoints are protected. Users need to sign up and log in before accessing the services.

## Unit Tests

- Unit tests are available in the `src/test` directory.

## License

- This project is licensed under the MIT License - see the [LICENSE.md](LICENSE.md) file for details.

## Acknowledgments

- Thanks to binlist.net for providing the card information API.
