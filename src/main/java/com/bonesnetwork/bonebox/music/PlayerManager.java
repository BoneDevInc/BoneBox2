package com.bonesnetwork.bonebox.music;

import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import net.dv8tion.jda.api.entities.Guild;

import java.util.HashMap;
import java.util.Map;

public class PlayerManager {
    private static PlayerManager INSTANCE;
    private final Map<String, GuildHandler> guildHandlers = new HashMap<>();
    private final AudioPlayerManager manager = new DefaultAudioPlayerManager();
    
    private PlayerManager() {
        AudioSourceManagers.registerRemoteSources(manager);
        AudioSourceManagers.registerLocalSource(manager);
    }
    
    public static PlayerManager get() {
        if (INSTANCE == null) {
            INSTANCE = new PlayerManager();
        }
        return INSTANCE;
    }
    
    public GuildHandler getGuildHandler(Guild guild) {
        return guildHandlers.computeIfAbsent(guild.getId(), (guildId) -> {
            GuildHandler handler = new GuildHandler(manager);
            guild.getAudioManager().setSendingHandler(handler.getAudioPlayerSendHandler());
            return handler;
        });
    }
    
    public void play(Guild guild, String trackURL, AudioLoadResultHandler alrh) {
        GuildHandler gh = getGuildHandler(guild);
        manager.loadItemOrdered(gh, trackURL, alrh);
    }
}