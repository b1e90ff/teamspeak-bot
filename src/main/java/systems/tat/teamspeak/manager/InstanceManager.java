package systems.tat.teamspeak.manager;

import systems.tat.teamspeak.TeamSpeak;
import systems.tat.teamspeak.model.TeamspeakCredentials;

import java.lang.reflect.Field;

/**
 * ToDo: Comment this class
 *
 * @author : Niklas Tat
 * @since : 28.09.2022
 */
public class InstanceManager {

    public static void setTeamspeakCredentialsInstance(TeamspeakCredentials teamspeakCredentials) throws NoSuchFieldException, IllegalAccessException {
        Field field = TeamSpeak.class.getDeclaredField("credentials");
        field.setAccessible(true);
        field.set(null,teamspeakCredentials);
    }
}
