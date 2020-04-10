package com.kim.jparedisdemo.service.impl;

import com.kim.jparedisdemo.dao.UserRepository;
import com.kim.jparedisdemo.model.User;
import com.kim.jparedisdemo.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository ;


    @Override
    public User save(User user) {
        log.info(" save User , {}" ,user);
        return null == user ? null : userRepository.save(user);
    }

    @Override
    public User get(Long id) {
        log.info(" get User , {}", id);
        return userRepository.findById(id).orElse(null);
    }
}
