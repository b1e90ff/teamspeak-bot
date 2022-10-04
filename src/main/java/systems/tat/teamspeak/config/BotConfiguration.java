package systems.tat.teamspeak.config;

import lombok.Data;
import systems.tat.teamspeak.model.*;

/**
 * ToDo: Comment this class
 *
 * @author : Niklas Tat
 * @since : 02.10.2022
 */
@Data
public class BotConfiguration {

    private static SupportChannel supportChannelConfig;
    private static AFK afkConfig;
    private static GlobalChannel globalChannelConfig;
    private static ChannelConfig channelConfig;

    public static AFK getAfkConfig() {
        return afkConfig;
    }

    public static SupportChannel getSupportChannelConfig() {
        return supportChannelConfig;
    }

    public static GlobalChannel getGlobalChannelConfig() {
        return globalChannelConfig;
    }

    public static void setAfkConfig(AFK afkConfig) {
        BotConfiguration.afkConfig = afkConfig;
    }

    public static void setSupportChannelConfig(SupportChannel supportChannelConfig) {
        BotConfiguration.supportChannelConfig = supportChannelConfig;
    }

    public static void setGlobalChannelConfig(GlobalChannel globalChannelConfig) {
        BotConfiguration.globalChannelConfig = globalChannelConfig;
    }

    public static void setChannelConfig(ChannelConfig channelConfig) {
        BotConfiguration.channelConfig = channelConfig;
    }

    public static ChannelConfig getChannelConfig() {
        return channelConfig;
    }
}
