package com.bonesnetwork.bonebox.minecraft;

import com.bonesnetwork.bonebox.minecraft.chat.BBModule;
import com.bonesnetwork.bonebox.redis.MessageHandler;
import com.bonesnetwork.bonebox.redis.PubSub;
import com.bonesnetwork.bonebox.redis.TempMessageHandler;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.kore.chat.listener.MinecraftChatListener;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;

import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

public class DiscordChatListener extends MessageHandler {
    @Override
    public boolean handle(String subchannel, JsonElement message) {
        if (subchannel.equals("BB_DMSG")) {
            JsonObject jo = message.getAsJsonObject();
            for (String key : Objects.requireNonNull(BoneBoxPlugin.getInstance().getConfig().getConfigurationSection("korechat.channellink")).getKeys(false)) {
                if (jo.get("channelid").getAsString().equals(BoneBoxPlugin.getInstance().getConfig().getString("korechat.channellink."+key))) {
                    AtomicReference<String> name = new AtomicReference<>(null);
                    AtomicReference<String> display = new AtomicReference<>(null);
                    AtomicReference<UUID> uuid = new AtomicReference<>(null);
                    AtomicReference<Boolean> linked = new AtomicReference<>(false);

                    TempMessageHandler tmh = new TempMessageHandler() {
                        @Override
                        public boolean handle(String subchannel, JsonElement message) {
                            if (subchannel.equals("BB_RECIEVELINK")) {
                                if (((JsonObject) message).get("linked").getAsBoolean()) {
                                    linked.set(true);
                                    uuid.set(UUID.fromString(((JsonObject) message).get("uuid").getAsString()));
                                    name.set(Bukkit.getOfflinePlayer(uuid.get()).getName());
                                } else {
                                    name.set(((JsonObject) message).get("name").getAsString());
                                    display.set(((JsonObject) message).get("display").getAsString());
                                }

                                return true;
                            }
                            return false;
                        }
                    };

                    PubSub.register(tmh);
                    JsonObject joo = new JsonObject();
                    joo.addProperty("userid", jo.get("userid").getAsString());
                    tmh.message("BB_GIVELINKED", joo);
                    tmh.blockUntilResponse(5000);
                    if (name.get() == null && uuid.get() == null) {
                        BoneBoxPlugin.getInstance().getLogger().warning("Couldn't process Discord message in 5 seconds, cancelling process");
                        return false;
                    }

                    MinecraftChatListener.sendChat(
                            key,
                            Component.text(jo.get("content").getAsString()),
                            new BBModule(name.get(), display.get(), uuid.get(), linked.get())
                    );
                }
            }
            return true;
        }
        return false;
    }
}
