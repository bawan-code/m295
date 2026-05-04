package ch.mahmud.bawan.job_marketplace.services;

import ch.mahmud.bawan.job_marketplace.models.JobPosting;
import ch.mahmud.bawan.job_marketplace.models.SavedJob;
import ch.mahmud.bawan.job_marketplace.models.User;
import ch.mahmud.bawan.job_marketplace.repositories.JobPostingRepository;
import ch.mahmud.bawan.job_marketplace.repositories.SavedJobRepository;
import ch.mahmud.bawan.job_marketplace.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SavedJobService {

    private final SavedJobRepository savedJobRepository;
    private final UserRepository userRepository;
    private final JobPostingRepository jobPostingRepository;

    public SavedJobService(
            SavedJobRepository savedJobRepository,
            UserRepository userRepository,
            JobPostingRepository jobPostingRepository
    ) {
        this.savedJobRepository = savedJobRepository;
        this.userRepository = userRepository;
        this.jobPostingRepository = jobPostingRepository;
    }

    public List<SavedJob> getAllSavedJobs() {
        return savedJobRepository.findAll();
    }

    public Optional<SavedJob> getSavedJobById(Integer id) {
        return savedJobRepository.findById(id);
    }

    public Optional<SavedJob> saveJob(Integer userId, Integer jobId) {
        Optional<User> user = userRepository.findById(userId);
        Optional<JobPosting> jobPosting = jobPostingRepository.findById(jobId);

        if (user.isEmpty() || jobPosting.isEmpty()) {
            return Optional.empty();
        }

        SavedJob savedJob = new SavedJob();
        savedJob.setUser(user.get());
        savedJob.setJobPosting(jobPosting.get());

        SavedJob savedResult = savedJobRepository.save(savedJob);

        return Optional.of(savedResult);
    }

    public boolean deleteSavedJob(Integer id) {
        if (!savedJobRepository.existsById(id)) {
            return false;
        }

        savedJobRepository.deleteById(id);
        return true;
    }
}