package com.me.consistenthash;

import com.me.consistenthash.hash.CustomHash;
import com.me.consistenthash.node.CacheNode;
import com.me.consistenthash.node.Node;
import com.me.consistenthash.walker.ConsistentHashRouter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@Slf4j
@RequiredArgsConstructor
public class MyController {

    private final RedisService redisService;
    private final CustomHash customHash;

    private final CacheNode cacheNode;

    @Value("${consistent-hash.numberOfVirtualNode}")
    private int nodeCount;
    @Value("${consistent-hash.name}")
    public String nodeName;

    private int cacheMiss;
    private int cacheHit;

    @GetMapping("/info")
    public String getInfo() {
        log.info("cacheHit = {}, cacheMiss = {}", cacheHit, cacheMiss);
        return "ok";
    }


    // 얘는 판단하지 않고 그냥 넣기만 한다.
    @GetMapping("/put/{key}")
    public String putData(@PathVariable String key) {
        cacheNode.findInCache(key);
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


    @GetMapping("/test")

    @GetMapping("/data/{key}")
    public String getData(@PathVariable String key) {
        List<Node> nodes = redisService.scanKeys("*").stream()
                .map(nodeName -> new Node(nodeName, nodeCount, customHash))
                .toList();

        ConsistentHashRouter consistentHashRouter = new ConsistentHashRouter(customHash, nodes);
        String parentNodeName = consistentHashRouter.route(key);

        if (parentNodeName.equals(nodeName)) {
            this.cacheHit++;
        } else {
            this.cacheMiss++;
        }

        return "ok";
    }
}
