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
import systems.tat.teamspeak.model.*;
import systems.tat.teamspeak.model.config.ChannelConfig;
import systems.tat.teamspeak.model.config.TeamspeakCredentialsConfig;
import systems.tat.teamspeak.util.InstanceUtil;

import java.util.HashMap;
import java.util.List;

/**
 * ToDo: Comment this class
 *
 * @author : Niklas Tat
 * @since : 04.10.2022
 */
@Slf4j
class ChannelWatcherTest {

    @BeforeAll
    public static void setUp() throws NoSuchFieldException, IllegalAccessException {
        // Load TeamSpeak instance
        TeamSpeak.getTs3API();

        InstanceUtil.setTeamspeakCredentialsInstance(TeamspeakCredentialsConfig.builder()
                .hostname("localhost")
                .virtualServerPort(9987)
                .defaultChannelId(1)
                .queryUsername("serveradmin")
                .queryPassword("mzsO8Yhe")
                .queryPort(10011)
                .nickname("Ts3Bot")
                .build());

        HashMap<String, Integer> channelPermissions = new HashMap<>();
        channelPermissions.put("i_channel_needed_modify_power", 75);
        channelPermissions.put("i_channel_needed_delete_power", 75);
        channelPermissions.put("i_channel_needed_join_power", 0);
        channelPermissions.put("i_ft_needed_file_upload_power", 75);
        channelPermissions.put("i_ft_needed_file_download_power", 75);
        channelPermissions.put("i_ft_needed_file_rename_power", 75);
        channelPermissions.put("i_ft_needed_file_browse_power", 75);
        channelPermissions.put("i_ft_needed_directory_create_power", 75);

        BotConfiguration.setChannelConfig(ChannelConfig.builder()
                        .isModuleEnabled(true)
                        .interval(1)
                        .channels(List.of(Channel.builder()
                                        .name("Public Talk ")
                                        .parenChannelId(13)
                                        .maxClientsInChannel(0)
                                        .channelCodecQuality(10)
                                        .minChannel(3)
                                        .maxChannel(10)
                                        .freeChannel(2)
                                        .channelPermissions(channelPermissions)
                                .build()))
                        .build());

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
        ChannelWatcher.stop();
        TeamSpeak.getTs3API().logout();
    }

    @Test
    void start() {
        ChannelWatcher.start();
    }
}