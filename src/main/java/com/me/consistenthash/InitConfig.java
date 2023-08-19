package com.me.consistenthash;

import com.me.consistenthash.hash.CustomHash;
import com.me.consistenthash.node.Node;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericToStringSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.util.Map;

@Configuration
@Slf4j
public class InitConfig {

   // @Value("${consistent-hash.name}")
   // private String nodeName;

    @Value("${consistent-hash.numberOfVirtualNode}")
    private int numberOfVirtualNode;

    @Bean
    public Node node(CustomHash customHash) {
        final Map<String, String> getenv = System.getenv();
        final String nodeName = getenv.get("nodename");
        return new Node(nodeName, numberOfVirtualNode, customHash);
    }

    @Bean
    public RedisTemplate<String, Boolean> redisTemplate(RedisConnectionFactory connectionFactory) {
        final RedisTemplate<String, Boolean> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new GenericToStringSerializer<>(Boolean.class));
        return template;
    }
}
