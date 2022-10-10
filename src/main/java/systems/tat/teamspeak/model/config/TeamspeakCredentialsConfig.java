package systems.tat.teamspeak.model.config;

import lombok.*;
import systems.tat.teamspeak.annotation.Configuration;

import java.io.Serializable;

/**
 * This Pojo class represents the credentials for the TeamSpeak server. It
 * includes the hostname, virtual server port, query port, query username and password.
 * This information is needed to connect to your TeamSpeak server. Be sure that you
 * whitelist the IP address of this Bot in your TeamSpeak server because the Bot use
 * the FloodRate Unlimited and can be blocked by the server if you don't whitelist it.
 * For more information about the FloodRate see the official documentation:
 *
 * @see com.github.theholywaffle.teamspeak3.TS3Query.FloodRate
 * @see systems.tat.teamspeak.TeamSpeak
 *
 * @author : Niklas Tat
 * @since : 28.09.2022
 */
@Data
@Configuration(path = "credentials\\teamspeak.json")
public class TeamspeakCredentialsConfig implements Serializable {

    /*
     Teamspeak Server Information
     */

    /**
     * This one is the hostname or IP address of the Teamspeak server.
     * It can be localhost, your domain or IP address. It is the same as the
     * one you use to connect to the server. But if you run the Bot one the same
     * Server as the Teamspeak server, you can use localhost.
     */
    private String hostname;
    /**
     * This is the port to your TeamSpeak Server. It's not the same as the query port.
     * The default port for TeamSpeak is 9987.
     */
    private int virtualServerPort;

    /**
     * This is the ID if the channel where the Bot should join.
     * IMPORTANT: If you delete the channel, you change the ID here and restart the Bot.
     */
    private int defaultChannelId;

    /*
     Teamspeak Query Information
     */

    /**
     * The bot need the query username to log in to the server with admin/root rights.
     * The default username for this is serveradmin. If you created a new Query User
     * be sure he has enough rights for this bot and your wanted functions.
     */
    private String queryUsername;
    /**
     * This password is being used to log in to the query interface.
     * If you don't know the password, you can change it by running
     * the TeamSpeak Server with the parameter serveradmin_password=MY_NEW_PASSWORD_HERE
     * IMPORTANT: With this password the bot get access as "root" or "admin" to the server.
     * This means, that the bot can do everything on the server. So don't give this password
     * anyone else, who don't know what he is doing and who don't is a trusted person.
     * With this he can for example delete your server, ban all users, etc.
     *
     */
    private String queryPassword;
    /**
     * This one is the Query port of your Teamspeak server.
     * The default value of this port is 10011. If you changed it,
     * you have to change it here as well it the config file.
     * IMPORTANT: It is not the same as the virtual server port.
     */
    private int queryPort;

    /**
     * This is the nickname of the bot. You can
     * change it to whatever you want in the config
     * file. The default nickname is "TeamSpeakBot"
     */
    private String nickname;

    public TeamspeakCredentialsConfig() {
        this.hostname = "localhost";
        this.virtualServerPort = 9987;
        this.defaultChannelId = 1;
        this.queryUsername = "serveradmin";
        this.queryPassword = "password";
        this.queryPort = 10011;
        this.nickname = "Tat";
    }
}
