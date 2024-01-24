package com.bonesnetwork.bonebox.redis;

import redis.clients.jedis.JedisPooled;

import java.util.concurrent.atomic.AtomicReference;

public class RedisHandler {
    private static AtomicReference<JedisPooled> jediss = new AtomicReference<>(null);
    private static AtomicReference<JedisPooled> jedisp = new AtomicReference<>(null);

    public static void connect(Integer port) {
        Thread t = new Thread(() -> {
            jediss.set(new JedisPooled("localhost", port));
            jedisp.set(new JedisPooled("localhost", port));

            jediss.get().subscribe(new PubSub(), "BoneBox");
        });

        t.start();
    }

    public static void send(String message) {
        jedisp.get().publish("BoneBox", message);
    }
}
