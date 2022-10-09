package systems.tat.teamspeak.config;

import lombok.Data;
import systems.tat.teamspeak.model.config.*;

/**
 * ToDo: Comment this class
 *
 * @author : Niklas Tat
 * @since : 02.10.2022
 */
@Data
public class BotConfiguration {

    private static TeamspeakCredentialsConfig teamspeakCredentialsConfig;
    private static SupportChannelConfig supportChannelConfig;
    private static AFKConfig afkConfig;
    private static GlobalChannelConfig globalChannelConfig;
    private static ChannelConfig channelConfig;
    private static TeamOverviewConfig teamOverviewConfig;


    public static TeamspeakCredentialsConfig getTeamspeakCredentialsConfig() {
        return teamspeakCredentialsConfig;
    }

    public static void setTeamspeakCredentialsConfig(TeamspeakCredentialsConfig teamspeakCredentialsConfig) {
        BotConfiguration.teamspeakCredentialsConfig = teamspeakCredentialsConfig;
    }

    public static AFKConfig getAfkConfig() {
        return afkConfig;
    }

    public static SupportChannelConfig getSupportChannelConfig() {
        return supportChannelConfig;
    }

    public static GlobalChannelConfig getGlobalChannelConfig() {
        return globalChannelConfig;
    }

    public static void setAfkConfig(AFKConfig afkConfig) {
        BotConfiguration.afkConfig = afkConfig;
    }

    public static void setSupportChannelConfig(SupportChannelConfig supportChannelConfig) {
        BotConfiguration.supportChannelConfig = supportChannelConfig;
    }

    public static void setGlobalChannelConfig(GlobalChannelConfig globalChannelConfig) {
        BotConfiguration.globalChannelConfig = globalChannelConfig;
    }

    public static void setChannelConfig(ChannelConfig channelConfig) {
        BotConfiguration.channelConfig = channelConfig;
    }

    public static ChannelConfig getChannelConfig() {
        return channelConfig;
    }

    public static void setTeamOverviewConfig(TeamOverviewConfig teamOverviewConfig) {
        BotConfiguration.teamOverviewConfig = teamOverviewConfig;
    }

    public static TeamOverviewConfig getTeamOverviewConfig() {
        return teamOverviewConfig;
    }
}
