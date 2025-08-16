package ziyad.demo.dto;

import jakarta.validation.constraints.NotNull;

public class VoteRequest {
    
    @NotNull(message = "Candidate ID is required")
    private Long candidateId;
    
    @NotNull(message = "Election ID is required")
    private Long electionId;
    
    // Constructors
    public VoteRequest() {}
    
    public VoteRequest(Long candidateId, Long electionId) {
        this.candidateId = candidateId;
        this.electionId = electionId;
    }
    
    // Getters and Setters
    public Long getCandidateId() { return candidateId; }
    public void setCandidateId(Long candidateId) { this.candidateId = candidateId; }
    
    public Long getElectionId() { return electionId; }
    public void setElectionId(Long electionId) { this.electionId = electionId; }
}
