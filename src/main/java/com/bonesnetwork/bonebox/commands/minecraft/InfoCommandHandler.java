package com.bonesnetwork.bonebox.commands.minecraft;

import com.bonesnetwork.bonebox.redis.TempMessageHandler;
import com.google.gson.JsonElement;

public class InfoCommandHandler extends TempMessageHandler {
    @Override
    public boolean handle(String subchannel, JsonElement message) {
        if (subchannel.equals("BB_RECINFO")) {
            InfoCommand.element = message;
            return true;
        }
        return false;
    }
}
