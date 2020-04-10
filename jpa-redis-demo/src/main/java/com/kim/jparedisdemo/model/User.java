package com.kim.jparedisdemo.model;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity(name = "kim_user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id ;

    @Column(name = "name")
    private String name ;

    @Column(name = "createTime")
    private Date createTime ;

    @Column(name = "phone")
    private String phone ;

    public User() {
     createTime = new Date();
    }
}
