package ziyad.demo.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class CandidateRegistrationRequest {
    
    @NotBlank(message = "Name is required")
    @Size(min = 2, max = 100, message = "Name must be between 2 and 100 characters")
    private String name;
    
    @NotBlank(message = "Party is required")
    @Size(min = 2, max = 50, message = "Party must be between 2 and 50 characters")
    private String party;
    
    @Size(max = 500, message = "Description must not exceed 500 characters")
    private String description;
    
    @NotNull(message = "Election ID is required")
    private Long electionId;
    
    // Constructors
    public CandidateRegistrationRequest() {}
    
    public CandidateRegistrationRequest(String name, String party, String description, Long electionId) {
        this.name = name;
        this.party = party;
        this.description = description;
        this.electionId = electionId;
    }
    
    // Getters and Setters
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public String getParty() { return party; }
    public void setParty(String party) { this.party = party; }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    
    public Long getElectionId() { return electionId; }
    public void setElectionId(Long electionId) { this.electionId = electionId; }
}
