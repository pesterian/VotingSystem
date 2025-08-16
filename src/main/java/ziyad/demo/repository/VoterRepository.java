package ziyad.demo.repository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ziyad.demo.entity.Voter;
import java.util.List;

@Repository
public interface VoterRepository extends CrudRepository<Voter, Long> {
    
    List<Voter> findByCity(String city);
    
    List<Voter> findByIsAssigned(boolean isAssigned);
    
    @Query("SELECT v FROM Voter v WHERE v.city = :city AND v.isAssigned = false")
    List<Voter> findUnassignedVotersByCity(@Param("city") String city);
    
    @Modifying
    @Query("UPDATE Voter v SET v.isAssigned = :assigned WHERE v.city = :city")
    int assignVotersByCity(@Param("city") String city, @Param("assigned") boolean assigned);
}
