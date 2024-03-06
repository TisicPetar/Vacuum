package rs.raf.demo.repositories;

import org.springframework.data.repository.CrudRepository;
import rs.raf.demo.entities.User;

public interface UserRepository extends CrudRepository<User, Integer> {
    User findById(Long id);
    User findByUsername(String username);
    void deleteUserById(Long id);
}
