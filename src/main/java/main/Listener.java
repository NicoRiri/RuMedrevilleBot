package main;

import config.Crous;
import me.maxouxax.multi4j.exceptions.MultiLoginException;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import javax.annotation.Nonnull;

import java.io.IOException;
import java.net.URISyntaxException;

public class Listener extends ListenerAdapter {


    @Override
    public void onReady(@Nonnull ReadyEvent event) {
        System.out.println((char) 27 + "[32m" + "Le bot est ready" + (char) 27 + "[37m");
    }

    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {

        //Instance Var
        Guild guild;
        User user;
        TextChannel textChannel;
        switch (event.getName()) {
            case "menu":

                try {
                    event.replyEmbeds(Crous.getMenu()).queue();
                } catch (MultiLoginException e) {
                    throw new RuntimeException(e);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                break;


            default:
                event.reply("n'existe pas").setEphemeral(true).queue();
                System.out.println("existe pas");
                break;

        }
    }

    @Override
    public void onMessageReceived(@Nonnull MessageReceivedEvent event) {
        String[] args = event.getMessage().getContentRaw().split("\\s+");

        /**
         * Commande
         */


//        //ru
//        if (args[0].equalsIgnoreCase("<@&947771137776046130>")) {
//            try {
//                event.getChannel().sendMessageEmbeds(Crous.getMenu());
//            } catch (MultiLoginException e) {
//                throw new RuntimeException(e);
//            } catch (IOException e) {
//                throw new RuntimeException(e);
//            } catch (URISyntaxException e) {
//                throw new RuntimeException(e);
//            } catch (InterruptedException e) {
//                throw new RuntimeException(e);
//            }
//        }
    }
}
