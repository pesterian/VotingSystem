package ziyad.demo.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import ziyad.demo.entity.Election;
import ziyad.demo.service.AdminService;
import ziyad.demo.service.AuthService;

import java.time.LocalDateTime;

@Component
public class DataLoader implements ApplicationRunner {
    
    @Autowired
    private AuthService authService;
    
    @Autowired
    private AdminService adminService;
    
    @Override
    public void run(ApplicationArguments args) throws Exception {
        // Create a default admin
        try {
            authService.createAdmin("admin@voting.com", "admin123", "System Administrator");
        } catch (RuntimeException e) {
            // Admin already exists
        }
        
        // Create a sample election
        try {
            Election election = adminService.createElection(
                "2024 Student Council Election",
                "Annual student council election for the university",
                LocalDateTime.now().plusDays(1), // Start tomorrow
                LocalDateTime.now().plusDays(7)  // End in a week
            );
            System.out.println("Sample election created with ID: " + election.getId());
        } catch (Exception e) {
            // Election might already exist
        }
    }
}
