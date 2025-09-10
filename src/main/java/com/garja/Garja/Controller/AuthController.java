package com.garja.Garja.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.garja.Garja.DTO.requests.LoginRequests;
import com.garja.Garja.DTO.requests.SignupRequests;
import com.garja.Garja.DTO.response.LoginResponse;
import com.garja.Garja.DTO.response.SignupResponse;
import com.garja.Garja.Exception.CustomException;
import com.garja.Garja.Service.AuthService;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthService authService;



    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody SignupRequests requests) {
        try {
            SignupResponse response = authService.signUp(requests);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (CustomException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Something went wrong");
        }
    }


    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequests loginRequest) {
        LoginResponse response = authService.login(loginRequest);
        
        if (response.getToken() == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        return ResponseEntity.ok(response);
    }
    
}
