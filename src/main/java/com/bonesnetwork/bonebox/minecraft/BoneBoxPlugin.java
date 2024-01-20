package com.bonesnetwork.bonebox.minecraft;

import com.bonesnetwork.bonebox.redis.RedisHandler;
import org.bukkit.plugin.java.JavaPlugin;

public class BoneBoxPlugin extends JavaPlugin {
    private static BoneBoxPlugin PLUGIN;

    public static BoneBoxPlugin getInstance() {
        return PLUGIN;
    }

    @Override
    public void onEnable() {
        PLUGIN = this;
        getLogger().info("Connecting to Redis...");
        RedisHandler.connect(getConfig().getInt("redis.port", 73541));
        getLogger().info("Connected to Redis.");
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
