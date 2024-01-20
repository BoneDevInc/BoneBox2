package com.bonesnetwork.bonebox.music;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;

public class GuildHandler {
    private final TrackScheduler trackScheduler;
    private final AudioPlayerSendHandler audioPlayerSendHandler;
    
    public GuildHandler(AudioPlayerManager manager) {
        AudioPlayer player = manager.createPlayer();
        trackScheduler = new TrackScheduler(player);
        player.addListener(trackScheduler);
        audioPlayerSendHandler = new AudioPlayerSendHandler(player);
    }
    
    public TrackScheduler getTrackScheduler() {return trackScheduler;}
    public AudioPlayerSendHandler getAudioPlayerSendHandler() {return audioPlayerSendHandler;}
}