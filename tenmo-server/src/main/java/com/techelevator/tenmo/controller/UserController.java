package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.UserDao;
import com.techelevator.tenmo.model.User;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@PreAuthorize("isAuthenticated()")
@RestController

public class UserController {
    //Basic Controller

    private UserDao userDao;

    public UserController(UserDao userDao){
        this.userDao = userDao;
    }

    @RequestMapping(path = "/user", method = RequestMethod.GET)
    public List<User> listUsers(){
        return userDao.findAll();
    }

    @RequestMapping(path = "/user/id/{id}", method = RequestMethod.GET)
    public User findUserById(@PathVariable int id){
        return userDao.getUserById(id);
    }

    @RequestMapping(path = "/user/{username}", method = RequestMethod.GET)
    public User findUserByUsername(@PathVariable String username){
        User user = userDao.findByUsername(username);
        return user;
    }

}
