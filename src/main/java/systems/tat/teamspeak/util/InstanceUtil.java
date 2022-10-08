package systems.tat.teamspeak.util;

import sun.misc.Unsafe;
import systems.tat.teamspeak.TeamSpeak;
import systems.tat.teamspeak.model.config.TeamspeakCredentialsConfig;

import java.lang.reflect.Field;

/**
 * ToDo: Comment this class
 *
 * @author : Niklas Tat
 * @since : 28.09.2022
 */
public class InstanceUtil {

    public static void setTeamspeakCredentialsInstance(TeamspeakCredentialsConfig teamspeakCredentialsConfig) throws NoSuchFieldException, IllegalAccessException {
        final Field unsafeField = Unsafe.class.getDeclaredField("theUnsafe");
        unsafeField.setAccessible(true);
        final Unsafe unsafe = (Unsafe) unsafeField.get(null);

        final Field field = TeamSpeak.class.getDeclaredField("credentials");
        final Object fieldBase = unsafe.staticFieldBase(field);
        final long offset = unsafe.staticFieldOffset(field);
        unsafe.putObject(fieldBase, offset, teamspeakCredentialsConfig);
    }
}
