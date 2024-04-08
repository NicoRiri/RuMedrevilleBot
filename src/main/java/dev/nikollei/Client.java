package dev.nikollei;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.ChunkingFilter;
import net.dv8tion.jda.api.utils.MemberCachePolicy;
import net.dv8tion.jda.api.utils.cache.CacheFlag;


import javax.security.auth.login.LoginException;
import java.util.EnumSet;

public class Client {

    private static JDA jda;

    private Client() throws LoginException {
        if (System.getenv("token") == null){
            throw new Error("No token in the process environment");
        }
        jda = JDABuilder.createDefault(
                        System.getenv("token"),
                        GatewayIntent.GUILD_MEMBERS,
                        GatewayIntent.GUILD_MESSAGES,
                        GatewayIntent.GUILD_VOICE_STATES,
                        GatewayIntent.GUILD_BANS,
                        GatewayIntent.GUILD_MESSAGE_REACTIONS,
                        GatewayIntent.DIRECT_MESSAGES
                )
                .disableCache(EnumSet.of(
                        CacheFlag.CLIENT_STATUS,
                        CacheFlag.ACTIVITY,
                        CacheFlag.EMOTE
                ))
                .setMemberCachePolicy(MemberCachePolicy.ALL)
                .addEventListeners(new Listener())
                .setActivity(Activity.playing("/menu"))
                .setChunkingFilter(ChunkingFilter.ALL)
                .build();

        jda.updateCommands().addCommands(
                Commands.slash("menu", "Indique le menu d'aujourd'hui au Resto U' de Médreville")
        ).queue();
    }

    public static void main(String[] args) throws LoginException {
        new Client();
    }

    public static JDA getJDA() {
        return jda;
    }

}
