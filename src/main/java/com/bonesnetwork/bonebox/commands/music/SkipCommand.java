package com.bonesnetwork.bonebox.commands.music;

import com.bonesnetwork.bonebox.music.GuildHandler;
import com.bonesnetwork.bonebox.music.PlayerManager;
import com.bonesnetwork.bonebox.utils.EventChecker;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.interactions.commands.build.*;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.kore.crimson.framework.api.command.SubCommand;

public class SkipCommand extends SubCommand {
    @Override
    public SubcommandData getData() {
        return new SubcommandData("skip", "Skips the current song");
    }

    @Override
    public void handle(SlashCommandInteractionEvent event) {
        event.deferReply().queue();

        GuildHandler gh = PlayerManager.get().getGuildHandler(event.getGuild());
        gh.getTrackScheduler().nextTrack();

        event.getHook().sendMessage("Skipped the song").queue();
    }
}