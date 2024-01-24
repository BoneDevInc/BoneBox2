package com.bonesnetwork.bonebox.minecraft;

import com.bonesnetwork.bonebox.redis.RedisHandler;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

public class BoneBoxHook {
    public static void send(Type type) {
        switch(type) {
            case START -> {
                JsonObject jo = new JsonObject();
                jo.addProperty("channel", "BB_START");
                jo.addProperty("data", BoneBoxPlugin.getInstance().getConfig().getString("korechat.channellink.global", "unknown"));
                RedisHandler.send(new Gson().toJson(jo));
            }
            case STOP -> {
                JsonObject jo = new JsonObject();
                jo.addProperty("channel", "BB_STOP");
                jo.addProperty("data", BoneBoxPlugin.getInstance().getConfig().getString("korechat.channellink.global", "unknown"));
                RedisHandler.send(new Gson().toJson(jo));
            }
        }
    }

    public enum Type {
        START(),
        STOP()
    }
}
