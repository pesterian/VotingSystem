package ziyad.demo.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import ziyad.demo.dto.VoteRequest;
import ziyad.demo.entity.Candidate;
import ziyad.demo.entity.Vote;
import ziyad.demo.exception.DuplicateVoteException;
import ziyad.demo.exception.UnauthorizedVoterException;
import ziyad.demo.exception.VotingClosedException;
import ziyad.demo.security.UserPrincipal;
import ziyad.demo.service.VotingService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/voter")
@CrossOrigin(origins = "*")
@PreAuthorize("hasRole('VOTER')")
public class VoterController {
    
    @Autowired
    private VotingService votingService;
    
    @GetMapping("/candidates/{electionId}")
    public ResponseEntity<List<Candidate>> getCandidatesForElection(@PathVariable Long electionId) {
        List<Candidate> candidates = votingService.getCandidatesForElection(electionId);
        return ResponseEntity.ok(candidates);
    }
    
    @PostMapping("/vote")
    public ResponseEntity<?> castVote(@Valid @RequestBody VoteRequest voteRequest, 
                                     Authentication authentication) {
        try {
            UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
            Vote vote = votingService.castVote(userPrincipal.getId(), voteRequest);
            return ResponseEntity.ok("Vote cast successfully!");
        } catch (VotingClosedException | DuplicateVoteException | UnauthorizedVoterException e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }
    
    @GetMapping("/my-vote/{electionId}")
    public ResponseEntity<?> getMyVote(@PathVariable Long electionId, Authentication authentication) {
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        Optional<Vote> vote = votingService.getVoterVoteForElection(userPrincipal.getId(), electionId);
        
        if (vote.isPresent()) {
            return ResponseEntity.ok(vote.get());
        } else {
            return ResponseEntity.ok("No vote cast yet");
        }
    }
}
