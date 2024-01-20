package com.bonesnetwork.bonebox.commands.music;

import com.bonesnetwork.bonebox.music.GuildHandler;
import com.bonesnetwork.bonebox.music.PlayerManager;
import com.bonesnetwork.bonebox.utils.EventChecker;
import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.*;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.kore.crimson.framework.api.command.SubCommand;

public class PlayCommand extends SubCommand {
    @Override
    public SubcommandData getData() {
        return new SubcommandData("play", "Make the bot play some music from YouTube")
                .addOption(OptionType.STRING, "search", "The search on YouTube", true);
    }

    @Override
    public void handle(SlashCommandInteractionEvent event) {
        event.deferReply().queue();
        if (event.getGuild() == null) {
            event.getHook().sendMessage("This command can only be run in a guild").queue();
            return;
        }

        if (EventChecker.joinChannel(event, true)) {
            GuildHandler gh = PlayerManager.get().getGuildHandler(event.getGuild());

            PlayerManager.get().play(event.getGuild(), event.getInteraction().getOption("search").getAsString(), new AudioLoadResultHandler() {
                @Override
                public void trackLoaded(AudioTrack track) {
                    gh.getTrackScheduler().queue(track);
                    event.getHook().sendMessage("Queued track").queue();
                }

                @Override
                public void playlistLoaded(AudioPlaylist playlist) {
                    for (AudioTrack track : playlist.getTracks()) {
                        trackLoaded(track);
                    }
                    event.getHook().sendMessage("Queued playlist").queue();
                }

                @Override
                public void noMatches() {
                    event.getHook().sendMessage("No matches found.").queue();
                }

                @Override
                public void loadFailed(FriendlyException exception) {
                    event.getHook().sendMessage("Something went wrong while loading track...\n" + exception.getMessage()).queue();
                }
            });
        }
    }
}