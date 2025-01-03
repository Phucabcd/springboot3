package ntp.springboot3.repo;

import ntp.springboot3.entity.Permission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;



@Repository
public interface PermissionRepo extends JpaRepository<Permission, String> {
}
