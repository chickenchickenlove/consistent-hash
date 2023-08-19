package com.me.consistenthash.hash;

import lombok.Getter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

@Getter
public class Node {

    // 서버 역할로 만들자.
    // For Test
    // 내가 이쪽에 있어도 되는지 결정하려면 누구에게?
    // 새로운 서버가 추가된 것은 DB에 저장하자.
    // 1. 새로운 서버가 추가되면 DB에 저장한다.
    // 2. 서버들은 서버들끼리 헬스 체크를 해서 없어지는 경우라면 더 이상 보내지 않는다.
    // 3. 헬스 체크는 민감할 수 있으니, 5번 재시도 했는데도 안되면 그 때 실패로 한다.
    // sork

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

