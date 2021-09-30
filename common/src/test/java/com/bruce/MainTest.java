package com.bruce;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Random;
import java.util.Set;

/**
 * MainTest
 * 类描述：
 *
 * @author:zhangyongbin5
 * @date:2021/9/26
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = CommonApplication.class)
@Slf4j
public class MainTest {

    @Autowired
    private RedisTemplate redisTemplate;

    String key = "click_orderByCount_sort";
    String userId_ = "user_id_";

    @Test
    public void init() throws Exception {
        for (int i = 0; i < 10; i++) {
            redisTemplate.opsForZSet().add(key, userId_ + i, 0.0);
        }
        Set<ZSetOperations.TypedTuple> set = redisTemplate.opsForZSet().reverseRangeByScoreWithScores(key, 0, System.currentTimeMillis());
        for (ZSetOperations.TypedTuple o : set) {
            System.out.println(o.getValue() + " , " + o.getScore());
        }
    }

    @Test
    public void redisSort() throws Exception {
        for (int i = 0; i < 1000; i++) {
            Thread.sleep(500);

            String s = userId_ + new Random().nextInt(10);

            Double score = redisTemplate.opsForZSet().score(key, s);

            int order = score.intValue() + 1;
            double newScore = Double.valueOf(order + "." + System.currentTimeMillis());

            double delta = newScore - score;
            redisTemplate.opsForZSet().incrementScore(key, s, delta);
        }
        Set<ZSetOperations.TypedTuple> set = redisTemplate.opsForZSet().reverseRangeByScoreWithScores(key, 0, System.currentTimeMillis());
        for (ZSetOperations.TypedTuple o : set) {
            System.out.println(o.getValue() + " , " + o.getScore());
        }
    }


}
