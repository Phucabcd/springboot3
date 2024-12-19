package ntp.springboot3.repo;

import ntp.springboot3.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface UserRepo extends JpaRepository<User, String> {
    boolean existsByUsername(String username);;
    Optional<User> findByUsername(String username);
}
