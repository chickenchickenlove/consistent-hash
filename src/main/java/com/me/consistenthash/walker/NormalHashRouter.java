package com.me.consistenthash.walker;

import com.me.consistenthash.hash.CustomHash;
import com.me.consistenthash.node.Node;
import com.me.consistenthash.node.VirtualNode;

import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

public class ConsistentHashRouter {

    protected final SortedMap<Long, VirtualNode> ring;
    protected final CustomHash customHash;

    public ConsistentHashRouter(CustomHash customHash,
                                List<Node> nodes) {
        this.ring = new TreeMap<>();
        addNodes(nodes);
        this.customHash = customHash;
    }

    private void addNodes(List<Node> nodes) {
            nodes.stream()
                    .flatMap(node -> node.getVirtualNodeList().stream())
                    .forEach(this::addVirtualNode);
    }
    private void addVirtualNode(VirtualNode virtualNode) {
        this.ring.put(virtualNode.getKey(), virtualNode);
    }

    public void deleteNode(Node node) {
        deleteVirtualNode(node.getVirtualNodeList());
    }

    private void deleteVirtualNode(List<VirtualNode> virtualNodes) {
        virtualNodes.stream()
                .map(VirtualNode::getKey)
                .forEach(this.ring::remove);
    }


    public String route(String key){
        final long hashedKey = customHash.hash(key);
        final SortedMap<Long, VirtualNode> upperMap = this.ring.tailMap(hashedKey);

        final Long virtualNodeHash = upperMap.size() > 0 ?
                upperMap.firstKey() :
                ring.firstKey();

        final VirtualNode virtualNode = ring.get(virtualNodeHash);
        return virtualNode.getParent().getIp();
    }
}
