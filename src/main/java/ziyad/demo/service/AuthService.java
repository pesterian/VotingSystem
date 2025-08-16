package ziyad.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ziyad.demo.dto.JwtResponse;
import ziyad.demo.dto.LoginRequest;
import ziyad.demo.dto.VoterRegistrationRequest;
import ziyad.demo.entity.Admin;
import ziyad.demo.entity.Role;
import ziyad.demo.entity.User;
import ziyad.demo.entity.Voter;
import ziyad.demo.repository.UserRepository;
import ziyad.demo.security.JwtUtils;
import ziyad.demo.security.UserPrincipal;

@Service
public class AuthService {
    
    @Autowired
    private AuthenticationManager authenticationManager;
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Autowired
    private JwtUtils jwtUtils;
    
    public JwtResponse authenticateUser(LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword())
        );
        
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);
        
        UserPrincipal userDetails = (UserPrincipal) authentication.getPrincipal();
        
        return new JwtResponse(jwt, userDetails.getId(), userDetails.getEmail(), 
                              userDetails.getName(), userDetails.getRole());
    }
    
    public void registerVoter(VoterRegistrationRequest signUpRequest) {
        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            throw new RuntimeException("Error: Email is already taken!");
        }
        
        Voter voter = new Voter(signUpRequest.getEmail(), 
                               passwordEncoder.encode(signUpRequest.getPassword()),
                               signUpRequest.getName(),
                               signUpRequest.getCity());
        
        userRepository.save(voter);
    }
    
    public void createAdmin(String email, String password, String name) {
        if (userRepository.existsByEmail(email)) {
            throw new RuntimeException("Error: Email is already taken!");
        }
        
        Admin admin = new Admin(email, passwordEncoder.encode(password), name);
        userRepository.save(admin);
    }
}
