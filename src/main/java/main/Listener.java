package main;

import config.Crous;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.io.IOException;


public class Listener extends ListenerAdapter {


    @Override
    public void onReady(ReadyEvent event) {
        System.out.println((char) 27 + "[32m" + "Le bot est ready" + (char) 27 + "[37m");
    }

    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        switch (event.getName()) {
            case "menu":
                try {
                    event.replyEmbeds(Crous.getMenu()).queue();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                break;

            default:
                event.reply("existe pas").setEphemeral(true).queue();
                System.out.println("existe pas");
                break;

        }
    }
}
