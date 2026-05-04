package ch.mahmud.bawan.job_marketplace.entities;

import ch.mahmud.bawan.job_marketplace.enumerations.Status;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "applications")
public class Application {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer applicationId;

    @Enumerated(EnumType.STRING)
    private Status status;

    private LocalDateTime appliedAt;

    @ManyToOne
    @JoinColumn(name = "job_id", nullable = false)
    private JobPosting jobPosting;

    @ManyToOne
    @JoinColumn(name = "job_seeker_id", nullable = false)
    private User jobSeeker;

    public Application() {
    }

    @PrePersist
    protected void onCreate() {
        this.appliedAt = LocalDateTime.now();

        if (this.status == null) {
            this.status = Status.PENDING;
        }
    }
}