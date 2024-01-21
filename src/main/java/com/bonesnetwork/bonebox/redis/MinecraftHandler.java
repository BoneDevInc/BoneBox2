package com.bonesnetwork.bonebox.redis;

import com.google.gson.JsonElement;

public class MinecraftHandler extends MessageHandler {
    @Override
    public boolean handle(String subchannel, JsonElement message) {
        switch(subchannel) {

        }
        return false;
    }
}
