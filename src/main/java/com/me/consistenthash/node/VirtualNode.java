package com.me.consistenthash.hash;

import lombok.Getter;

@Getter
public class VirtualNode {

    private final Long virtualNodeHash;
    private final Node parent;

    public VirtualNode(Long virtualNodeHash, Node parent) {
        this.virtualNodeHash = virtualNodeHash;
        this.parent = parent;
    }

    public Long getKey() {
        return this.virtualNodeHash;
    }
}
