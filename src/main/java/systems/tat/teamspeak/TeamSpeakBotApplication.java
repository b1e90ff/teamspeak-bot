package systems.tat.teamspeak;

import lombok.extern.slf4j.Slf4j;
import systems.tat.teamspeak.loader.ConfigLoader;
import systems.tat.teamspeak.loader.WatcherLoader;

/**
 * ToDo: Comment this class
 *
 * @author : Niklas Tat
 * @since : 28.09.2022
 */
@Slf4j
public class TeamSpeakBotApplication {

    public static void main(String[] args) {
        // Loading Configuration
        ConfigLoader.getInstance().loadConfig();
        // Connect to TeamSpeak
        TeamSpeak.setInstance();
        // Searching for any Watchers
        WatcherLoader.getInstance().searchForAnyWatcher();
        // Starting Watchers
        WatcherLoader.getInstance().startWatchers();
    }
}
