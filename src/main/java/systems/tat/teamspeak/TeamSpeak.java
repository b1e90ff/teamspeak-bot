package systems.tat.teamspeak;

import com.github.theholywaffle.teamspeak3.TS3Api;
import lombok.Getter;
import systems.tat.teamspeak.model.TeamspeakCredentials;

/**
 * ToDo: Comment this class
 *
 * @author : Niklas Tat
 * @since : 28.09.2022
 */
@Getter
public class TeamSpeak {

    private static final TeamspeakCredentials credentials = null;
    private static TS3Api api;
    private TeamSpeak() {}
}
