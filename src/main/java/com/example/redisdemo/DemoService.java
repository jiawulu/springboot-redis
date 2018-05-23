package com.example.redisdemo;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.redis.core.ReactiveZSetOperations;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * @author wuzhong on 2018/5/23.
 * @version 1.0
 */
@Service
public class DemoService {

    @Autowired
    private ReactiveRedisTemplate<String, String> redisTemplate;

    public Mono<Boolean> initData() {

        ReactiveZSetOperations<String, String> stringStringReactiveZSetOperations = redisTemplate.opsForZSet();

        Mono<Boolean> zset_key_1 = stringStringReactiveZSetOperations.add("zset_key_1", String
            .valueOf(System.currentTimeMillis() % 100), System.currentTimeMillis() % 100);

        return zset_key_1;

    }

    public Flux<String> popFirstElement() throws IOException {

        Flux execute1 = redisTemplate.execute(script(),
                                              Arrays.asList(new String[] {"zset_key_3", "zset_key_2", "zset_key_1"}));

        return execute1;

    }

    private RedisScript script() throws IOException {
        String file = (String)DemoService.class.getClassLoader().getResource("sort_pop.lua").getFile();
        String string = FileUtils.readFileToString(new File(file));
        return RedisScript.of(string, String.class);
    }

}
