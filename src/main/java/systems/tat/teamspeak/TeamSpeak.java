package systems.tat.teamspeak;

import com.github.theholywaffle.teamspeak3.TS3Api;
import com.github.theholywaffle.teamspeak3.TS3Config;
import com.github.theholywaffle.teamspeak3.TS3Query;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import systems.tat.teamspeak.config.BotConfiguration;
import systems.tat.teamspeak.model.config.TeamspeakCredentialsConfig;

/**
 * ToDo: Comment this class
 *
 * @author : Niklas Tat
 * @since : 28.09.2022
 */
@Getter
@Slf4j
public class TeamSpeak {

    private static TeamSpeak instance;
    private static TS3Api api;

    private TeamSpeak() {
        instance = this;
        log.info("TeamSpeak initialized");
        connectToTeamSpeak();
    }

    public static TS3Api getTs3API() {
        return api;
    }

    public static void setInstance() {
        if (instance == null) {
            new TeamSpeak();
        }

    }

    private void connectToTeamSpeak() {
        log.info("Connecting to TeamSpeak...");
        try {
            final TS3Config ts3Config = new TS3Config();
            TeamspeakCredentialsConfig credentials = BotConfiguration.getTeamspeakCredentialsConfig();

            //set address
            ts3Config.setHost(credentials.getHostname());
            //set query port
            ts3Config.setQueryPort(credentials.getQueryPort());
            //set flood rate to unlimited
            ts3Config.setFloodRate(TS3Query.FloodRate.UNLIMITED);

            final TS3Query ts3Query = new TS3Query(ts3Config);
            //connect to teamspeak server
            ts3Query.connect();

            api = ts3Query.getApi();
            //login to teamspeak server
            api.login(credentials.getQueryUsername(), credentials.getQueryPassword());

            log.info("Successfully connected to Teamspeak Server!");
            //select teamspeak by port
            api.selectVirtualServerByPort(credentials.getVirtualServerPort());
            //set bot nickname
            api.setNickname(credentials.getNickname());
            api.registerAllEvents();
        } catch (Exception ex) {
            log.error("Error while connecting to TeamSpeak", ex);
            log.error("Shutting down...");
            System.exit(1);
        }
    }

    public static void moveToDefault() {
        if (api.whoAmI().getChannelId() != BotConfiguration.getTeamspeakCredentialsConfig().getDefaultChannelId()) {
            api.moveQuery(BotConfiguration.getTeamspeakCredentialsConfig().getDefaultChannelId());
        }
    }
}
