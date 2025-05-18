# Spring Boot Basic Authentication Example

This project demonstrates the implementation of Basic Authentication in a Spring Boot application. Basic Authentication is a simple authentication scheme built into the HTTP protocol where credentials are sent as base64 encoded string in the format `username:password`.

## Features

- Spring Boot Security Configuration
- Basic Authentication Implementation
- User Details Service
- Protected REST Endpoints for Product Management
- In-memory User Management

## Prerequisites

- Java 17 or higher
- Maven 3.6.x or higher
- Vscode

## Project Setup

1. Clone the repository:
```bash
git clone <repository-url>
cd basicauth
```

2. Build the project:
```bash
mvn clean install
```

3. Run the application:
```bash
mvn spring-boot:run
```

## Security Configuration

The project uses Spring Security for implementing basic authentication. Here's a basic overview of the security configuration:

```java
@Configuration
@EnableWebSecurity
public class SecurityConfig {
    
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests((auth) -> auth
                .requestMatchers("/api/products/**").authenticated()
                .anyRequest().authenticated()
            )
            .httpBasic();
        return http.build();
    }
}
```

## API Endpoints

The application exposes several protected endpoints that require authentication:

### Product Management Endpoints
- `GET /api/products` - Get all products
- `GET /api/products/{id}` - Get a specific product
- `POST /api/products` - Create a new product
- `PUT /api/products/{id}` - Update a product
- `DELETE /api/products/{id}` - Delete a product

## How to Test

1. Using cURL:
```bash
# Get all products
curl -X GET http://localhost:8080/api/products -H "Authorization: Basic $(echo -n 'username:password' | base64)"

# Create a new product
curl -X POST http://localhost:8080/api/products \
  -H "Authorization: Basic $(echo -n 'username:password' | base64)" \
  -H "Content-Type: application/json" \
  -d '{"name": "Product Name", "price": 29.99, "description": "Product Description"}'

# Get a specific product
curl -X GET http://localhost:8080/api/products/1 -H "Authorization: Basic $(echo -n 'username:password' | base64)"
```

2. Using Postman:
   - Create a new request collection for Products API
   - Select "Basic Auth" under Authorization
   - Enter your username and password
   - Test the following endpoints:
     * GET http://localhost:8080/api/products
     * POST http://localhost:8080/api/products
     * GET http://localhost:8080/api/products/{id}

## Security Considerations

1. Basic Authentication sends credentials encoded (not encrypted)
2. Always use HTTPS in production
3. Consider using more secure authentication methods like JWT for production applications
4. Implement proper authorization for different user roles (e.g., ADMIN vs USER)

## Dependencies

```xml
<dependencies>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-security</artifactId>
    </dependency>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-web</artifactId>
    </dependency>
</dependencies>
```

## Contributing

Feel free to submit issues and enhancement requests.

## License

This project is licensed under the MIT License - see the LICENSE file for details.
