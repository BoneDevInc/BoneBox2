package com.bonesnetwork.bonebox.commands;

import com.bonesnetwork.bonebox.BoneBox;
import com.bonesnetwork.bonebox.LinkHandler;
import com.bonesnetwork.bonebox.commands.minecraft.InfoCommand;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;
import net.kore.crimson.framework.api.command.Command;
import net.kore.crimson.framework.api.command.CommandRegistery;

public class LinkCommand extends Command {
    public static void register() {
        LinkCommand lc = new LinkCommand();
        CommandRegistery.register(lc);
    }

    @Override
    public SlashCommandData getData() {
        return Commands.slash("link", "Link to a Minecraft account");
    }

    @Override
    public void handle(SlashCommandInteractionEvent event) {
        String ui = event.getUser().getId();
        String code = BoneBox.getLinkHandler().generateCode(ui);

        if (code == null) {
            event.reply("You are already linked.").queue();
        } else {
            event.reply("Your code is "+code+". In Minecraft run /discordlink "+code+" to link your account").queue();
        }
    }
}
