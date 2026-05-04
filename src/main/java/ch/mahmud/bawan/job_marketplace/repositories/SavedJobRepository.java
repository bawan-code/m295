package ch.mahmud.bawan.job_marketplace.repositories;

import ch.mahmud.bawan.job_marketplace.models.SavedJob;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SavedJobRepository extends JpaRepository<SavedJob, Integer> {
}