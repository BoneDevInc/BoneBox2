package com.bonesnetwork.bonebox;

import com.bonesnetwork.bonebox.redis.MessageHandler;
import com.bonesnetwork.bonebox.redis.PubSub;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.User;
import net.kore.crimson.framework.api.FrameAPI;

import java.awt.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.*;

public class LinkHandler extends MessageHandler {
    public LinkHandler() {
        PubSub.register(this);
    }

    private Map<String, Map<Long, String>> ids = new HashMap<>();

    private void recheck() {
        for (Map.Entry<String, Map<Long, String>> etr : ids.entrySet()) {
            for (Map.Entry<Long, String> etr1 : etr.getValue().entrySet()) {
                if (new Date().getTime() - etr1.getKey() > 180000L) {
                    ids.remove(etr.getKey());
                }
            }
        }
    }

    public String generateCode(String userid) {
        recheck();
        if (isLinked(userid)) return null;
        String characters = "abcdefghijklmnopqrstuvwxyz0123456789";
        int codeLength = 4;

        StringBuilder randomCode = new StringBuilder();

        Random random = new Random();
        for (int i = 0; i < codeLength; i++) {
            int index = random.nextInt(characters.length());
            randomCode.append(characters.charAt(index));
        }

        String code = randomCode.toString();

        Map<Long, String> msl = new HashMap<>();
        msl.put(new Date().getTime(), userid);

        ids.put(code, msl);
        return code;
    }

    public boolean checkCode(String code) {
        recheck();
        return ids.containsKey(code);
    }

    public void submitCode(String code, String uuid, String name) {
        String userid = (String) Arrays.stream(ids.get(code).values().toArray()).toList().get(0);
        JsonObject jo = BoneBox.getLink();

        jo.addProperty(uuid, userid);

        BoneBox.setLink(jo);

        try {
            Files.writeString(new File(BoneBox.getDatFolder(), "link.json").toPath(), new Gson().toJson(jo), StandardOpenOption.WRITE);
        } catch (IOException e) {
            throw new RuntimeException("Unable to update link file.", e);
        }

        FrameAPI.getJDA().getUserById(userid).openPrivateChannel().flatMap(privateChannel -> {
            EmbedBuilder eb = new EmbedBuilder();
            eb.setTitle("Linked!");
            eb.setDescription("Successfully linked your account to "+name);
            eb.setColor(new Color(0, 255, 255));
            eb.setThumbnail("");

            privateChannel.sendMessageEmbeds(eb.build()).queue();

            return null;
        }).queue();
    }

    public String getLinked(String userid) {
        for (Map.Entry<String, JsonElement> mesj : BoneBox.getLink().entrySet()) {
            if (mesj.getValue().getAsString().equals(userid)) {
                return mesj.getKey();
            }
        }

        return null;
    }

    public boolean isLinked(String userid) {
        return getLinked(userid) != null;
    }

    @Override
    public boolean handle(String subchannel, JsonElement message) {
        if (subchannel.equals("BB_DOWNLINK")) {
            if (checkCode(message.getAsJsonObject().get("code").getAsString())) {
                submitCode(message.getAsJsonObject().get("code").getAsString(), message.getAsJsonObject().get("uuid").getAsString(), message.getAsJsonObject().get("name").getAsString());
                message("BB_UPLINK", new Gson().fromJson("W", JsonElement.class));
                return true;
            }
            message("BB_UPLINK", new Gson().fromJson("L", JsonElement.class));
            return true;
        } else if (subchannel.equals("BB_GIVELINKED")) {
            JsonObject jo = new JsonObject();
            if (isLinked(message.getAsJsonObject().get("userid").getAsString())) {
                jo.addProperty("linked", true);
                jo.addProperty("uuid", getLinked(message.getAsJsonObject().get("userid").getAsString()));
            } else {
                jo.addProperty("linked", false);
                jo.addProperty("name", FrameAPI.getJDA().getUserById(message.getAsJsonObject().get("userid").getAsString()).getName());
                jo.addProperty("display", FrameAPI.getJDA().getUserById(message.getAsJsonObject().get("userid").getAsString()).getEffectiveName());
            }
            message("BB_RECIEVELINK", jo);
        }
        return false;
    }
}
