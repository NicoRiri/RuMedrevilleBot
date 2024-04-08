package dev.nikollei;

import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.io.IOException;
import java.time.LocalTime;
import java.util.Objects;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Listener extends ListenerAdapter {


    @Override
    public void onReady(ReadyEvent event) {
        System.out.println("✅ Bot Started");
        String channelId = System.getenv("channelId");
        String dailyTime = System.getenv("dailyTime");

        if (dailyTime != null){
            Pattern patternDailyTime = Pattern.compile("(\\b\\d{1,2}:\\d{2}\\b)");
            Matcher matcherDailyTime = patternDailyTime.matcher(dailyTime);
            if (!matcherDailyTime.find()) {
                throw new Error("wrong format of dailyTime");
            }
        }

        int hour, minute;
        if (!Objects.equals(dailyTime, "") && dailyTime != null){
            hour = Integer.parseInt(dailyTime.split(":")[0]);
            minute = Integer.parseInt(dailyTime.split(":")[1]);
        } else {
            minute = -1;
            hour = -1;
        }

        if (!Objects.equals(channelId, "") && channelId != null && hour != -1 && minute != -1){
            ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
            scheduler.scheduleAtFixedRate(() -> {
                if (isBusinessDay()) {
                    LocalTime currentTime = LocalTime.now();
                    if (currentTime.getHour() == hour && currentTime.getMinute() == minute) {
                        try {
                            sendMenuInChannel(event.getJDA().getTextChannelById(channelId));
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
            }, 0, 1, TimeUnit.MINUTES);
        }

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
                event.reply("La commande n'existe pas").setEphemeral(true).queue();
                break;

        }
    }
    private void sendMenuInChannel(TextChannel channel) throws IOException {
        if (channel != null) {
            channel.sendMessageEmbeds(Crous.getMenu()).queue();
        }
    }
    private static boolean isBusinessDay() {
        int dayOfWeek = java.time.LocalDate.now().getDayOfWeek().getValue();
        return dayOfWeek >= 1 && dayOfWeek <= 5;
    }
}
