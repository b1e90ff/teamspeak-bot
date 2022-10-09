package systems.tat.teamspeak.watcher;

import com.github.theholywaffle.teamspeak3.TS3Api;
import com.github.theholywaffle.teamspeak3.TS3Config;
import com.github.theholywaffle.teamspeak3.TS3Query;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import systems.tat.teamspeak.TeamSpeak;
import systems.tat.teamspeak.config.BotConfiguration;
import systems.tat.teamspeak.model.config.GlobalChannelConfig;
import systems.tat.teamspeak.model.config.SupportChannelConfig;
import systems.tat.teamspeak.model.config.TeamspeakCredentialsConfig;

import java.util.List;

/**
 * ToDo: Comment this class
 *
 * @author : Niklas Tat
 * @since : 04.10.2022
 */
@Slf4j
class SupportGlobalChannelConfigWatcherTest {
/*
    @BeforeAll
    public static void setup() throws NoSuchFieldException, IllegalAccessException {
        // Load TeamSpeak instance
        TeamSpeak.getTs3API();

        InstanceUtil.setTeamspeakCredentialsConfig(TeamspeakCredentialsConfig.builder()
                .hostname("localhost")
                .virtualServerPort(9987)
                .defaultChannelId(1)
                .queryUsername("serveradmin")
                .queryPassword("mzsO8Yhe")
                .queryPort(10011)
                .nickname("Ts3Bot")
                .build());

        BotConfiguration.setSupportChannelConfig(SupportChannelConfig.builder()
                .isWatcherEnabled(true)
                .interval(1)
                .closedChannelName("closed")
                .openChannelName("Open %supporter%")
                .supportOnlineJoinMessage("%supporter% Supporter has been notified")
                .supportOfflineJoinMessage("No supporter online")
                .clientJoinedSupportMessage("The Client %client% has joined the support channel")
                .clientJoinedSupportPokeMessage("%client% need help")
                .channelId(4)
                .supportOnlineMaxClients(200)
                .supportOfflineMaxClients(0)
                .clientSupportChannelMaxClients(0)
                .clientSupportChannelCodecQuality(10)
                .clientSupportChannelNeededJoinPower(50)
                .clientSupportChannelNeededTalkPower(2)
                .idleTime(1000000000000L)
                .supportGroupIds(List.of(6))
                .ignoredGroupIds(List.of(10))
                .createChannelIfJoin(true)
                .isClientStickyIfJoin(true)
                .supportOnlineUnlimitedClients(true)
                .supportOfflineUnlimitedClients(true)
                .pokeSupporterIfJoin(true)
                .privateMessageSupporterIfJoin(true)
                .privateMessageClientIfJoin(true)
                .build());

        BotConfiguration.setGlobalChannelConfig(GlobalChannelConfig.builder().afkChannelIds(List.of(1)).build());

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
    static void tearDown() {
        SupportChannelWatcher.stop();
        TeamSpeak.getTs3API().logout();
    }

    @Test
    void start() {
        SupportChannelWatcher.start();
    }

 */
}