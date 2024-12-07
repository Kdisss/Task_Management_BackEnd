package com.kkd.task_submission_service.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Submission{

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;


    private Long taskId;


    private String githubLink;


    private Long userId;

    private String status = "PENDING";


    private LocalDateTime submissionTime;


    public void setStatus(String status) {
        this.status = status;
    }

    public Long getTaskId() {
        return taskId;
    }
}