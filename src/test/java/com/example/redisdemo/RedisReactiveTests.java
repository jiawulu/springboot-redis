package com.example.redisdemo;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import org.apache.commons.io.FileUtils;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.StringRedisConnection;
import org.springframework.data.redis.core.BoundZSetOperations;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.test.context.junit4.SpringRunner;
import reactor.core.publisher.Flux;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RedisReactiveTests {

    @Autowired
    private ReactiveRedisTemplate<String,String> redisTemplate;

    @Test
    public void testScript() throws IOException, InterruptedException {

        Flux execute1 = redisTemplate.execute(script(),
                                              Arrays.asList(new String[] {"zset_key_3", "zset_key_2", "zset_key_1"}));

        execute1.subscribe(o -> {
            System.out.println(o);
        });


    }

    public RedisScript script() throws IOException {
        String file = (String)RedisReactiveTests.class.getClassLoader().getResource("sort_pop.lua").getFile();
        String string = FileUtils.readFileToString(new File(file));
        return RedisScript.of(string, String.class);
    }

}
