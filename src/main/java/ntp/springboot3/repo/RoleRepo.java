package ntp.springboot3.repo;

import ntp.springboot3.entity.Permission;
import ntp.springboot3.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface RoleRepo extends JpaRepository<Role, String> {
}
