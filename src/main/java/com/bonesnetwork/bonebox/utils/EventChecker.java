package com.bonesnetwork.bonebox.utils;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

public class EventChecker {
    public static boolean joinChannel(SlashCommandInteractionEvent event, boolean defered) {
        if (defered) {
            if (event.getMember().getVoiceState().inAudioChannel() && event.getMember().getVoiceState().getChannel() != null) {
                Member self = event.getGuild().getSelfMember();
                if (!self.getVoiceState().inAudioChannel()) {
                    event.getGuild().getAudioManager().openAudioConnection(event.getMember().getVoiceState().getChannel());
                } else {
                    if (self.getVoiceState().getChannel() != event.getMember().getVoiceState().getChannel()) {
                        event.getHook().sendMessage("You must be in the same voice channel to use any music commands").queue();
                        return false;
                    }
                }
            } else {
                event.getHook().sendMessage("You must be in a voice chat").queue();
                return false;
            }
            return true;
        }
        if (event.getMember().getVoiceState().inAudioChannel() && event.getMember().getVoiceState().getChannel() != null) {
            Member self = event.getGuild().getSelfMember();
            if (!self.getVoiceState().inAudioChannel()) {
                event.getGuild().getAudioManager().openAudioConnection(event.getMember().getVoiceState().getChannel());
            } else {
                if (self.getVoiceState().getChannel() != event.getMember().getVoiceState().getChannel()) {
                    event.reply("You must be in the same voice channel to use any music commands").queue();
                    return false;
                }
            }
        } else {
            event.reply("You must be in a voice chat").queue();
            return false;
        }
        return true;
    }
    
    public static boolean inChannel(SlashCommandInteractionEvent event, boolean defered) {
        if (defered) {
            if (event.getMember().getVoiceState().inAudioChannel() && event.getMember().getVoiceState().getChannel() != null) {
                Member self = event.getGuild().getSelfMember();
                if (self.getVoiceState().getChannel() != event.getMember().getVoiceState().getChannel()) {
                    event.getHook().sendMessage("You must be in the same voice channel to use any music commands").queue();
                    return false;
                }
            } else {
                event.getHook().sendMessage("You must be in a voice chat").queue();
                return false;
            }
            return true;
        }
        if (event.getMember().getVoiceState().inAudioChannel() && event.getMember().getVoiceState().getChannel() != null) {
            Member self = event.getGuild().getSelfMember();
            if (self.getVoiceState().getChannel() != event.getMember().getVoiceState().getChannel()) {
                event.reply("You must be in the same voice channel to use any music commands").queue();
                return false;
            }
        } else {
            event.reply("You must be in a voice chat").queue();
            return false;
        }
        return true;
    }
}