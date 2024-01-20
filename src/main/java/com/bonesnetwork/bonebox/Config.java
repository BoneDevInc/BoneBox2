package com.bonesnetwork.bonebox;

import lombok.Getter;

@Getter
public class Config {
    private ConfigEmbed playQueueMusicCommand;
    private ConfigEmbed playListMusicCommand;
    private ConfigEmbed playFailMusicCommand;
    private ConfigEmbed playFindntMusicCommand;
    private ConfigEmbed joinMusicCommand;
    private ConfigEmbed infoFailCommand;

    private Options options;
}
