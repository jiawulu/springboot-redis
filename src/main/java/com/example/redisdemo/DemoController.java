package com.example.redisdemo;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * @author wuzhong on 2018/5/23.
 * @version 1.0
 */
@RestController
@RequestMapping("demos")
public class DemoController {

    @Autowired
    private DemoService demoService;

    @GetMapping("create")
    public Mono<Boolean> create(){
        return demoService.initData();
    }

    @GetMapping("pop")
    public Flux<String> pop() throws IOException {
        return demoService.popFirstElement();
    }

}
