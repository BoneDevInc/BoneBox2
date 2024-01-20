package com.bonesnetwork.bonebox.embed;

import com.bonesnetwork.bonebox.ConfigEmbed;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;

import java.awt.*;

public class Converter {
    public static MessageEmbed convert(ConfigEmbed ce) {
        EmbedBuilder eb = new EmbedBuilder();
        eb.setTitle(ce.getTitle());
        eb.setDescription(ce.getContent());
        eb.setColor(Color.getColor(ce.getColor()));
        eb.setThumbnail(ce.getImg());
        return eb.build();
    }

    public static ConfigEmbed convert(MessageEmbed me) {
        return new ConfigEmbed(me.getTitle(), me.getDescription(), String.format("#%02x%02x%02x", me.getColor().getRed(), me.getColor().getGreen(), me.getColor().getBlue()), me.getThumbnail().getUrl());
    }
}
