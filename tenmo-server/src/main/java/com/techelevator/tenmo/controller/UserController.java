package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.UserDao;
import com.techelevator.tenmo.model.User;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@PreAuthorize("isAuthenticated()")
@RestController

public class UserController {
    private UserDao userDao;

    public UserController(UserDao userDao){
        this.userDao = userDao;
    }

    @RequestMapping(path = "/user", method = RequestMethod.GET)
    public List<User> list(){
        return userDao.findAll();
    }

    @RequestMapping(path = "/user/{id}", method = RequestMethod.GET)
    public User findUserById(@RequestParam int id){
        User user = userDao.getUserById(id);
        return user;
    }

    @RequestMapping(path = "/user/username", method = RequestMethod.GET)
    public User findUserByUsername(@RequestParam String name){
        User user = userDao.findByUsername(name);
        return user;
    }

    @RequestMapping(path = "/user/{id}/username", method = RequestMethod.GET)
    public int findIdByUsername(@RequestParam String username){
        int id = userDao.findIdByUsername(username);
        return id;
    }
}
