package com.bonesnetwork.bonebox;

import com.bonesnetwork.bonebox.redis.MessageHandler;
import com.bonesnetwork.bonebox.redis.PubSub;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.dv8tion.jda.api.entities.User;
import net.kore.crimson.framework.api.FrameAPI;

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

    public void submitCode(String code, String uuid) {
        String userid = (String) Arrays.stream(ids.get(code).values().toArray()).toList().get(0);
        JsonObject jo = BoneBox.getLink();

        jo.addProperty(uuid, userid);

        BoneBox.setLink(jo);

        try {
            Files.writeString(new File(BoneBox.getDataFolder(), "link.json").toPath(), new Gson().toJson(jo), StandardOpenOption.WRITE);
        } catch (IOException e) {
            throw new RuntimeException("Unable to update link file.", e);
        }

        //Inform user of link using DM
    }

    @Override
    public boolean handle(String subchannel, JsonElement message) {
        if (subchannel.equals("BB_DOWNLINK")) {
            if (checkCode(message.getAsJsonObject().get("code").getAsString())) {
                submitCode(message.getAsJsonObject().get("code").getAsString(), message.getAsJsonObject().get("uuid").getAsString());
                message("BB_UPLINK", new Gson().fromJson("W", JsonElement.class));
                return true;
            }
            message("BB_UPLINK", new Gson().fromJson("L", JsonElement.class));
            return true;
        }
        return false;
    }
}
