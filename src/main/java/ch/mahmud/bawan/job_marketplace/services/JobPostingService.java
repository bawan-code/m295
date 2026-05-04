package ch.mahmud.bawan.job_marketplace.services;

import ch.mahmud.bawan.job_marketplace.models.JobPosting;
import ch.mahmud.bawan.job_marketplace.models.User;
import ch.mahmud.bawan.job_marketplace.repositories.JobPostingRepository;
import ch.mahmud.bawan.job_marketplace.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class JobPostingService {

    private final JobPostingRepository jobPostingRepository;
    private final UserRepository userRepository;

    public JobPostingService(
            JobPostingRepository jobPostingRepository,
            UserRepository userRepository
    ) {
        this.jobPostingRepository = jobPostingRepository;
        this.userRepository = userRepository;
    }

    public List<JobPosting> getAllJobPostings() {
        return jobPostingRepository.findAll();
    }

    public Optional<JobPosting> getJobPostingById(Integer id) {
        return jobPostingRepository.findById(id);
    }

    public Optional<JobPosting> createJobPosting(Integer employerId, JobPosting jobPosting) {
        Optional<User> employer = userRepository.findById(employerId);

        if (employer.isEmpty()) {
            return Optional.empty();
        }

        jobPosting.setEmployer(employer.get());
        JobPosting savedJobPosting = jobPostingRepository.save(jobPosting);

        return Optional.of(savedJobPosting);
    }

    public Optional<JobPosting> updateJobPosting(Integer id, JobPosting updatedJobPosting) {
        return jobPostingRepository.findById(id).map(existingJobPosting -> {
            existingJobPosting.setTitle(updatedJobPosting.getTitle());
            existingJobPosting.setDescription(updatedJobPosting.getDescription());
            existingJobPosting.setLocation(updatedJobPosting.getLocation());
            existingJobPosting.setSalaryRange(updatedJobPosting.getSalaryRange());

            return jobPostingRepository.save(existingJobPosting);
        });
    }

    public boolean deleteJobPosting(Integer id) {
        if (!jobPostingRepository.existsById(id)) {
            return false;
        }

        jobPostingRepository.deleteById(id);
        return true;
    }
}