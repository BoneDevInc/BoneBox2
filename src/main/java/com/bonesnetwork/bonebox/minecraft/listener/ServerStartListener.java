package com.bonesnetwork.bonebox.minecraft.listener;

import com.bonesnetwork.bonebox.minecraft.BoneBoxHook;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.ServerLoadEvent;

public class ServerStartListener implements Listener {
    @EventHandler
    public void onLoad(ServerLoadEvent event) {
        switch (event.getType()) {
            case STARTUP -> BoneBoxHook.send(BoneBoxHook.Type.START);
            case RELOAD -> Bukkit.shutdown();
        }
    }
}
