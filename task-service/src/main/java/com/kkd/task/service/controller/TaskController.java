package com.kkd.task.service.controller;

import com.kkd.task.service.model.Task;
import com.kkd.task.service.model.TaskStatus;
import com.kkd.task.service.model.UserDto;
import com.kkd.task.service.service.TaskService;
import com.kkd.task.service.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    @Autowired
    private TaskService taskService;

    @Autowired
    private UserService userService;


    @PostMapping
    public ResponseEntity<Task> createTask(@RequestBody Task task,
                                           @RequestHeader("Authorization") String jwt) throws Exception{

        UserDto user = userService.getUserProfile(jwt);

        Task createdTask = taskService.createTask(task, user.getRole());

        return new ResponseEntity<>(createdTask, HttpStatus.CREATED);
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<Task> getTaskById(@PathVariable Long id,
                                            @RequestHeader("Authorization") String jwt) throws Exception {

        UserDto user = userService.getUserProfile(jwt);

        Task task = taskService.getTaskById(id);

        return new ResponseEntity<>(task, HttpStatus.OK);
    }

    @GetMapping("/user")
    public ResponseEntity<List<Task>>getAssignedUserTask(
            @RequestParam(required = false) TaskStatus status,
            @RequestHeader("Authorization") String jwt) throws Exception{

        UserDto user = userService.getUserProfile(jwt);

        List<Task> task = taskService.assignedUsersTask(user.getId(), status);

        return new ResponseEntity<>(task, HttpStatus.OK);
    }

    @GetMapping()
    public ResponseEntity<List<Task>> getAllTask(
            @RequestParam(required = false)TaskStatus status,
            @RequestHeader("Authorization") String jwt) throws Exception{

        UserDto user = userService.getUserProfile(jwt);

        List<Task>  task = taskService.getAllTask(status);

        return new ResponseEntity<>(task, HttpStatus.OK);
    }

    @GetMapping("/{id}/user/{userid}/assigned")
    public ResponseEntity<Task> assignedTaskToUser(
            @PathVariable Long id,
            @PathVariable Long userid,
            @RequestHeader("Authorization") String jwt) throws Exception{

        UserDto user = userService.getUserProfile(jwt);

        Task task = taskService.assignedToUser(userid, id);

        return new ResponseEntity<>(task, HttpStatus.OK);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Task> updateTask(
            @PathVariable Long id,
            @RequestBody Task req,
            @RequestHeader("Authorization") String jwt) throws Exception{

        UserDto user = userService.getUserProfile(jwt);

        Task task = taskService.updateTask(id, req, user.getId());

        return new ResponseEntity<>(task, HttpStatus.OK);
    }

    @PutMapping("/complete/{id}")
    public ResponseEntity<Task> completeTask(
            @PathVariable Long id) throws Exception {

        Task task = taskService.completeTask(id);

        return new ResponseEntity<>(task, HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteTask(
            @PathVariable Long id) throws Exception {

        taskService.deleteTask(id);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
