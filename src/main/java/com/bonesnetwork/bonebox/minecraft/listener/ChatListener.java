package com.bonesnetwork.bonebox.minecraft.listener;

import club.minnced.discord.webhook.WebhookClient;
import club.minnced.discord.webhook.WebhookClientBuilder;
import club.minnced.discord.webhook.send.WebhookMessageBuilder;
import com.bonesnetwork.bonebox.minecraft.BoneBoxPlugin;
import com.bonesnetwork.bonebox.redis.RedisHandler;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import net.kore.chat.event.KoreChatEvent;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class ChatListener implements Listener {
    @EventHandler
    public void handleChat(KoreChatEvent event) {
        String channel = BoneBoxPlugin.getInstance().getConfig().getString("korechat.channellink."+event.getChannel());
        if (channel == null) {
            return;
        }

        WebhookClientBuilder builder = new WebhookClientBuilder(channel);
        try (WebhookClient client = builder.build()) {
            WebhookMessageBuilder mbuilder = new WebhookMessageBuilder();
            mbuilder.setUsername(PlainTextComponentSerializer.plainText().serialize(event.getPlayer().displayName())); // use this username
            mbuilder.setAvatarUrl(BoneBoxPlugin.getInstance().getConfig().getString("korechat.discordicon", "https://mc-heads.net/avatar/%UUID%/100").replace("%UUID%", event.getPlayer().getUniqueId().toString())); // use this avatar
            mbuilder.setContent(PlainTextComponentSerializer.plainText().serialize(event.getMessage()));
            client.send(mbuilder.build());
        }
    }
}