package com.kim.jparedisdemo.service;

import com.kim.jparedisdemo.model.User;

public interface UserService {

    /**
     * 保存用户
     * @param user 用户
     * @return 用户
     */
    User  save(User user);

    /**
     * 获取用户
     * @param id id
     * @return 用户
     */
    User get(Long id);
}
