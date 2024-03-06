package rs.raf.demo.repositories;

import org.springframework.data.repository.CrudRepository;
import rs.raf.demo.entities.Status;

public interface StatusRepository extends CrudRepository<Status, Long> {
    Status findByStatus(String name);
}
