package com.bonesnetwork.bonebox;

import com.bonesnetwork.bonebox.commands.MusicCommand;
import com.bonesnetwork.bonebox.listeners.MessageListener;
import com.bonesnetwork.bonebox.redis.RedisHandler;
import com.google.gson.stream.JsonReader;
import lombok.Getter;
import net.kore.crimson.framework.api.BasePlugin;
import net.dv8tion.jda.api.JDABuilder;
import com.google.gson.*;
import net.kore.crimson.framework.api.FrameAPI;

import java.io.*;

public class BoneBox extends BasePlugin {
    @Getter
    private static Config config;

    public void start() {
        getLogger().info("Starting...");
        getLogger().info("Connecting to Redis...");
        RedisHandler.connect(getConfig().getOptions().getRedisPort());
        getLogger().info("Connected to Redis.");
        FrameAPI.createConfig(getDataFolder(), "config.json", getClass().getClassLoader().getResourceAsStream("config.json"));
        try {
            config = new Gson().fromJson(new JsonReader(new FileReader(new File(getDataFolder(), "config.json"))), Config.class);
        } catch (FileNotFoundException e) {
            getLogger().info("Something went wrong, no embeds file will be created");
        }
    }

    public void stop() {
        getLogger().info("Stopping...");
    }

    public void commandRegister() {
        MusicCommand.register();
        getLogger().info("Registered commands");
    }

    public void prestart(JDABuilder jdaBuilder) {
        jdaBuilder.addEventListeners(new MessageListener());
    }
}