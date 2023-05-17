package main;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.interactions.commands.OptionType;
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
        jda = JDABuilder.createDefault(
                //Prod
//                        "MTA2ODU1NTM1MjY2NjI5NjQ2Mw.GagxzX.dzr2_9N7uBOdCjjO_5_gRTEBTtTkXRfEAjAsyU",
                //Dev
                        "NDQ1OTg5MjEwMjY1MDkyMDk5.G8qxu6.OUMGqKyQ3QnqQfDbpH1EZlnvgvmYvtnzKJw1UY",
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
                .enableCache(CacheFlag.VOICE_STATE)
                .addEventListeners(new Listener())
                .setActivity(Activity.playing("/menu"))
                .setChunkingFilter(ChunkingFilter.ALL)
                .build();

        jda.updateCommands().addCommands(
                Commands.slash("menu", "Indique le menu du Resto U' de Médreville d'aujourd'hui")
        ).queue();
    }

    public static void main(String[] args) throws LoginException {
        new Client();
    }

    public static JDA getJDA() {
        return jda;
    }

}
