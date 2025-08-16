package ziyad.demo.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "votes", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"voter_id", "election_id"})
})
public class Vote {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "voter_id", nullable = false)
    private Voter voter;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "candidate_id", nullable = false)
    private Candidate candidate;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "election_id", nullable = false)
    private Election election;
    
    @Column(name = "voted_at")
    private LocalDateTime votedAt = LocalDateTime.now();
    
    // Constructors
    public Vote() {}
    
    public Vote(Voter voter, Candidate candidate, Election election) {
        this.voter = voter;
        this.candidate = candidate;
        this.election = election;
    }
    
    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public Voter getVoter() { return voter; }
    public void setVoter(Voter voter) { this.voter = voter; }
    
    public Candidate getCandidate() { return candidate; }
    public void setCandidate(Candidate candidate) { this.candidate = candidate; }
    
    public Election getElection() { return election; }
    public void setElection(Election election) { this.election = election; }
    
    public LocalDateTime getVotedAt() { return votedAt; }
    public void setVotedAt(LocalDateTime votedAt) { this.votedAt = votedAt; }
}
