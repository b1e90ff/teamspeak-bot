package systems.tat.teamspeak.config;

import lombok.Data;
import systems.tat.teamspeak.model.config.AFKConfig;
import systems.tat.teamspeak.model.config.ChannelConfig;
import systems.tat.teamspeak.model.config.GlobalChannelConfig;
import systems.tat.teamspeak.model.config.SupportChannelConfig;

/**
 * ToDo: Comment this class
 *
 * @author : Niklas Tat
 * @since : 02.10.2022
 */
@Data
public class BotConfiguration {

    private static SupportChannelConfig supportChannelConfig;
    private static AFKConfig afkConfig;
    private static GlobalChannelConfig globalChannelConfig;
    private static ChannelConfig channelConfig;

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
}
