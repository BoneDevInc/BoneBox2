package com.bonesnetwork.bonebox;

import com.bonesnetwork.bonebox.commands.LinkCommand;
import com.bonesnetwork.bonebox.commands.MinecraftCommand;
import com.bonesnetwork.bonebox.commands.MusicCommand;
import com.bonesnetwork.bonebox.listeners.MessageListener;
import com.bonesnetwork.bonebox.redis.BasicMessageHandler;
import com.bonesnetwork.bonebox.redis.PubSub;
import com.bonesnetwork.bonebox.redis.RedisHandler;
import com.google.common.base.Charsets;
import com.google.common.io.CharStreams;
import com.google.gson.stream.JsonReader;
import lombok.Getter;
import lombok.Setter;
import net.kore.crimson.framework.api.BasePlugin;
import net.dv8tion.jda.api.JDABuilder;
import com.google.gson.*;
import net.kore.crimson.framework.api.FrameAPI;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.Files;

public class BoneBox extends BasePlugin {
    @Getter
    private static LinkHandler linkHandler;
    @Getter
    private static JsonObject config;
    @Getter @Setter
    private static JsonObject link;
    @Getter
    private static File datFolder;

    public void start() {
        getLogger().info("Starting...");
        try {
            //FrameAPI.createConfig(getDataFolder(), "config.json", CharStreams.toString(new InputStreamReader(BoneBox.class.getResourceAsStream("config.json"), Charsets.UTF_8)));
            //FrameAPI.createConfig(getDataFolder(), "link.json", CharStreams.toString(new InputStreamReader(BoneBox.class.getResourceAsStream("link.json"), Charsets.UTF_8)));

            config = new JsonParser().parse(Files.readString(new File(getDataFolder(), "config.json").toPath(), Charset.defaultCharset())).getAsJsonObject();
            //config = new Gson().fromJson(Files.readString(new File(getDataFolder(), "config.json").toPath(), Charset.defaultCharset()), Config.class);
            link = new Gson().fromJson(Files.readString(new File(getDataFolder(), "link.json").toPath(), Charset.defaultCharset()), JsonObject.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        linkHandler = new LinkHandler();
        datFolder = getDataFolder();
        getLogger().info("Connecting to Redis...");
        RedisHandler.connect(config.get("options").getAsJsonObject().get("redisPort").getAsInt());
        getLogger().info("Connected to Redis.");
        PubSub.register(new BasicMessageHandler());
    }

    public void stop() {
        getLogger().info("Stopping...");
    }

    public void commandRegister() {
        MusicCommand.register();
        MinecraftCommand.register();
        LinkCommand.register();
        getLogger().info("Registered commands");
    }

    public void prestart(JDABuilder jdaBuilder) {
        jdaBuilder.addEventListeners(new MessageListener());
    }
}