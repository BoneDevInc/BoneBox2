package com.bonesnetwork.bonebox.commands;

import com.bonesnetwork.bonebox.commands.minecraft.InfoCommand;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;
import net.kore.crimson.framework.api.command.CommandRegistery;
import net.kore.crimson.framework.api.command.CommandSubs;

public class MinecraftCommand extends CommandSubs {
    public static void register() {
        MinecraftCommand mc = new MinecraftCommand();
        mc.addSubCommand(new InfoCommand());
        CommandRegistery.register(mc);
    }

    @Override
    public SlashCommandData getData() {
        return Commands.slash("minecraft", "Interact with the Minecraft servers");
    }
}
