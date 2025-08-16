package ziyad.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ziyad.demo.dto.VoteRequest;
import ziyad.demo.entity.Candidate;
import ziyad.demo.entity.Election;
import ziyad.demo.entity.Vote;
import ziyad.demo.entity.Voter;
import ziyad.demo.exception.DuplicateVoteException;
import ziyad.demo.exception.UnauthorizedVoterException;
import ziyad.demo.exception.VotingClosedException;
import ziyad.demo.repository.CandidateRepository;
import ziyad.demo.repository.ElectionRepository;
import ziyad.demo.repository.VoteRepository;
import ziyad.demo.repository.VoterRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class VotingService {
    
    @Autowired
    private VoteRepository voteRepository;
    
    @Autowired
    private VoterRepository voterRepository;
    
    @Autowired
    private CandidateRepository candidateRepository;
    
    @Autowired
    private ElectionRepository electionRepository;
    
    @Transactional
    public Vote castVote(Long voterId, VoteRequest voteRequest) {
        // Check if voter exists and is assigned
        Voter voter = voterRepository.findById(voterId)
            .orElseThrow(() -> new RuntimeException("Voter not found"));
        
        if (!voter.isAssigned()) {
            throw new UnauthorizedVoterException("Voter is not assigned to vote");
        }
        
        // Check if election exists
        Election election = electionRepository.findById(voteRequest.getElectionId())
            .orElseThrow(() -> new RuntimeException("Election not found"));
        
        // Check if voting is within allowed time window
        LocalDateTime now = LocalDateTime.now();
        if (now.isBefore(election.getStartTime()) || now.isAfter(election.getEndTime())) {
            throw new VotingClosedException("Voting is currently closed.");
        }
        
        // Check if voter has already voted in this election
        if (voteRepository.existsByVoterIdAndElectionId(voterId, voteRequest.getElectionId())) {
            throw new DuplicateVoteException("Voter has already voted in this election");
        }
        
        // Check if candidate exists and belongs to the election
        Candidate candidate = candidateRepository.findById(voteRequest.getCandidateId())
            .orElseThrow(() -> new RuntimeException("Candidate not found"));
        
        if (!candidate.getElection().getId().equals(voteRequest.getElectionId())) {
            throw new RuntimeException("Candidate does not belong to this election");
        }
        
        // Create and save vote
        Vote vote = new Vote(voter, candidate, election);
        return voteRepository.save(vote);
    }
    
    public List<Candidate> getCandidatesForElection(Long electionId) {
        return candidateRepository.findByElectionId(electionId);
    }
    
    public List<Object[]> getElectionResults(Long electionId) {
        return voteRepository.getElectionResults(electionId);
    }
    
    public Optional<Vote> getVoterVoteForElection(Long voterId, Long electionId) {
        return voteRepository.findByVoterIdAndElectionId(voterId, electionId);
    }
}
