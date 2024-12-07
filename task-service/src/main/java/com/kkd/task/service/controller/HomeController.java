package com.kkd.task.service.controller;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class HomeController {


    @GetMapping("/task")
    public ResponseEntity<String>getAssignedUserTask(){


        return new ResponseEntity<>("Welcome To Task Service", HttpStatus.OK);
    }
}
