package systems.tat.teamspeak.model;

import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * ToDo: Comment this class
 *
 * @author : Niklas Tat
 * @since : 04.10.2022
 */
@Data
@Builder
// The First AFK Channel in Channel is the default AFK Channel
public class AFK {

    private boolean isModuleEnabled;
    private int interval;

    private int idleTime;

    private List<Integer> ignoredGroupIds;
    // All AFK channels in the Channel Pojo are included in this list
    private List<Integer> ignoredChannelIds;
    private List<String> ignoredClientUniqueIds;
}
