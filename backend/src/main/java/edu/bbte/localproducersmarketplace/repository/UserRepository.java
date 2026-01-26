package edu.bbte.localproducersmarketplace.repository;

import edu.bbte.localproducersmarketplace.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmail(String email);
}
