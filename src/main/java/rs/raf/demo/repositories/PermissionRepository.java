package rs.raf.demo.repositories;

import org.springframework.data.repository.CrudRepository;
import rs.raf.demo.entities.Permission;

public interface PermissionRepository extends CrudRepository<Permission, Long> {
}
