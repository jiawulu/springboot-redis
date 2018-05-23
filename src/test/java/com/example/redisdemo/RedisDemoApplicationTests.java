package com.example.redisdemo;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import org.apache.commons.io.FileUtils;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.StringRedisConnection;
import org.springframework.data.redis.core.BoundZSetOperations;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.scripting.ScriptSource;
import org.springframework.scripting.support.ResourceScriptSource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.FileSystemUtils;
import org.springframework.util.StringUtils;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RedisDemoApplicationTests {

    @Autowired
    private RedisTemplate<String,String> redisTemplate;

    String[] keys = new String[]{"zset_key_3","zset_key_3"};

    @Test
    public void initList() {

        redisTemplate.delete("zset_key_3");
        redisTemplate.delete("zset_key_3");

        BoundZSetOperations zSetOperations = redisTemplate.boundZSetOps("zset_key_3");
        Boolean add = zSetOperations.add("1", 1000);
        Boolean add2 = zSetOperations.add("2", 1001);
        Boolean add3 = zSetOperations.add("3", 1002);
        Boolean add4 = zSetOperations.add("4", 999);

        Assert.assertTrue(add);

        BoundZSetOperations zSetOperations2 = redisTemplate.boundZSetOps("zset_key_2");
        Assert.assertTrue(zSetOperations2.add("5", 1000));
        Assert.assertTrue(zSetOperations2.add("6", 1000));
        Assert.assertTrue(zSetOperations2.add("7", 1000));
        Assert.assertTrue(zSetOperations2.add("8", 1001));

    }


    @Test
    public void testPipeline() {

        String unionKey = "KEY_WAITING_UNION_TMP";

        String[] keys = new String[]{"wuzhongtest_key2","wuzhongtest_key1"};

        Set<String> range = redisTemplate.boundZSetOps(keys[0]).range(0, 0);

        List<Object> objects = redisTemplate.executePipelined(new RedisCallback<Object>() {
            @Override
            public Object doInRedis(RedisConnection connection) throws DataAccessException {
                StringRedisConnection stringRedisConn = (StringRedisConnection) connection;
                stringRedisConn.zUnionStore(unionKey, keys);
                stringRedisConn.zRange(unionKey, 0, 0);
                return null;
            }
        });


    }

    @Test
    public void testScript() throws IOException {
        Object execute = redisTemplate.execute(script(), Arrays.asList(new String[]{"zset_key_3","zset_key_2","zset_key_1"}));
        System.out.println(execute);

    }

    public RedisScript script() throws IOException {
        String file = (String)RedisDemoApplicationTests.class.getClassLoader().getResource("sort_pop.lua").getFile();
        String string = FileUtils.readFileToString(new File(file));
        return RedisScript.of(string, String.class);
    }

}
