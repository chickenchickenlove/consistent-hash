package com.me.consistenthash.node;

import com.me.consistenthash.hash.CustomHash;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class CacheNode {

    private final Map<Long, String> cache = new HashMap<>();
    private final CustomHash customHash;
    private long cacheHit = 0;
    private long cacheMiss = 0;

    public String findInCache(String data) {
        // key can be any other data. because method parameter `data` means `key`.
        long hash = this.customHash.hash(data);
        if (cache.containsKey(hash)) {
            cacheHit++;
            return cache.get(hash);
        }
        cacheMiss++;
        cache.put(hash, data);
        return null;
    }

    public long getCacheHit() {
        return cacheHit;
    }

    public long getCacheMiss() {
        return cacheMiss;
    }

    public void clearCache() {
        cache.clear();
        cacheHit = 0;
        cacheMiss = 0;
    }
}
