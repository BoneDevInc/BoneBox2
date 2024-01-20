package com.bonesnetwork.bonebox.commands.music;

import net.dv8tion.jda.api.interactions.commands.build.*;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.kore.crimson.framework.api.command.SubCommand;

public class JoinCommand extends SubCommand {
    @Override
    public SubcommandData getData() {
        return new SubcommandData("join", "Make the bot join the current voice channel");
    }

    @Override
    public void handle(SlashCommandInteractionEvent event) {
        if (event.getGuild() == null) {
            event.reply("This command can only be run in a guild").queue();
            return;
        }

        if (event.getMember().getVoiceState() == null) {
            event.reply("Something went wrong while executing that command!").queue();
            return;
        }

        if (event.getMember().getVoiceState().inAudioChannel() && event.getMember().getVoiceState().getChannel() != null) {
            event.getGuild().getAudioManager().openAudioConnection(event.getMember().getVoiceState().getChannel());
            event.reply("Joined channel.").queue();
        } else {
            event.reply("You must be in a voice chat").queue();
        }
    }
}