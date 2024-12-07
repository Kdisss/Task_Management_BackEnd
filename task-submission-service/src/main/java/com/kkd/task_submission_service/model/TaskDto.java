package com.kkd.task_submission_service.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaskDto {

    private long id;

    private String title;

    private String description;

    private String image;

    private long assignedUserId;

    private List<String> tags = new ArrayList<>();

    private TaskStatus status;

    private LocalDateTime deadline;

    private LocalDateTime createdAt;
}
