package com.garja.Garja.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.garja.Garja.DTO.requests.PasswordRequests;
import com.garja.Garja.DTO.response.PasswordResponse;
import com.garja.Garja.Model.User;
import com.garja.Garja.Repo.UserRepo;

@Service
public class PasswordResetService {

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;
    


    public PasswordResponse resetPassword(String email, PasswordRequests request) {
        User user = userRepo.findByEmail(email);
        if (user == null) {
            throw new RuntimeException("User not found with email: " + email);
        }
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepo.save(user);

        return new PasswordResponse(email, "Password reset successfully!");
    }
}
