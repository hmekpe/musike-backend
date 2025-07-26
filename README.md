# Musike Backend

A Spring Boot backend application for the Musike music streaming platform.

## Features

- JWT-based authentication
- OAuth2 authentication with Google and GitHub
- PostgreSQL database integration
- RESTful API endpoints
- CORS configuration for frontend integration

## OAuth2 Providers

The application currently supports:
- **Google OAuth2** - For Google account authentication
- **GitHub OAuth2** - For GitHub account authentication

For detailed setup instructions, see [GITHUB_OAUTH_SETUP.md](./GITHUB_OAUTH_SETUP.md)

## Quick Start

1. Configure your database in `application.properties`
2. Set up OAuth2 providers (see setup guide)
3. Run the application: `./mvnw spring-boot:run`
4. Access the API at `http://localhost:8080`

## API Endpoints

- `POST /login` - User authentication
- `POST /users` - User registration
- `GET /oauth2/**` - OAuth2 authentication endpoints 
