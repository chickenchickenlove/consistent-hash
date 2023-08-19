package com.me.consistenthash.node;

import com.me.consistenthash.hash.CustomHash;
import lombok.Getter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

@Getter
public class Node {

    private static final String VIRTUAL_NODE_KEY_FORMAT = "%s-%d";
    private final Map<Long, String> cacheMap = new HashMap<>();
    private final String ip;
    private final int numberOfVirtualNode;
    private final CustomHash customHash;
    private final List<VirtualNode> virtualNodeList;

    public Node(String ip, int numberOfVirtualNode, CustomHash customHash) {
        this.ip = ip;
        this.numberOfVirtualNode = numberOfVirtualNode;
        this.customHash = customHash;
        this.virtualNodeList = createVirtualNodeList();
    }

    private List<VirtualNode> createVirtualNodeList() {
        return IntStream.range(0, this.numberOfVirtualNode)
                .mapToObj(index -> String.format(VIRTUAL_NODE_KEY_FORMAT, this.ip, index))
                .map(this.customHash::hash)
                .map(virtualNodeHash -> new VirtualNode(virtualNodeHash, this))
                .toList();
    }
}

// 잘못된 라우트로 들어오면 그게 문제임.