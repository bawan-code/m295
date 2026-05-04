package ch.mahmud.bawan.job_marketplace.repositories;

import ch.mahmud.bawan.job_marketplace.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer> {
}