package ziyad.demo.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ziyad.demo.dto.JwtResponse;
import ziyad.demo.dto.LoginRequest;
import ziyad.demo.dto.VoterRegistrationRequest;
import ziyad.demo.service.AuthService;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {
    
    @Autowired
    private AuthService authService;
    
    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        try {
            JwtResponse jwtResponse = authService.authenticateUser(loginRequest);
            return ResponseEntity.ok(jwtResponse);
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body("Error: Invalid credentials");
        }
    }
    
    @PostMapping("/register/voter")
    public ResponseEntity<?> registerVoter(@Valid @RequestBody VoterRegistrationRequest signUpRequest) {
        try {
            authService.registerVoter(signUpRequest);
            return ResponseEntity.ok("Voter registered successfully!");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest()
                .body("Error: " + e.getMessage());
        }
    }
}
