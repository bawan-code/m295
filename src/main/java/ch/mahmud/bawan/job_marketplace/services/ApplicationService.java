package ch.mahmud.bawan.job_marketplace.services;

import ch.mahmud.bawan.job_marketplace.models.Application;
import ch.mahmud.bawan.job_marketplace.models.JobPosting;
import ch.mahmud.bawan.job_marketplace.models.Status;
import ch.mahmud.bawan.job_marketplace.models.User;
import ch.mahmud.bawan.job_marketplace.repositories.ApplicationRepository;
import ch.mahmud.bawan.job_marketplace.repositories.JobPostingRepository;
import ch.mahmud.bawan.job_marketplace.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ApplicationService {

    private final ApplicationRepository applicationRepository;
    private final UserRepository userRepository;
    private final JobPostingRepository jobPostingRepository;

    public ApplicationService(
            ApplicationRepository applicationRepository,
            UserRepository userRepository,
            JobPostingRepository jobPostingRepository
    ) {
        this.applicationRepository = applicationRepository;
        this.userRepository = userRepository;
        this.jobPostingRepository = jobPostingRepository;
    }

    public List<Application> getAllApplications() {
        return applicationRepository.findAll();
    }

    public Optional<Application> getApplicationById(Integer id) {
        return applicationRepository.findById(id);
    }

    public Optional<Application> createApplication(Integer jobSeekerId, Integer jobId) {
        Optional<User> jobSeeker = userRepository.findById(jobSeekerId);
        Optional<JobPosting> jobPosting = jobPostingRepository.findById(jobId);

        if (jobSeeker.isEmpty() || jobPosting.isEmpty()) {
            return Optional.empty();
        }

        Application application = new Application();
        application.setJobSeeker(jobSeeker.get());
        application.setJobPosting(jobPosting.get());
        application.setStatus(Status.PENDING);

        Application savedApplication = applicationRepository.save(application);

        return Optional.of(savedApplication);
    }

    public Optional<Application> updateApplicationStatus(Integer id, Status status) {
        return applicationRepository.findById(id).map(existingApplication -> {
            existingApplication.setStatus(status);

            return applicationRepository.save(existingApplication);
        });
    }

    public boolean deleteApplication(Integer id) {
        if (!applicationRepository.existsById(id)) {
            return false;
        }

        applicationRepository.deleteById(id);
        return true;
    }
}