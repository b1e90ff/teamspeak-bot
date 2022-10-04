package systems.tat.teamspeak.config;

import lombok.Data;
import systems.tat.teamspeak.model.Channel;
import systems.tat.teamspeak.model.SupportChannel;

/**
 * ToDo: Comment this class
 *
 * @author : Niklas Tat
 * @since : 02.10.2022
 */
@Data
public class BotConfiguration {

    private static SupportChannel supportChannelConfig;
    private static Channel channelConfig;

    public static SupportChannel getSupportChannelConfig() {
        return supportChannelConfig;
    }

    public static Channel getChannelConfig() {
        return channelConfig;
    }

    public static void setSupportChannelConfig(SupportChannel supportChannelConfig) {
        BotConfiguration.supportChannelConfig = supportChannelConfig;
    }

    public static void setChannelConfig(Channel channelConfig) {
        BotConfiguration.channelConfig = channelConfig;
    }
}
