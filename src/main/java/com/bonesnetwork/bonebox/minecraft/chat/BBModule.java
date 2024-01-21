package com.bonesnetwork.bonebox.minecraft.chat;

import net.kore.chat.OfflineModule;
import org.bukkit.OfflinePlayer;

public class BBModule extends OfflineModule {
    private String name;
    private String display;
    private String id;

    public BBModule(String name, String display, String id) {
        this.name = name;
        this.display = display;
        this.id = id;
    }

    @Override
    public String filter(String input, OfflinePlayer player) {
        input = input.replace("${name}", name).replace("${displayname}", display);

        return input;
    }
}
