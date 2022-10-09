package systems.tat.teamspeak.model.config;

import lombok.Data;
import systems.tat.teamspeak.annotation.Configuration;
import systems.tat.teamspeak.model.Group;

import java.util.List;

/**
 * ToDo: Comment this class
 *
 * @author : Niklas Tat
 * @since : 08.10.2022
 */
@Data
@Configuration(path = "/watcher/team-overview.json")
public class TeamOverviewConfig {

    private boolean isWatcherEnabled;
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

    public TeamOverviewConfig() {
        this.isWatcherEnabled = false;
        this.interval = 30;
        this.header = "[center]Header Message for Team Overview You can use TeamSpeak Tags like [b]bold[/b] or [i]italic[/i]...[center]";
        this.footer = "\n\n[center]Footer sample message[center]";
        this.groupSeparator = "\n\n";
        this.clientSeparator = "\n";
        this.onlineMessage = "[B][COLOR=#1bfa02](Online)[/COLOR][/B]\n";
        this.awayMessage = "[B][COLOR=#ff4e00] (Away)[/COLOR][/B]\n";
        this.offlineMessage = "[B][COLOR=#ff0000](Offline)[/COLOR][/B]\n";
        this.notAvailableMessage = "[B][COLOR=#decb1f](Not Available)[/COLOR][/B]\n";

        this.groups = List.of(
                Group.builder()
                        .header("[CENTER][B][SIZE=14]%group%[/SIZE][/B][/CENTER]\n")
                        .emptyMessage("We are searing for new members")
                        .name("Administrator")
                        .template("[B]%client%[/B] | %status%")
                        .groupId(6)
                        .build(),
                Group.builder()
                        .header("[CENTER][B][SIZE=14]%group%[/SIZE][/B][/CENTER]\n")
                        .emptyMessage("We are searing for new members")
                        .name("Supporter")
                        .template("[B]%client%[/B] | %status%")
                        .groupId(11)
                        .build());

        this.channelIds = List.of(4, 10);
    }
}
