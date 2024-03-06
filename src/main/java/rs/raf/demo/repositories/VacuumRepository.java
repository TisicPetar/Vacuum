package rs.raf.demo.repositories;

import org.springframework.data.repository.CrudRepository;
import rs.raf.demo.entities.Status;
import rs.raf.demo.entities.User;
import rs.raf.demo.entities.Vacuum;

import java.util.List;
import java.util.Optional;

public interface VacuumRepository extends CrudRepository<Vacuum, Long> {
    Optional<Vacuum> findById
            (Long id);
    List<Vacuum> findByAddedByUserAndActive
            (User addedByUser, boolean active);
    List<Vacuum> findByAddedByUserAndNameContainingAndStatusAndCreationDateBetweenAndActive
            (User user, String name, Status status, Long dateFrom, Long dateTo, boolean active);
    List<Vacuum> findByAddedByUserAndNameContainingAndActive
            (User user, String name, boolean active);
    List<Vacuum> findByAddedByUserAndStatusAndActive
            (User user, Status status, boolean active);
    List<Vacuum> findByAddedByUserAndCreationDateBetweenAndActive
            (User user, Long dateFrom, Long dateTo, boolean active);
    List<Vacuum> findByAddedByUserAndCreationDateAfterAndActive
            (User user, Long dateFrom, boolean active);
    List<Vacuum> findByAddedByUserAndCreationDateBeforeAndActive
            (User user, Long dateTo, boolean active);
}
