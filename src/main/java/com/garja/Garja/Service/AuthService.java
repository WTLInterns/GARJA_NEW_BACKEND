package com.garja.Garja.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.garja.Garja.DTO.requests.LoginRequests;
import com.garja.Garja.DTO.requests.SignupRequests;
import com.garja.Garja.DTO.response.LoginResponse;
import com.garja.Garja.DTO.response.SignupResponse;
import com.garja.Garja.Exception.CustomException;
import com.garja.Garja.JWT.JwtUtils;
import com.garja.Garja.Model.AdminProfile;
import com.garja.Garja.Model.User;
import com.garja.Garja.Model.UserProfile;
import com.garja.Garja.Repo.AdminProfileRepo;
import com.garja.Garja.Repo.UserProfileRepo;
import com.garja.Garja.Repo.UserRepo;

@Service
public class AuthService {
    

    @Autowired
	private UserRepo userRepo;

	@Autowired
	private AdminProfileRepo adminProfileRepository;

	@Autowired
	private UserProfileRepo userProfileRepository;	
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	private JwtUtils jwtUtils;
	
	@Autowired
	private AuthenticationManager authenticationManager;


    public SignupResponse signUp(SignupRequests requests) {

        if (userRepo.existsByEmail(requests.getEmail())) {
            throw new CustomException("Email already exists!");
        }

        User user = new User();
        user.setFirstName(requests.getFirstName());
        user.setLastName(requests.getLastName());
        user.setEmail(requests.getEmail());
        user.setPassword(passwordEncoder.encode(requests.getPassword())); 
        user.setRole(requests.getRole());
        user.setPhoneNumber(requests.getPhoneNumber());

        User savedUser = userRepo.save(user);

        if ("ADMIN".equalsIgnoreCase(savedUser.getRole())) {
            AdminProfile adminProfile = new AdminProfile();
            // adminProfile.setId(savedUser.getId()); 
            adminProfile.setDepartment("IT");      
            adminProfile.setUser(savedUser);

            adminProfileRepository.save(adminProfile);
        } else if ("USER".equalsIgnoreCase(savedUser.getRole())) {
            UserProfile userProfile = new UserProfile();
            // userProfile.setId(savedUser.getId());
            userProfile.setPreferredLanguage("ENGLISH"); 
            userProfile.setUser(savedUser);

            userProfileRepository.save(userProfile);
        }

        SignupResponse response = new SignupResponse();
        response.setId(savedUser.getId());
        response.setEmail(savedUser.getEmail());
        response.setPassword(savedUser.getPassword());
        response.setFirstName(savedUser.getFirstName());
        response.setLastName(savedUser.getLastName());
        response.setPhoneNumber(savedUser.getPhoneNumber());
        response.setRole(savedUser.getRole());

        return response;
    }



    public LoginResponse login(LoginRequests loginRequest) {
		LoginResponse response = new LoginResponse();
		try {
			authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getEmail(),loginRequest.getPassword()));
			var user = this.userRepo.findByEmail(loginRequest.getEmail());
			var jwt = jwtUtils.generateToken(user);
			response.setToken(jwt);
            response.setFirstName(user.getFirstName());
            response.setId(user.getId());
            response.setLastName(user.getLastName());
			response.setRole(user.getRole());
			response.setEmail(user.getEmail());
			response.setPassword(user.getPassword());
		}
		
		catch(Exception e) {
		System.out.println(e.getMessage());
			
		}
		return response;
	}
}
