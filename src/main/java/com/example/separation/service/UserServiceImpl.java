package com.example.separation.service;

import com.example.separation.entity.User;
import com.example.separation.mapper.UserDao;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * Created by zhankun on 2018/11/13.
 */
@Service
public class UserServiceImpl implements UserService {

    @Resource
    private UserDao userDao;

    @Override
    public User findById(int id) {
        return userDao.findById(id);
    }

    @Override
    public void insert(User user) {
        userDao.insert(user);
    }

    @Override
    public void update(User user) {
        userDao.updateById(user);
    }
}
