package com.kkd.task_submission_service.service;


import com.kkd.task_submission_service.model.TaskDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.config.Task;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "SUBMISSION-SERVICE" , url = "http://localhost:5002/")
public interface TaskService {

    @GetMapping("/api/tasks/get/{id}")
    public TaskDto getTaskById(
            @PathVariable Long id,
            @RequestHeader("Authorization") String jwt) throws Exception;


    @PutMapping("/api/tasks/complete/{id}")
    public TaskDto completeTask(
            @PathVariable Long id) throws Exception;
}
