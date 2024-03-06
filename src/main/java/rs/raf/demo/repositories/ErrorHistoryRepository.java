package rs.raf.demo.repositories;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import rs.raf.demo.entities.ErrorHistory;
import rs.raf.demo.entities.User;

import java.util.List;

public interface ErrorHistoryRepository extends JpaRepository<ErrorHistory, Long> {
    List<ErrorHistory> findByUser(Pageable pageable, User user);
}
