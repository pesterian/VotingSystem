package ziyad.demo.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ziyad.demo.entity.Vote;
import java.util.List;
import java.util.Optional;

@Repository
public interface VoteRepository extends CrudRepository<Vote, Long> {
    
    boolean existsByVoterIdAndElectionId(Long voterId, Long electionId);
    
    List<Vote> findByElectionId(Long electionId);
    
    Optional<Vote> findByVoterIdAndElectionId(Long voterId, Long electionId);
    
    @Query("SELECT v.candidate.id, v.candidate.name, COUNT(v) as voteCount FROM Vote v " +
           "WHERE v.election.id = :electionId " +
           "GROUP BY v.candidate.id, v.candidate.name " +
           "ORDER BY COUNT(v) DESC")
    List<Object[]> getElectionResults(@Param("electionId") Long electionId);
}
