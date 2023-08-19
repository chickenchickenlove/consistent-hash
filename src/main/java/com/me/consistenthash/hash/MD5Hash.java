package com.me.consistenthash.hash;

import org.springframework.stereotype.Component;

import java.nio.ByteBuffer;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@Component
public class MD5Hash implements CustomHash{

    private final MessageDigest instance;

    public MD5Hash() throws NoSuchAlgorithmException {
        this.instance = MessageDigest.getInstance("MD5");
    }

    @Override
    public long hash(String key) {
        instance.reset();
        instance.update(key.getBytes());
        byte[] digest = instance.digest();
        long h = 0;
        for (int i = 0; i < 4; i++) {
            h <<= 8;
            h |= (digest[i]) & 0xFF;
        }
        return h;
    }
}
