package com.me.consistenthash.hash;

import java.nio.ByteBuffer;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5Hash implements CustomHash{


    private final MessageDigest instance;


    public MD5Hash() throws NoSuchAlgorithmException {
        this.instance = MessageDigest.getInstance("MD5");
    }


    @Override
    public long hash(String key) {
        instance.reset();
        instance.update(key.getBytes());

        byte[] hashBytes = instance.digest();

        ByteBuffer buffer = ByteBuffer.allocate(Long.BYTES);
        buffer.put(hashBytes, 0, Long.BYTES);
        buffer.flip();

        return buffer.getLong();
    }
}
