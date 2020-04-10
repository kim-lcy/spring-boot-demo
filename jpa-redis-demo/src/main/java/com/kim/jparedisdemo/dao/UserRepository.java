package com.kim.jparedisdemo.dao;


import com.kim.jparedisdemo.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User,Long>{
}
