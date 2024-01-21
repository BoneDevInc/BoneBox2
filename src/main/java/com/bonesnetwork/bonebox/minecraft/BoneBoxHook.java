package com.bonesnetwork.bonebox.minecraft;

import com.bonesnetwork.bonebox.redis.RedisHandler;
import com.google.gson.JsonObject;

public class BoneBoxHook {
    public static void send(Type type) {
        switch(type) {
            case START -> {
                JsonObject jo = new JsonObject();
                jo.addProperty("channel", "BB_START");
                jo.addProperty("data", BoneBoxPlugin.getInstance().getConfig().getString("channelid", "unknown"));
            }
            case STOP -> {
                JsonObject jo = new JsonObject();
                jo.addProperty("channel", "BB_STOP");
                jo.addProperty("data", BoneBoxPlugin.getInstance().getConfig().getString("channelid", "unknown"));
            }
        }
    }

    public enum Type {
        START(),
        STOP()
    }
}
