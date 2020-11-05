package com.example.test.web;

import com.example.test.service.MainService;
import com.kim.idempotent.Idempotent;
import com.kim.idempotent.IdempotentMode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author: kim.zjy
 * @email: kim.zjy@bitsun-inc.com
 * @create: 2020-10-14 10:58
 */
@RestController
@Slf4j
public class MainController {

    @Autowired
    private MainService mainService ;

    @GetMapping(value = "/test")
    @Idempotent(mode = IdempotentMode.DUPLICATE ,  expire = 10 , times = 1 )
    public String  test(@RequestParam(value = "args") String args){
        log.info("调用 MainController , args {} " , args);

        return mainService.service(args);
    }
}
