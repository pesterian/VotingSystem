package ziyad.demo.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ziyad.demo.entity.Candidate;
import java.util.List;

@Repository
public interface CandidateRepository extends CrudRepository<Candidate, Long> {
    
    List<Candidate> findByElectionId(Long electionId);
    
    List<Candidate> findByNameContaining(String name);
    
    @Query("SELECT c FROM Candidate c WHERE c.election.id = :electionId ORDER BY c.name")
    List<Candidate> findCandidatesByElectionOrderedByName(@Param("electionId") Long electionId);
}
