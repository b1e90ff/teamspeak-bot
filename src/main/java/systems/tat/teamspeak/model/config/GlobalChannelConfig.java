package systems.tat.teamspeak.model.config;

import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * ToDo: Comment this class
 *
 * @author : Niklas Tat
 * @since : 02.10.2022
 */
@Data
public class GlobalChannelConfig {

    private List<Integer> afkChannelIds;

    public GlobalChannelConfig() {
        this.afkChannelIds = List.of(1);
    }
}
