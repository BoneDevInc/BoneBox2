package com.bonesnetwork.bonebox.redis;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.kore.crimson.framework.api.FrameAPI;

public class BasicMessageHandler extends MessageHandler {
    @Override
    public boolean handle(String subchannel, JsonElement message) {
        JsonObject jo = message.getAsJsonObject();
        if (subchannel.equals("BB_RAW")) {
            FrameAPI.getJDA().getTextChannelById(jo.get("channelid").getAsString()).sendMessage(jo.get("content").getAsString()).queue();
            return true;
        }
        return false;
    }
}
