package com.kkd.task.user.service.repository;

import com.kkd.task.user.service.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    @Autowired
    public User findByEmail(String email);

}
