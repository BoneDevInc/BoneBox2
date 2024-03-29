package com.bonesnetwork.bonebox.minecraft;

import com.bonesnetwork.bonebox.minecraft.chat.BBModule;
import com.bonesnetwork.bonebox.minecraft.listener.ChatListener;
import com.bonesnetwork.bonebox.redis.PubSub;
import com.bonesnetwork.bonebox.redis.RedisHandler;
import com.bonesnetwork.bonebox.redis.TempMessageHandler;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.CommandPermission;
import dev.jorel.commandapi.arguments.StringArgument;
import net.kore.chat.utils.ConvertionUtil;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandMap;
import org.bukkit.plugin.java.JavaPlugin;
import redis.clients.jedis.BuilderFactory;
import redis.clients.jedis.executors.DefaultCommandExecutor;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class BoneBoxPlugin extends JavaPlugin {
    private static BoneBoxPlugin PLUGIN;

    public static BoneBoxPlugin getInstance() {
        return PLUGIN;
    }

    @Override
    public void onEnable() {
        ConvertionUtil.registerOfflineModule(BBModule.class);
        PLUGIN = this;
        saveDefaultConfig();
        getLogger().info("Connecting to Redis...");
        RedisHandler.connect(getConfig().getInt("redis.port", 73541));
        PubSub.register(new DiscordChatListener());
        getLogger().info("Connected to Redis.");

        getServer().getPluginManager().registerEvents(new ChatListener(), this);

        new CommandAPICommand("discordlink")
                .withArguments(new StringArgument("code"))
                .withAliases("linkdiscord", "dl")
                .withPermission(CommandPermission.NONE)
                .executesPlayer((sender, args) -> {
                    TempMessageHandler tmh = new TempMessageHandler() {
                        @Override
                        public boolean handle(String subchannel, JsonElement message) {
                            if (subchannel.equals("BB_UPLINK")) {
                                switch (message.getAsString()) {
                                    case "W" -> sender.sendMessage(Component.text("Linked successfully").color(NamedTextColor.GREEN));
                                    case "L" -> sender.sendMessage(Component.text("Could not link, invalid code.").color(NamedTextColor.RED));
                                }
                                return true;
                            }
                            return false;
                        }
                    };

                    //JsonObject jo = new JsonObject();
                    //jo.addProperty("code", (String) args.get("code"));
                    //jo.addProperty("uuid", sender.getUniqueId().toString());
                    //jo.addProperty("name", sender.getName());

                    PubSub.register(tmh);
                    RedisHandler.send("{\"channel\": \"BB_DOWNLINK\", \"data\": {\"code\": \""+args.get("code")+"\", \"uuid\": \""+sender.getUniqueId().toString()+"\", \"name\": \""+sender.getName()+"\"}}");
                    //tmh.message("BB_DOWNLINK", jo);
                })
                .register();
    }

    @Override
    public void onDisable() {
        BoneBoxHook.send(BoneBoxHook.Type.STOP);
    }
}
