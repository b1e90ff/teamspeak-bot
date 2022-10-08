package systems.tat.teamspeak.watcher;

import com.github.theholywaffle.teamspeak3.TS3Api;
import com.github.theholywaffle.teamspeak3.TS3Config;
import com.github.theholywaffle.teamspeak3.TS3Query;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import systems.tat.teamspeak.TeamSpeak;
import systems.tat.teamspeak.config.BotConfiguration;
import systems.tat.teamspeak.model.config.AFKConfig;
import systems.tat.teamspeak.model.config.GlobalChannelConfig;
import systems.tat.teamspeak.model.config.TeamspeakCredentialsConfig;
import systems.tat.teamspeak.util.InstanceUtil;

import java.util.List;

/**
 * ToDo: Comment this class
 *
 * @author : Niklas Tat
 * @since : 04.10.2022
 */
@Slf4j
class AFKConfigWatcherTest {

    @BeforeAll
    public static void setUp() throws NoSuchFieldException, IllegalAccessException {
        InstanceUtil.setTeamspeakCredentialsInstance(TeamspeakCredentialsConfig.builder()
                .hostname("localhost")
                .virtualServerPort(9987)
                .defaultChannelId(1)
                .queryUsername("serveradmin")
                .queryPassword("mzsO8Yhe")
                .queryPort(10011)
                .nickname("Ts3Bot")
                .build());

        BotConfiguration.setAfkConfig(AFKConfig.builder()
                .isModuleEnabled(true)
                .interval(1)
                .idleTime(10)
                .ignoredGroupIds(List.of(6))
                .ignoredChannelIds(List.of(12))
                .ignoredClientUniqueIds(List.of("kz3HFAlCw7ZUWtIclgWvwYSUBcE="))
                .build());

        BotConfiguration.setGlobalChannelConfig(GlobalChannelConfig.builder().afkChannelIds(List.of(10, 1)).build());

        final TS3Config ts3Config = new TS3Config();
        TeamspeakCredentialsConfig credentials = TeamSpeak.getCredentials();

        //set address
        ts3Config.setHost(credentials.getHostname());
        //set query port
        ts3Config.setQueryPort(credentials.getQueryPort());
        //set flood rate to unlimited
        ts3Config.setFloodRate(TS3Query.FloodRate.UNLIMITED);

        final TS3Query ts3Query = new TS3Query(ts3Config);
        //connect to teamspeak server
        ts3Query.connect();

        TS3Api api = ts3Query.getApi();
        //login to teamspeak server
        api.login(credentials.getQueryUsername(), credentials.getQueryPassword());

        log.info("Successfully connected to Teamspeak Server!");
        //select teamspeak by port
        api.selectVirtualServerByPort(credentials.getVirtualServerPort());
        //set bot nickname
        api.setNickname(credentials.getNickname());
        api.registerAllEvents();

        TeamSpeak.setTs3API(api);
        TeamSpeak.moveToDefault();
    }

    @AfterAll
    public static void tearDown() {
        AFKWatcher.stop();
        TeamSpeak.getTs3API().logout();
    }

    @Test
    void start() {
        AFKWatcher.start();
    }
}