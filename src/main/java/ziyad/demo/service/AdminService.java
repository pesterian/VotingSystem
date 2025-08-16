package ziyad.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ziyad.demo.dto.CandidateRegistrationRequest;
import ziyad.demo.entity.Candidate;
import ziyad.demo.entity.Election;
import ziyad.demo.entity.Voter;
import ziyad.demo.repository.CandidateRepository;
import ziyad.demo.repository.ElectionRepository;
import ziyad.demo.repository.VoterRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class AdminService {
    
    @Autowired
    private CandidateRepository candidateRepository;
    
    @Autowired
    private VoterRepository voterRepository;
    
    @Autowired
    private ElectionRepository electionRepository;
    
    public Candidate registerCandidate(CandidateRegistrationRequest request) {
        Election election = electionRepository.findById(request.getElectionId())
            .orElseThrow(() -> new RuntimeException("Election not found"));
        
        Candidate candidate = new Candidate(
            request.getName(), 
            request.getParty(), 
            request.getDescription(), 
            election
        );
        
        return candidateRepository.save(candidate);
    }
    
    @Transactional
    public int assignVotersByCity(String city) {
        return voterRepository.assignVotersByCity(city, true);
    }
    
    public List<Voter> getUnassignedVotersByCity(String city) {
        return voterRepository.findUnassignedVotersByCity(city);
    }
    
    public List<Voter> getAllVoters() {
        return (List<Voter>) voterRepository.findAll();
    }
    
    public List<Candidate> getAllCandidates() {
        return (List<Candidate>) candidateRepository.findAll();
    }
    
    public Election createElection(String title, String description, 
                                 LocalDateTime startTime, LocalDateTime endTime) {
        Election election = new Election(title, description, startTime, endTime);
        return electionRepository.save(election);
    }
    
    public List<Election> getAllElections() {
        return (List<Election>) electionRepository.findAll();
    }
}
