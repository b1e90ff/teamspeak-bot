package systems.tat.teamspeak;

import com.github.theholywaffle.teamspeak3.TS3Api;
import lombok.Getter;
import systems.tat.teamspeak.model.config.TeamspeakCredentialsConfig;

/**
 * ToDo: Comment this class
 *
 * @author : Niklas Tat
 * @since : 28.09.2022
 */
@Getter
public class TeamSpeak {

    private static final TeamspeakCredentialsConfig credentials = null;
    private static TS3Api api;
    private TeamSpeak() {}

    public static TS3Api getTs3API() {
        return api;
    }

    public static TeamspeakCredentialsConfig getCredentials() {
        return credentials;
    }

    public static void setTs3API(TS3Api api) {
        TeamSpeak.api = api;
    }

    public static void moveToDefault() {
        if (api.whoAmI().getChannelId() != credentials.getDefaultChannelId()) {
            api.moveQuery(credentials.getDefaultChannelId());
        }
    }
}
