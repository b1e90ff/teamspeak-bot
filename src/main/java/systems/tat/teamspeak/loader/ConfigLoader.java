package systems.tat.teamspeak.loader;

import lombok.extern.slf4j.Slf4j;
import systems.tat.teamspeak.TeamSpeak;
import systems.tat.teamspeak.config.BotConfiguration;
import systems.tat.teamspeak.model.config.*;
import systems.tat.teamspeak.util.FileUtil;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * ToDo: Comment this class
 *
 * @author : Niklas Tat
 * @since : 09.10.2022
 */
@Slf4j
public class ConfigLoader {

    private static final String TEAMSPEAK_CREDENTIALS_PATH = FileUtil.getJarPath() + "config/credentials.json";
    private static final String GLOBAL_CHANNEL_PATH = FileUtil.getJarPath() + "config/channel.json";
    private static final String WATCHER_PATH = FileUtil.getJarPath() + "watcher/";
    private static final String AFK_WATCHER_PATH = WATCHER_PATH + "afk.json";
    private static final String CHANNEL_WATCHER_PATH = WATCHER_PATH + "channel.json";
    private static final String SUPPORT_WATCHER_PATH = WATCHER_PATH + "support.json";
    private static final String TEAM_OVERVIEW_WATCHER_PATH = WATCHER_PATH + "teamOverview.json";

    private static final AtomicBoolean createdNewFile = new AtomicBoolean(true);

    private static ConfigLoader instance;

    private ConfigLoader() {
        instance = this;
        log.info("ConfigLoader initialized");
    }

    public static ConfigLoader getInstance() {
        if (instance == null) {
            return new ConfigLoader();
        }

        return instance;
    }

    public void loadConfig() {
        log.info("Loading config...");
        loadTeamSpeakCredentialsConfig();
        loadGlobalChannelConfig();
        loadAFKConfig();
        loadChannelConfig();
        loadSupportConfig();
        loadTeamOverviewConfig();

        if (createdNewFile.get()) {
            log.warn("Created new config files!");
            log.warn("Please fill out the config files and restart the bot!");
            System.exit(0);
        }
    }

    private void loadTeamSpeakCredentialsConfig() {
        log.info("Loading TeamSpeak credentials config...");

        if (!FileUtil.jsonFileExists(TEAMSPEAK_CREDENTIALS_PATH)) {
            log.warn("TeamSpeak credentials config not found, creating new one...");
            createdNewFile.set(true);

            // Creating the Credentials Template
            FileUtil.writeJsonFile(TEAMSPEAK_CREDENTIALS_PATH, new TeamspeakCredentialsConfig());

            log.warn("TeamSpeak credentials config created. Please fill in the credentials and restart the bot.");
            log.warn("The Bot will shutdown after checking the other configs");
            return;
        }

        //Loading json file
        BotConfiguration.setTeamspeakCredentialsConfig(FileUtil.readJsonFile(TEAMSPEAK_CREDENTIALS_PATH, TeamspeakCredentialsConfig.class));

        log.info("Successfully loaded TeamSpeak credentials config");
    }

    private void loadAFKConfig() {
        log.info("Loading AFK Watcher config...");

        if (!FileUtil.jsonFileExists(AFK_WATCHER_PATH)) {
            log.warn("AFK Watcher config not found, creating new one...");
            createdNewFile.set(true);

            // Creating the Config Template
            FileUtil.writeJsonFile(AFK_WATCHER_PATH, new AFKConfig());

            log.warn("AFK Watcher config created. Please edit the config and restart the bot.");
            log.warn("The Bot will shutdown after checking the other configs");
            return;
        }

        //Loading json file
        BotConfiguration.setAfkConfig(FileUtil.readJsonFile(AFK_WATCHER_PATH, AFKConfig.class));
        log.info("Successfully loaded AFK Watcher config");
    }

    private void loadChannelConfig() {
        log.info("Loading Channel Watcher config...");

        if (!FileUtil.jsonFileExists(CHANNEL_WATCHER_PATH)) {
            log.warn("Channel Watcher config not found, creating new one...");
            createdNewFile.set(true);

            // Creating the Config Template
            FileUtil.writeJsonFile(CHANNEL_WATCHER_PATH, new ChannelConfig());

            log.warn("Channel Watcher config created. Please edit the config and restart the bot.");
            log.warn("The Bot will shutdown after checking the other configs");
            return;
        }

        //Loading json file
        BotConfiguration.setChannelConfig(FileUtil.readJsonFile(CHANNEL_WATCHER_PATH, ChannelConfig.class));
        log.info("Successfully loaded Channel Watcher config");
    }

    private void loadGlobalChannelConfig() {
        log.info("Loading Global Channel config...");

        if (!FileUtil.jsonFileExists(GLOBAL_CHANNEL_PATH)) {
            log.warn("Global Channel config not found, creating new one...");
            createdNewFile.set(true);

            // Creating the Config Template
            FileUtil.writeJsonFile(GLOBAL_CHANNEL_PATH, new GlobalChannelConfig());

            log.warn("Global Channel config created. Please edit the config and restart the bot.");
            log.warn("The Bot will shutdown after checking the other configs");
            return;
        }

        //Loading json file
        BotConfiguration.setGlobalChannelConfig(FileUtil.readJsonFile(GLOBAL_CHANNEL_PATH, GlobalChannelConfig.class));
        log.info("Successfully loaded Global Channel config");
    }

    private void loadSupportConfig() {
        log.info("Loading Support Watcher config...");

        if (!FileUtil.jsonFileExists(SUPPORT_WATCHER_PATH)) {
            log.warn("Support Watcher config not found, creating new one...");
            createdNewFile.set(true);

            // Creating the Config Template
            FileUtil.writeJsonFile(SUPPORT_WATCHER_PATH, new SupportChannelConfig());

            log.warn("Support Watcher config created. Please edit the config and restart the bot.");
            log.warn("The Bot will shutdown after checking the other configs");
            return;
        }

        //Loading json file
        BotConfiguration.setSupportChannelConfig(FileUtil.readJsonFile(SUPPORT_WATCHER_PATH, SupportChannelConfig.class));
        log.info("Successfully loaded Support Watcher config");
    }

    private void loadTeamOverviewConfig() {
        log.info("Loading Team Overview Watcher config...");

        if (!FileUtil.jsonFileExists(TEAM_OVERVIEW_WATCHER_PATH)) {
            log.warn("Team Overview Watcher config not found, creating new one...");
            createdNewFile.set(true);

            // Creating the Config Template
            FileUtil.writeJsonFile(TEAM_OVERVIEW_WATCHER_PATH, new TeamOverviewConfig());

            log.warn("Team Overview Watcher config created. Please edit the config and restart the bot.");
            log.warn("The Bot will shutdown after checking the other configs");
            return;
        }

        //Loading json file
        BotConfiguration.setTeamOverviewConfig(FileUtil.readJsonFile(TEAM_OVERVIEW_WATCHER_PATH, TeamOverviewConfig.class));
        log.info("Successfully loaded Team Overview Watcher config");
    }
}
