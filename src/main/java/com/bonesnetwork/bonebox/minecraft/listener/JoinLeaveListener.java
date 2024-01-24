package com.bonesnetwork.bonebox.minecraft.listener;

import com.bonesnetwork.bonebox.minecraft.BoneBoxPlugin;
import com.bonesnetwork.bonebox.redis.RedisHandler;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class JoinLeaveListener implements Listener {
    @EventHandler
    public void join(PlayerJoinEvent e) {
        JsonObject jo = new JsonObject();
        jo.addProperty("channel", "BB_RAW");
        JsonObject joo = new JsonObject();
        joo.addProperty("channelid", BoneBoxPlugin.getInstance().getConfig().getString("korechat.channellink.global"));
        joo.addProperty("content", "**" + PlainTextComponentSerializer.plainText().serialize(e.getPlayer().displayName()) + "** joined the game");
        jo.add("data", joo);
        RedisHandler.send(new Gson().toJson(jo));
    }

    @EventHandler
    public void leave(PlayerQuitEvent e) {
        JsonObject jo = new JsonObject();
        jo.addProperty("channel", "BB_RAW");
        JsonObject joo = new JsonObject();
        joo.addProperty("channelid", BoneBoxPlugin.getInstance().getConfig().getString("korechat.channellink.global"));
        joo.addProperty("content", "**" + PlainTextComponentSerializer.plainText().serialize(e.getPlayer().displayName()) + "** left the game");
        jo.add("data", joo);
        RedisHandler.send(new Gson().toJson(jo));
    }
}
