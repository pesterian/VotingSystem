package ziyad.demo.entity;

import jakarta.persistence.*;
import java.util.List;

@Entity
@DiscriminatorValue("VOTER")
public class Voter extends User {
    
    private boolean isAssigned = false;
    
    @OneToMany(mappedBy = "voter", fetch = FetchType.LAZY)
    private List<Vote> votes;
    
    // Constructors
    public Voter() {
        super();
        setRole(Role.VOTER);
    }
    
    public Voter(String email, String password, String name, String city) {
        super(email, password, name, Role.VOTER);
        setCity(city);
    }
    
    // Getters and Setters
    public boolean isAssigned() { return isAssigned; }
    public void setAssigned(boolean assigned) { isAssigned = assigned; }
    
    public List<Vote> getVotes() { return votes; }
    public void setVotes(List<Vote> votes) { this.votes = votes; }
}
