package ch.mahmud.bawan.job_marketplace.repositories;

import ch.mahmud.bawan.job_marketplace.models.JobPosting;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JobPostingRepository extends JpaRepository<JobPosting, Integer> {
}