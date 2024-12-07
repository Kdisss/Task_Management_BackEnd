package com.kkd.task_submission_service.controller;

import com.kkd.task_submission_service.model.Submission;
import com.kkd.task_submission_service.model.UserDto;
import com.kkd.task_submission_service.service.SubmissionService;
import com.kkd.task_submission_service.service.TaskService;
import com.kkd.task_submission_service.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/submissions")
public class SubmissionController {

    @Autowired
    private SubmissionService submissionService;

    @Autowired
    private UserService userService;

    @Autowired
    private TaskService taskService;

    @PostMapping()
    public ResponseEntity<Submission>submitTask(
            @RequestParam Long task_id,
            @RequestParam String github_link,
            @RequestHeader ("Authorization") String jwt
    )throws Exception{
        UserDto user = userService.getUserProfile(jwt);
        Submission submission = submissionService.submitTask(task_id,github_link,user.getId(),jwt);
        return new ResponseEntity<>(submission, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Submission>getSubmissionbyId(
            @PathVariable Long id,
            @RequestHeader ("Authorization") String jwt
    )throws Exception{
        UserDto user = userService.getUserProfile(jwt);
        Submission submissions = submissionService.getTaskSubmissionById(id);
        return new ResponseEntity<>(submissions, HttpStatus.CREATED);
    }

    @GetMapping()
    public ResponseEntity<List<Submission>>getAllSubmissions(
            @PathVariable Long id,
            @RequestHeader ("Authorization") String jwt
    )throws Exception{
        UserDto user = userService.getUserProfile(jwt);
        List<Submission> submissions = submissionService.getAllTaskSubmissions();
        return new ResponseEntity<>(submissions, HttpStatus.CREATED);
    }

    @GetMapping("/task/{taskId}")
    public ResponseEntity<List<Submission>>getAllSubmissionsByTaskId(
            @PathVariable Long taskId,
            @RequestHeader ("Authorization") String jwt
    )throws Exception{
        UserDto user = userService.getUserProfile(jwt);
        List<Submission> submissions = submissionService.getTaskSubmissionsByTaskId(taskId);
        return new ResponseEntity<>(submissions, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Submission>acceptOrDeclineSubmission(
            @PathVariable Long id,
            @RequestParam("status") String status,
            @RequestHeader ("Authorization") String jwt
    )throws Exception{
        UserDto user = userService.getUserProfile(jwt);
        Submission submission = submissionService.acceptDeclineSubmission(id,status);
        return new ResponseEntity<>(submission, HttpStatus.CREATED);
    }


    public TaskService getTaskService() {
        return taskService;
    }

    public void setTaskService(TaskService taskService) {
        this.taskService = taskService;
    }
}