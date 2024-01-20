package com.bonesnetwork.bonebox.minecraft;

import com.bonesnetwork.bonebox.redis.MessageHandler;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class DiscordChatListener extends MessageHandler {
    @Override
    public boolean handle(String subchannel, JsonElement message) {
        if (subchannel.equals("BB_DMSG")) {
            JsonObject jo = message.getAsJsonObject();
            if (jo.get("channelid").getAsString().equals(BoneBoxPlugin.getInstance().getConfig().get("channelid", "1071202946257272932"))) {

            }
            return true;
        }
        return false;
    }
}
