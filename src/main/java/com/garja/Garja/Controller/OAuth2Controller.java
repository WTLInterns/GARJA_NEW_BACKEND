package com.garja.Garja.Controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "http://localhost:3000")
public class OAuth2Controller {

    @GetMapping("/google")
    public void googleLogin(HttpServletResponse response) throws Exception {
        // This will redirect to Google OAuth2 authorization endpoint
        response.sendRedirect("/oauth2/authorization/google");
    }
    
    @GetMapping("/oauth2/success")
    public ResponseEntity<String> oauth2Success() {
        return ResponseEntity.ok("OAuth2 login successful");
    }
}
