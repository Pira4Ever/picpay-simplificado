package edu.octavio.simplified_picpay.domain.repository;

import edu.octavio.simplified_picpay.domain.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByEmail(String email);
    boolean existsByDocument(String document);
    Optional<User> findByEmail(String email);
}
