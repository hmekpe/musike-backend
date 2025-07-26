package com.musike.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class OAuth2Controller {

    @Autowired
    private ClientRegistrationRepository clientRegistrationRepository;

    @GetMapping("/oauth2/registrations")
    public Map<String, String> getOAuth2Registrations() {
        Map<String, String> registrations = new HashMap<>();
        
        // Get all available OAuth2 registrations
        Iterable<ClientRegistration> clientRegistrations = (Iterable<ClientRegistration>) clientRegistrationRepository;
        
        for (ClientRegistration registration : clientRegistrations) {
            String registrationId = registration.getRegistrationId();
            String clientId = registration.getClientId();
            registrations.put(registrationId, clientId);
        }
        
        return registrations;
    }

    @GetMapping("/oauth2/registration/{registrationId}")
    public Map<String, Object> getOAuth2RegistrationDetails(@PathVariable String registrationId) {
        ClientRegistration registration = clientRegistrationRepository.findByRegistrationId(registrationId);
        
        if (registration != null) {
            Map<String, Object> details = new HashMap<>();
            details.put("registrationId", registration.getRegistrationId());
            details.put("clientId", registration.getClientId());
            details.put("clientName", registration.getClientName());
            details.put("authorizationUri", registration.getProviderDetails().getAuthorizationUri());
            details.put("tokenUri", registration.getProviderDetails().getTokenUri());
            details.put("userInfoUri", registration.getProviderDetails().getUserInfoEndpoint().getUri());
            details.put("redirectUri", registration.getRedirectUri());
            return details;
        }
        
        return Map.of("error", "Registration not found: " + registrationId);
    }
} 