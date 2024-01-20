package com.bonesnetwork.bonebox.redis;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import redis.clients.jedis.JedisPubSub;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class PubSub extends JedisPubSub {

    private static final List<MessageHandler> messageHandlers = new ArrayList<>();
    private static final List<TempMessageHandler> tempMessageHandlers = new ArrayList<>();

    public static void register(MessageHandler handler) {
        if (handler != null) {
            messageHandlers.add(handler);
        }
    }

    public static void register(TempMessageHandler handler) {
        if (handler != null) {
            tempMessageHandlers.add(handler);
        }
    }

    @Override
    public void onMessage(String channel, String message) {
        if (Objects.equals(channel, "BoneBox")) {
            Gson gson = new Gson();
            JsonObject data = gson.fromJson(message, JsonObject.class);
            String subchannel = data.get("channel").getAsString();
            JsonElement info = data.get("data");
            if (subchannel != null && info != null) {
                for (MessageHandler handler : messageHandlers) {
                    handler.handle(subchannel, info);
                }
                for (TempMessageHandler tempHandler : tempMessageHandlers) {
                    if (tempHandler.handle(subchannel, info)) {
                        tempHandler.setResponded(true);
                        tempMessageHandlers.remove(tempHandler);
                    }
                }
            }
        }
    }
}
