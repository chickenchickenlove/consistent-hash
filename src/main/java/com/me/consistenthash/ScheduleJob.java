package com.me.consistenthash;


import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.TimeUnit;

@Component // 이걸 넣어야 됨.
@Slf4j
public class ScheduleJob {

    private String nodeName;
    private final RedisTemplate<String, Boolean> redisTemplate;

    public ScheduleJob(RedisTemplate<String, Boolean> redisTemplate) {
        Map<String, String> getenv = System.getenv();
        this.nodeName = getenv.get("nodename");
        this.redisTemplate = redisTemplate;
    }

//    @Scheduled(fixedRate = 300)
    public void healthCheck() {
        redisTemplate.opsForValue()
                        .set(nodeName, true, 1000, TimeUnit.MICROSECONDS);
    }

}
