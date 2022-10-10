package systems.tat.teamspeak.model.config;

import lombok.Data;
import systems.tat.teamspeak.annotation.Configuration;

import java.util.List;

/**
 * ToDo: Comment this class
 *
 * @author : Niklas Tat
 * @since : 04.10.2022
 */
@Data
@Configuration(path = "watcher\\afk.json")
// The First AFK Channel in Channel is the default AFK Channel
public class AFKConfig {

    private boolean isWatcherEnabled;
    private int interval;

    private int idleTime;

    private List<Integer> ignoredGroupIds;
    // All AFK channels in the Channel Pojo are included in this list
    private List<Integer> ignoredChannelIds;
    private List<String> ignoredClientUniqueIds;

    public AFKConfig() {
        this.isWatcherEnabled = false;
        this.interval = 5;
        this.idleTime = 300;
        this.ignoredGroupIds = List.of(1);
        this.ignoredChannelIds = List.of(1);
        this.ignoredClientUniqueIds = List.of("kz3HFAlCw7ZUWtIclgWvwYSUBcE=");
    }
}
