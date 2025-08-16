package ziyad.demo.entity;

import jakarta.persistence.*;

@Entity
@DiscriminatorValue("ADMIN")
public class Admin extends User {
    
    // Constructors
    public Admin() {
        super();
        setRole(Role.ADMIN);
    }
    
    public Admin(String email, String password, String name) {
        super(email, password, name, Role.ADMIN);
    }
}
