package com.kim.jparedisdemo.web;

import com.kim.jparedisdemo.model.User;
import com.kim.jparedisdemo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "user")
public class UserController {

    @Autowired
    private UserService userService ;


    @RequestMapping(value = "create" , method = RequestMethod.POST)
    public User create(@RequestBody  User user){
        userService.save(user);
        return user;
    }

    @RequestMapping(value = "get" , method = RequestMethod.GET)
    public User get(@RequestParam(value = "id") Long id ){
        return userService.get(id);
    }

    @RequestMapping(value = "update" , method = RequestMethod.POST)
    public User update(@RequestBody User user){
        userService.save(user);
        return userService.get(user.getId());
    }



}
