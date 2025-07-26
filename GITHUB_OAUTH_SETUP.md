# GitHub OAuth Setup Guide

This guide explains how to set up GitHub OAuth authentication for the Musike application.

## Prerequisites

1. A GitHub account
2. Access to GitHub Developer Settings

## Step 1: Create GitHub OAuth App

1. Go to [GitHub Developer Settings](https://github.com/settings/developers)
2. Click "New OAuth App"
3. Fill in the application details:
   - **Application name**: Musike
   - **Homepage URL**: `http://localhost:8080` (for development)
   - **Authorization callback URL**: `http://localhost:8080/login/oauth2/code/github`
4. Click "Register application"
5. Note down the **Client ID** and **Client Secret**

## Step 2: Update Application Properties

Update `src/main/resources/application.properties` with your GitHub OAuth credentials:

```properties
# GitHub OAuth Configuration
spring.security.oauth2.client.registration.github.client-id=YOUR_GITHUB_CLIENT_ID
spring.security.oauth2.client.registration.github.client-secret=YOUR_GITHUB_CLIENT_SECRET
spring.security.oauth2.client.registration.github.scope=read:user,user:email
```

Replace `YOUR_GITHUB_CLIENT_ID` and `YOUR_GITHUB_CLIENT_SECRET` with the values from Step 1.

## Step 3: Environment Variables (Recommended for Production)

For production, use environment variables instead of hardcoding credentials:

```bash
export GITHUB_CLIENT_ID=your_github_client_id
export GITHUB_CLIENT_SECRET=your_github_client_secret
```

Then update `application.properties`:

```properties
spring.security.oauth2.client.registration.github.client-id=${GITHUB_CLIENT_ID}
spring.security.oauth2.client.registration.github.client-secret=${GITHUB_CLIENT_SECRET}
```

## Step 4: Frontend Integration

The frontend has been updated to include GitHub login buttons. The OAuth flow works as follows:

1. User clicks GitHub login button
2. User is redirected to GitHub for authorization
3. After authorization, GitHub redirects back to the backend
4. Backend generates JWT token and redirects to frontend
5. Frontend receives token and stores it for authentication

## Step 5: Testing

1. Start the backend application
2. Start the frontend application
3. Navigate to the login/register screen
4. Click the GitHub button
5. Complete the GitHub authorization flow
6. Verify you're redirected back with a JWT token

## Security Considerations

1. **Never commit OAuth secrets to version control**
2. Use environment variables in production
3. Implement proper CORS configuration for production
4. Consider implementing CSRF protection
5. Use HTTPS in production

## Troubleshooting

### Common Issues

1. **Invalid redirect URI**: Ensure the callback URL in GitHub matches exactly
2. **CORS errors**: Check CORS configuration in `WebSecurityConfig.java`
3. **JWT token issues**: Verify JWT secret is properly configured

### Debug Logging

Enable debug logging for OAuth2 by adding to `application.properties`:

```properties
logging.level.org.springframework.security.oauth2=DEBUG
```

## Production Deployment

For production deployment:

1. Update GitHub OAuth app settings:
   - Homepage URL: Your production domain
   - Authorization callback URL: `https://yourdomain.com/login/oauth2/code/github`

2. Update CORS configuration in `WebSecurityConfig.java`

3. Set environment variables for OAuth credentials

4. Use HTTPS for all OAuth endpoints

## Files Modified

- `src/main/resources/application.properties` - OAuth configuration
- `src/main/java/com/musike/service/CustomOAuth2UserService.java` - OAuth user service
- `src/main/java/com/musike/service/OAuth2SuccessHandler.java` - OAuth success handler
- Frontend screens (LoginScreen.js, RegisterScreen.js) - UI updates
- Added GitHub icon to assets

## Next Steps

1. Implement user creation/update logic in `CustomOAuth2UserService`
2. Add proper error handling for OAuth failures
3. Implement user profile management
4. Add logout functionality
5. Consider implementing refresh tokens 