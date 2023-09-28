package main;

import config.Crous;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.io.IOException;
import java.time.LocalTime;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


public class Listener extends ListenerAdapter {


    @Override
    public void onReady(ReadyEvent event) {
        System.out.println((char) 27 + "[32m" + "Le bot est ready" + (char) 27 + "[37m");
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        scheduler.scheduleAtFixedRate(() -> {
            if (EstJourOuvrable()) {
                LocalTime currentTime = LocalTime.now();
                if (currentTime.getHour() == 11 && currentTime.getMinute() == 50) {
                    try {
                        EnvoyerMessageAutomatique(event.getJDA().getTextChannelById("1156713583179214968"));
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }, 0, 1, TimeUnit.MINUTES);
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
    private void EnvoyerMessageAutomatique(TextChannel channel) throws IOException {
        if (channel != null) {
            channel.sendMessageEmbeds(Crous.getMenu()).queue();
        }
    }
    private static boolean EstJourOuvrable() {
        int dayOfWeek = java.time.LocalDate.now().getDayOfWeek().getValue();
        return dayOfWeek >= 1 && dayOfWeek <= 5;
    }
}
