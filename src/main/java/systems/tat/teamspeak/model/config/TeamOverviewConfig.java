package systems.tat.teamspeak.model.config;

import lombok.Builder;
import lombok.Data;
import systems.tat.teamspeak.model.Group;

import java.util.List;

/**
 * ToDo: Comment this class
 *
 * @author : Niklas Tat
 * @since : 08.10.2022
 */
@Data
@Builder
public class TeamOverviewConfig {

    private boolean isModuleEnabled;
    private int interval;

    private String header;
    private String footer;
    private String groupSeparator;
    private String clientSeparator;
    private String onlineMessage;
    private String awayMessage;
    private String offlineMessage;
    private String notAvailableMessage;

    private List<Group> groups;
    private List<Integer> channelIds;
}
