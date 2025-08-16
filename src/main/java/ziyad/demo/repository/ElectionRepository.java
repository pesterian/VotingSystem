package ziyad.demo.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ziyad.demo.entity.Election;

@Repository
public interface ElectionRepository extends CrudRepository<Election, Long> {
}
