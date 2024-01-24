package com.bonesnetwork.bonebox.commands.minecraft;

import com.bonesnetwork.bonebox.BoneBox;
import com.bonesnetwork.bonebox.embed.Converter;
import com.bonesnetwork.bonebox.redis.PubSub;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData;
import net.kore.crimson.framework.api.command.SubCommand;

import java.util.Objects;

public class InfoCommand extends SubCommand {
    protected static JsonElement element;

    @Override
    public SubcommandData getData() {
        OptionData od = new OptionData(OptionType.STRING, "server", "The selected server", true);
        od.addChoice("Hub", "hub");
        return new SubcommandData("info", "Get information about the selected server").addOptions(od);
    }

    @Override
    public void handle(SlashCommandInteractionEvent event) {
        event.deferReply().queue();

        InfoCommandHandler ich = new InfoCommandHandler();
        PubSub.register(ich);
        JsonObject jo = new JsonObject();
        jo.addProperty("server", Objects.requireNonNull(event.getInteraction().getOption("server")).getAsString());
        ich.message("BB_GETINFO", jo);
        ich.blockUntilResponse(10000);
        if (element == null) {
            //event.getHook().sendMessageEmbeds(Converter.convert(BoneBox.getConfig().getInfoFailCommand())).queue();
            return;
        }

        // Use element to create an embed here and send it
        // event.getHook().sendMessageEmbeds(Some embed here).queue();
    }
}
