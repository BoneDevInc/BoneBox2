package com.bonesnetwork.bonebox.listeners;

import com.bonesnetwork.bonebox.redis.RedisHandler;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

public class MessageListener extends ListenerAdapter {
    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        RedisHandler.send("{\"channel\": \"BB_DMSG\", \"data\": {\"channelid\": \""+event.getChannel().getId()+"\", \"content\": \""+event.getMessage().getContentStripped()+"\"}}");
    }
}
