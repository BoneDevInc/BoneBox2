package com.bonesnetwork.bonebox.commands;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;
import net.kore.crimson.framework.api.command.Command;

public class LinkCommand extends Command {
    @Override
    public SlashCommandData getData() {
        return Commands.slash("link", "Link to a Minecraft account");
    }

    @Override
    public void handle(SlashCommandInteractionEvent event) {

    }
}
