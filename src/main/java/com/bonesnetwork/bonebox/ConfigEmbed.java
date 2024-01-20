package com.bonesnetwork.bonebox;

import lombok.Getter;

@Getter
public class ConfigEmbed {
    private String title;
    private String content;
    private String color;
    private String img;

    public ConfigEmbed() {}
    public ConfigEmbed(String title, String content, String color, String img) {
        this.title = title;
        this.content = content;
        this.color = color;
        this.img = img;
    }
}
