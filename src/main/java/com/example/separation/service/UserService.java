package com.example.separation.service;

import com.example.separation.entity.User;
import org.springframework.stereotype.Service;

/**
 * Created by zhankun on 2018/11/13.
 */
public interface UserService {

    User findById(int id);

    void insert(User user);

    void update(User user);
}
