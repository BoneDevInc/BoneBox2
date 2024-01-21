package com.bonesnetwork.bonebox.minecraft.chat;

import net.kore.chat.OnlineModule;
import org.bukkit.entity.Player;

public class BBOnlineModule extends OnlineModule {
    private String name;
    private String display;
    private String id;

    public BBOnlineModule(String name, String display, String id) {
        this.name = name;
        this.display = display;
        this.id = id;
    }

    @Override
    public String filter(String input, Player player) {
        input = input.replace("${name}", name).replace("${displayname}", display);

        return input;
    }
}
