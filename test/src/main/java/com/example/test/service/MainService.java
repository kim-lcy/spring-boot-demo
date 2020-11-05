package com.example.test.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author: kim.zjy
 * @email: kim.zjy@bitsun-inc.com
 * @create: 2020-10-14 10:58
 */
@Service
@Slf4j
public class MainService {

    public String  service(String args){
        log.info("调用 MainService , args : {}", args);
        return "success";
    }
}
