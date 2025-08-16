package ziyad.demo.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "candidates")
public class Candidate {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String name;
    
    @Column(nullable = false)
    private String party;
    
    private String description;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "election_id", nullable = false)
    private Election election;
    
    @OneToMany(mappedBy = "candidate", fetch = FetchType.LAZY)
    private List<Vote> votes;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();
    
    // Constructors
    public Candidate() {}
    
    public Candidate(String name, String party, String description, Election election) {
        this.name = name;
        this.party = party;
        this.description = description;
        this.election = election;
    }
    
    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public String getParty() { return party; }
    public void setParty(String party) { this.party = party; }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    
    public Election getElection() { return election; }
    public void setElection(Election election) { this.election = election; }
    
    public List<Vote> getVotes() { return votes; }
    public void setVotes(List<Vote> votes) { this.votes = votes; }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
