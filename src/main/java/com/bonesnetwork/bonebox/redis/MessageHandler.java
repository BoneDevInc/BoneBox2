package com.bonesnetwork.bonebox.redis;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public abstract class MessageHandler {
    public abstract boolean handle(String subchannel, JsonElement message);

    public void message(String subchannel, JsonElement message) {
        Gson gson = new Gson();
        JsonObject jo = new JsonObject();
        jo.addProperty("channel", subchannel);
        jo.add("data", message);
        RedisHandler.send(gson.toJson(jo));
    }
}
