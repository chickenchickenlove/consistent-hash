package com.me.consistenthash;

import com.me.consistenthash.node.CacheNode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;


@RestController
@Slf4j
@RequiredArgsConstructor
public class MyController {
    private final CacheNode cacheNode;

    @GetMapping("/put/{key}")
    public String putData(@PathVariable String key) {
        cacheNode.findInCache(key);
        return "ok";
    }

    @PostMapping("/put")
    public String putDataWithKey(@RequestBody String data) {
        log.info("data = {}", data);
        return "ok";
    }


    @GetMapping("/show-status")
    public String putData() {
        final long cacheHit = cacheNode.getCacheHit();
        final long cacheMiss = cacheNode.getCacheMiss();

        return String.format("cache hit : %d, cache miss : %d",
                cacheHit,
                cacheMiss);
    }
}
