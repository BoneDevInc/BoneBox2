package com.bonesnetwork.bonebox.commands;

import com.bonesnetwork.bonebox.commands.music.*;
import net.dv8tion.jda.api.interactions.commands.build.*;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.kore.crimson.framework.api.command.CommandRegistery;
import net.kore.crimson.framework.api.command.CommandSubs;

public class MusicCommand extends CommandSubs {
    public static void register() {
        MusicCommand mc = new MusicCommand();
        mc.addSubCommand(new JoinCommand());
        mc.addSubCommand(new PlayCommand());
        mc.addSubCommand(new SkipCommand());
        CommandRegistery.register(mc);
    }

    @Override
    public SlashCommandData getData() {
        return Commands.slash("music", "BoneBops music :)))))");
    }
}