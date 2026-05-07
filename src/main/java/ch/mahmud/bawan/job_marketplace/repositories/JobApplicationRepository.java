package ch.mahmud.bawan.job_marketplace.repositories;

import ch.mahmud.bawan.job_marketplace.models.JobApplication;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JobApplicationRepository extends JpaRepository<JobApplication, Integer> {
}