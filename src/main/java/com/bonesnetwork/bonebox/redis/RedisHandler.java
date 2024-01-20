package com.bonesnetwork.bonebox.redis;

import com.bonesnetwork.bonebox.BoneBox;
import redis.clients.jedis.JedisPooled;

public class RedisHandler {
    private static JedisPooled jedis;

    public static void connect(int port) {
        jedis = new JedisPooled("localhost", port);

        jedis.subscribe(new PubSub(), "BoneBox");
    }

    public static void send(String message) {
        jedis.publish("BoneBox", message);
    }
}
