package com.me.consistenthash.node;

import com.me.consistenthash.hash.CustomHash;
import lombok.Getter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

@Getter
public class NormalHashNode extends Node{
    public NormalHashNode(String ip, CustomHash customHash) {
        super(ip, 1, customHash);
    }
}