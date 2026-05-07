package ch.mahmud.bawan.job_marketplace.services;

import ch.mahmud.bawan.job_marketplace.models.JobApplication;
import ch.mahmud.bawan.job_marketplace.models.JobPosting;
import ch.mahmud.bawan.job_marketplace.models.Status;
import ch.mahmud.bawan.job_marketplace.models.User;
import ch.mahmud.bawan.job_marketplace.repositories.JobApplicationRepository;
import ch.mahmud.bawan.job_marketplace.repositories.JobPostingRepository;
import ch.mahmud.bawan.job_marketplace.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class JobApplicationService {

    private final JobApplicationRepository jobApplicationRepository;
    private final UserRepository userRepository;
    private final JobPostingRepository jobPostingRepository;

    public JobApplicationService(
            JobApplicationRepository jobApplicationRepository,
            UserRepository userRepository,
            JobPostingRepository jobPostingRepository
    ) {
        this.jobApplicationRepository = jobApplicationRepository;
        this.userRepository = userRepository;
        this.jobPostingRepository = jobPostingRepository;
    }

    public List<JobApplication> getAllApplications() {
        return jobApplicationRepository.findAll();
    }

    public Optional<JobApplication> getApplicationById(Integer id) {
        return jobApplicationRepository.findById(id);
    }

    public Optional<JobApplication> createApplication(Integer jobSeekerId, Integer jobId) {
        Optional<User> jobSeeker = userRepository.findById(jobSeekerId);
        Optional<JobPosting> jobPosting = jobPostingRepository.findById(jobId);

        if (jobSeeker.isEmpty() || jobPosting.isEmpty()) {
            return Optional.empty();
        }

        JobApplication jobApplication = new JobApplication();
        jobApplication.setJobSeeker(jobSeeker.get());
        jobApplication.setJobPosting(jobPosting.get());
        jobApplication.setStatus(Status.PENDING);

        JobApplication savedJobApplication = jobApplicationRepository.save(jobApplication);

        return Optional.of(savedJobApplication);
    }

    public Optional<JobApplication> updateApplicationStatus(Integer id, Status status) {
        return jobApplicationRepository.findById(id).map(existingJobApplication -> {
            existingJobApplication.setStatus(status);

            return jobApplicationRepository.save(existingJobApplication);
        });
    }

    public boolean deleteApplication(Integer id) {
        if (!jobApplicationRepository.existsById(id)) {
            return false;
        }

        jobApplicationRepository.deleteById(id);
        return true;
    }
}