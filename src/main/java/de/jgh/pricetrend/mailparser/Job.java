package de.jgh.pricetrend.mailparser;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
public class Job implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private LocalDateTime initDateTime;
    private LocalDateTime finishDateTime;
    @Enumerated(EnumType.STRING)
    private JobStatus jobStatus;
    private int processedEntries;

    public Job() {
        this.initDateTime = LocalDateTime.now();
        this.jobStatus = JobStatus.CREATED;
        this.processedEntries = 0;
    }

    public Job startJob() {
        this.setJobStatus(JobStatus.STARTED);
        return this;
    }

    public Job finishJob() {
        setJobStatus(JobStatus.FINISHED);
        this.setFinishDateTime(LocalDateTime.now());
        return this;
    }

    public int getProcessedEntries() {
        return processedEntries;
    }

    public void setProcessedEntries(int processedEntries) {
        this.processedEntries = processedEntries;
    }

    public Job(LocalDateTime initDateTime) {
        this.initDateTime = initDateTime;
    }

    public LocalDateTime getFinishDateTime() {
        return finishDateTime;
    }

    public void setFinishDateTime(LocalDateTime finishDateTime) {
        this.finishDateTime = finishDateTime;
    }

    public JobStatus getJobStatus() {
        return jobStatus;
    }

    public void setJobStatus(JobStatus jobStatus) {
        this.jobStatus = jobStatus;
    }
}
