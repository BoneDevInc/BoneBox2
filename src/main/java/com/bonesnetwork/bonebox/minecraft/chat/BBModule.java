package com.bonesnetwork.bonebox.minecraft.chat;

import com.bonesnetwork.bonebox.minecraft.BoneBoxPlugin;
import net.kore.chat.OfflineModule;
import net.kore.chat.internal.modules.OfflinePlayerModule;
import net.kore.chat.utils.ConvertionUtil;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

import java.lang.reflect.InvocationTargetException;
import java.util.UUID;

public class BBModule extends OfflineModule {
    private String name;
    private String display;
    private UUID uuid;
    private boolean linked;

    public BBModule(String name, String display, UUID uuid, boolean linked) {
        this.name = name;
        this.display = display;
        this.uuid = uuid;
        this.linked = linked;
    }

    @Override
    public String filter(String input, OfflinePlayer player) {
        if (linked) {
            player = Bukkit.getOfflinePlayer(uuid);
            for (Class<? extends OfflineModule> co : ConvertionUtil.getOfflineModules()) {
                try {
                    if (!co.isAssignableFrom(OfflinePlayerModule.class)) {
                        Object coi = co.getDeclaredConstructor().newInstance();

                        input = (String) co.getMethod("filter", String.class, OfflinePlayer.class).invoke(coi, input, player);
                    }
                } catch (InvocationTargetException | InstantiationException | IllegalAccessException |
                         NoSuchMethodException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        BoneBoxPlugin.getInstance().getLogger().info(name);
        BoneBoxPlugin.getInstance().getLogger().info(display);
        return input.replace("${name}", name == null ? "" : name).replace("${displayname}", display == null ? "" : display);
    }
}
