package ch.mahmud.bawan.job_marketplace.repositories;

import ch.mahmud.bawan.job_marketplace.models.Application;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ApplicationRepository extends JpaRepository<Application, Integer> {
}