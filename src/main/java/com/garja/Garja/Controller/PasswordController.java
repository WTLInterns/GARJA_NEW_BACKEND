package com.garja.Garja.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.garja.Garja.DTO.requests.PasswordRequests;
import com.garja.Garja.DTO.requests.SignupRequests;
import com.garja.Garja.DTO.response.PasswordResponse;
import com.garja.Garja.DTO.response.SignupResponse;
import com.garja.Garja.Service.PasswordResetService;
import com.garja.Garja.Service.UserDetailsServiceImpl;


@RestController
@RequestMapping("/common")
public class PasswordController {

    @Autowired
    private PasswordResetService passwordResetService;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;
    

    @PostMapping("/reset-password")
    public ResponseEntity<PasswordResponse> resetPassword(
            @RequestBody PasswordRequests request) {
Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    		String email = authentication.getName();        PasswordResponse response = passwordResetService.resetPassword(email, request);
        return ResponseEntity.ok(response);
    }


    @GetMapping("/showProfile")
    public SignupResponse showProfile() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    		String email = authentication.getName(); 
        return this.userDetailsService.showProfile(email);
    }

    @PutMapping("/updatePassword")
    public SignupResponse updatePassword(
            @RequestBody SignupRequests request) {
Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    		String email = authentication.getName(); 
            return this.userDetailsService.updatePassword(email, request);

                
            }
        
       



}
