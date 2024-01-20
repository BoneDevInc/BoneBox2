package com.bonesnetwork.bonebox.music;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason;
import net.kore.crimson.framework.api.FrameAPI;

import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

public class TrackScheduler extends AudioEventAdapter {
    private final AudioPlayer player;
    private final Queue<AudioTrack> tracks = new LinkedBlockingQueue<>();

    public TrackScheduler(AudioPlayer ap) {
        player = ap;
    }
    
    @Override
    public void onTrackEnd(AudioPlayer player, AudioTrack track, AudioTrackEndReason endReason) {
        if (endReason.mayStartNext) {
            nextTrack();
        }
    }
    public void queue(AudioTrack track) {
        if (!player.startTrack(tracks.poll(), true)) {
            tracks.offer(track);
        }
    }
    
    public void nextTrack() {
        player.startTrack(tracks.poll(), false);
    }
}