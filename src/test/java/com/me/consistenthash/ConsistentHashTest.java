package com.me.consistenthash;

import com.me.consistenthash.hash.CustomHash;
import com.me.consistenthash.hash.MD5Hash;
import com.me.consistenthash.hash.NormalHash;
import com.me.consistenthash.node.Node;
import com.me.consistenthash.node.NormalHashNode;
import com.me.consistenthash.walker.ConsistentHashRouter;
import com.me.consistenthash.walker.HashRouter;
import com.me.consistenthash.walker.NormalHashRouter;
import com.navercorp.fixturemonkey.FixtureMonkey;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@SpringBootTest
@Slf4j
public class ConsistentHashTest {

    final FixtureMonkey fixture = FixtureMonkey.create();

    @Autowired
    CustomHash customHash;

    @Autowired
    RedisTemplate<String, Boolean> redisTemplate;

    @Autowired
    RedisService redisService;

    @Value("${consistent-hash.numberOfVirtualNode}")
    private int nodeCount;
    @Value("${consistent-hash.name}")
    public String nodeName;


    Map<String, Integer> serverPort;
    @BeforeEach
    void setUp() {
        this.serverPort = new HashMap<>();

        serverPort.put("server1", 28080);
        serverPort.put("server2", 28081);
        serverPort.put("server3", 28082);
        serverPort.put("server4", 28083);
        serverPort.put("server5", 28084);
    }



    @Test
    void E2ETest() throws InterruptedException {
        // Given
        final List<String> traffics = IntStream.range(0, 10_000)
                .mapToObj(String::valueOf)
                .toList();

        final List<Node> nodes = getNode();
        final ConsistentHashRouter consistentHashRouter = new ConsistentHashRouter(customHash, nodes);

        printNowStatus(nodes);
        // When
        putData(traffics, consistentHashRouter);

        // Then
        printNowStatus(nodes);
    }

    @Test
    void E2ETestWithNormalHash() throws InterruptedException {
        // Given
        final List<String> traffics = IntStream.range(0, 10_000)
                .mapToObj(String::valueOf)
                .toList();

        final List<Node> nodes = getNormalNode();
        final NormalHashRouter normalHashRouter = new NormalHashRouter(customHash, nodes);

        printNowStatus(nodes);
        // When
        putData(traffics, normalHashRouter);

        // Then
        printNowStatus(nodes);
    }

    private List<Node> getNode() {
        return redisService.scanKeys("*").stream()
                .map(nodeName -> new Node(nodeName, nodeCount, customHash))
                .toList();
    }

    private List<Node> getNormalNode() {
        return redisService.scanKeys("*").stream()
                .map(nodeName -> (Node)(new NormalHashNode(nodeName, customHash)))
                .toList();
    }

    private void putData(List<String> datas, HashRouter hashRouter) throws InterruptedException {
        for (String key : datas) {
            try (CloseableHttpClient httpClient = HttpClients.createDefault();) {
                final String parentNodeName = hashRouter.route(key);
                Integer port = serverPort.get(parentNodeName);
                final String url = String.format("http://localhost:%d/put/%s", port, key);
                final HttpGet httpGet = new HttpGet(url);
                httpClient.execute(httpGet);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void printNowStatus(List<Node> nodes) throws InterruptedException {
        try (CloseableHttpClient httpClient = HttpClients.createDefault();) {
            for (Node node : nodes) {
                Integer port = serverPort.get(node.getIp());
                final String url = String.format("http://localhost:%d/show-status", port);
                final HttpGet httpGet = new HttpGet(url);
                HttpResponse response = httpClient.execute(httpGet);
                HttpEntity entity = response.getEntity();
                String responseBody = EntityUtils.toString(entity);
                log.info("nodeName = {}, body = {}", node.getIp(), responseBody);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
