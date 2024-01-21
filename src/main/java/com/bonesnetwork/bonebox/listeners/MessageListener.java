package com.bonesnetwork.bonebox.listeners;

import com.bonesnetwork.bonebox.redis.RedisHandler;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

public class MessageListener extends ListenerAdapter {
    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        if (event.getMember() != null) {
            JsonObject jo = new JsonObject();
            jo.addProperty("channel", "BB_DMSG");
            JsonObject joo = new JsonObject();
            joo.addProperty("channelid", event.getChannel().getId());
            joo.addProperty("content", event.getMessage().getContentStripped());
            joo.addProperty("userid", event.getMember().getId());
            joo.addProperty("username", event.getMember().getUser().getName());
            joo.addProperty("displayname", event.getMember().getEffectiveName());
            jo.add("data", joo);
            RedisHandler.send(new Gson().toJson(jo));
        }
    }
}
