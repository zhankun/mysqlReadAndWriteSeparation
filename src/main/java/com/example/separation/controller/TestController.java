package com.example.separation.controller;

import com.example.separation.entity.User;
import com.example.separation.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by zhankun on 2018/11/13.
 */
@RequestMapping(value = "test")
@RestController
public class TestController {

    @Autowired
    UserService userService;

    @RequestMapping(value = "findById/{id}")
    public User findById(@PathVariable int id){
        User user = userService.findById(id);
        return user;
    }

    @RequestMapping(value = "insert")
    public String testInsert(){
        User user = new User();
        user.setAddress("beijing");
        user.setName("james");
        user.setSex(-1);
        userService.insert(user);
        return "success";
    }

    @RequestMapping(value = "update")
    public String testUpdate(){
        User user = new User();
        user.setId(124);
        user.setAge(288);
        userService.update(user);
        return "success";
    }
}
