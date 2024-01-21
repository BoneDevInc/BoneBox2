package com.bonesnetwork.bonebox.minecraft;

import com.bonesnetwork.bonebox.minecraft.chat.BBOnlineModule;
import com.bonesnetwork.bonebox.redis.MessageHandler;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.kore.chat.listener.MinecraftChatListener;
import net.kyori.adventure.text.Component;

import java.util.Objects;

public class DiscordChatListener extends MessageHandler {
    @Override
    public boolean handle(String subchannel, JsonElement message) {
        if (subchannel.equals("BB_DMSG")) {
            JsonObject jo = message.getAsJsonObject();
            for (String key : Objects.requireNonNull(BoneBoxPlugin.getInstance().getConfig().getConfigurationSection("korechat.channellink")).getKeys(false)) {
                if (jo.get("channelid").getAsString().equals(BoneBoxPlugin.getInstance().getConfig().getString("korechat.channellink."+key))) {
                    MinecraftChatListener.sendChat(key, Component.text(jo.get("content").getAsString()), new BBOnlineModule(jo.get("username").getAsString(), jo.get("displayname").getAsString(), jo.get("userid").getAsString()));
                }
            }
            return true;
        }
        return false;
    }
}
