package ziyad.demo.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ziyad.demo.dto.CandidateRegistrationRequest;
import ziyad.demo.entity.Candidate;
import ziyad.demo.entity.Election;
import ziyad.demo.entity.Voter;
import ziyad.demo.service.AdminService;
import ziyad.demo.service.VotingService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
@CrossOrigin(origins = "*")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {
    
    @Autowired
    private AdminService adminService;
    
    @Autowired
    private VotingService votingService;
    
    @PostMapping("/candidates")
    public ResponseEntity<?> registerCandidate(@Valid @RequestBody CandidateRegistrationRequest request) {
        try {
            Candidate candidate = adminService.registerCandidate(request);
            return ResponseEntity.ok(candidate);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }
    
    @GetMapping("/candidates")
    public ResponseEntity<List<Candidate>> getAllCandidates() {
        List<Candidate> candidates = adminService.getAllCandidates();
        return ResponseEntity.ok(candidates);
    }
    
    @PostMapping("/assign-voters")
    public ResponseEntity<?> assignVotersByCity(@RequestBody Map<String, String> request) {
        try {
            String city = request.get("city");
            int assignedCount = adminService.assignVotersByCity(city);
            return ResponseEntity.ok("Assigned " + assignedCount + " voters in " + city);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }
    
    @GetMapping("/voters")
    public ResponseEntity<List<Voter>> getAllVoters() {
        List<Voter> voters = adminService.getAllVoters();
        return ResponseEntity.ok(voters);
    }
    
    @GetMapping("/voters/unassigned")
    public ResponseEntity<List<Voter>> getUnassignedVoters(@RequestParam String city) {
        List<Voter> voters = adminService.getUnassignedVotersByCity(city);
        return ResponseEntity.ok(voters);
    }
    
    @PostMapping("/elections")
    public ResponseEntity<?> createElection(@RequestBody Map<String, Object> request) {
        try {
            String title = (String) request.get("title");
            String description = (String) request.get("description");
            LocalDateTime startTime = LocalDateTime.parse((String) request.get("startTime"));
            LocalDateTime endTime = LocalDateTime.parse((String) request.get("endTime"));
            
            Election election = adminService.createElection(title, description, startTime, endTime);
            return ResponseEntity.ok(election);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }
    
    @GetMapping("/elections")
    public ResponseEntity<List<Election>> getAllElections() {
        List<Election> elections = adminService.getAllElections();
        return ResponseEntity.ok(elections);
    }
    
    @GetMapping("/results/{electionId}")
    public ResponseEntity<List<Object[]>> getElectionResults(@PathVariable Long electionId) {
        List<Object[]> results = votingService.getElectionResults(electionId);
        return ResponseEntity.ok(results);
    }
}
