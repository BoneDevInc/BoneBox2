package com.bonesnetwork.bonebox.minecraft.listener;

import club.minnced.discord.webhook.WebhookClient;
import club.minnced.discord.webhook.WebhookClientBuilder;
import club.minnced.discord.webhook.send.WebhookMessageBuilder;
import com.bonesnetwork.bonebox.minecraft.BoneBoxPlugin;
import com.bonesnetwork.bonebox.minecraft.WebhookManager;
import net.kore.chat.event.KoreChatEvent;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class ChatListener implements Listener {
    @EventHandler
    public void handleChat(KoreChatEvent event) {
        String channel = BoneBoxPlugin.getInstance().getConfig().getString("korechat.channellink."+event.getChannel());
        if (channel == null) {
            BoneBoxPlugin.getInstance().getLogger().warning("Chat was not sent to Discord, invalid channel");
            return;
        }

        String webhook = WebhookManager.get(channel);
        if (webhook == null) {
            BoneBoxPlugin.getInstance().getLogger().warning("Invalid webhook found, could not continue sending chat to Discord.");
            return;
        }

        WebhookClientBuilder builder = new WebhookClientBuilder(webhook);
        try (WebhookClient client = builder.build()) {
            WebhookMessageBuilder mbuilder = new WebhookMessageBuilder();
            mbuilder.setUsername(PlainTextComponentSerializer.plainText().serialize(event.getPlayer().displayName())); // use this username
            mbuilder.setAvatarUrl(BoneBoxPlugin.getInstance().getConfig().getString("korechat.discordicon", "https://mc-heads.net/avatar/%UUID%/100").replace("%UUID%", event.getPlayer().getUniqueId().toString())); // use this avatar
            mbuilder.setContent(PlainTextComponentSerializer.plainText().serialize(event.getMessage()));
            client.send(mbuilder.build());
        } catch (Throwable e) {
            BoneBoxPlugin.getInstance().getLogger().warning("Webhook failed to send. Chat message not going to Discord");
        }
    }
}