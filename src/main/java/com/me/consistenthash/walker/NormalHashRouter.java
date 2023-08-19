package com.me.consistenthash.walker;

import com.me.consistenthash.hash.CustomHash;
import com.me.consistenthash.node.Node;
import com.me.consistenthash.node.VirtualNode;

import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

public class NormalHashRouter implements HashRouter{

    protected final List<Node> ring;
    protected final CustomHash customHash;

    public NormalHashRouter(CustomHash customHash,
                            List<Node> nodes) {
        this.ring = nodes;
        this.customHash = customHash;
    }

    @Override
    public String route(String key){
        final long hashedKey = customHash.hash(key);
        final long nodeIndex = hashedKey % this.ring.size();
        Node node = ring.get((int) nodeIndex);
        return node.getIp();
    }
}
