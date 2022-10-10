package systems.tat.teamspeak.model.config;

import lombok.Data;
import systems.tat.teamspeak.annotation.Configuration;

import java.util.List;

/**
 * ToDo: Comment this class
 *
 * @author : Niklas Tat
 * @since : 02.10.2022
 */
@Data
@Configuration(path = "watcher\\global-channel.json")
public class GlobalChannelConfig {

    private List<Integer> afkChannelIds;

    public GlobalChannelConfig() {
        this.afkChannelIds = List.of(1);
    }
}
