package systems.tat.teamspeak.model.config;

import lombok.Builder;
import lombok.Data;
import systems.tat.teamspeak.annotation.Configuration;

import java.io.Serializable;
import java.util.List;

/**
 * ToDo: Comment this class
 *
 * @author : Niklas Tat
 * @since : 29.09.2022
 */
@Data
@Configuration(path = "/watcher/support-channel.json")
public class SupportChannelConfig implements Serializable {

    private boolean isWatcherEnabled;
    private int interval;

    private String closedChannelName;
    private String openChannelName;
    private String supportOnlineJoinMessage;
    private String supportOfflineJoinMessage;
    private String clientJoinedSupportMessage;
    private String clientJoinedSupportPokeMessage;

    private int channelId;
    private int supportOnlineMaxClients;
    private int supportOfflineMaxClients;
    private int clientSupportChannelMaxClients;
    private int clientSupportChannelCodecQuality;
    private int clientSupportChannelNeededJoinPower;
    private int clientSupportChannelNeededTalkPower;

    private int idleTime;

    private List<Integer> supportGroupIds;
    private List<Integer> ignoredGroupIds;

    private boolean createChannelIfJoin;
    private boolean isClientStickyIfJoin;
    private boolean supportOnlineUnlimitedClients;
    private boolean supportOfflineUnlimitedClients;
    private boolean pokeSupporterIfJoin;
    private boolean privateMessageSupporterIfJoin;
    private boolean privateMessageClientIfJoin;

    public SupportChannelConfig() {
        this.isWatcherEnabled = false;
        this.interval = 5;

        this.closedChannelName = "Support currently closed!";
        this.openChannelName = "Open %supporter%";
        this.supportOnlineJoinMessage = "%supporter% Supporter has been notified";
        this.supportOfflineJoinMessage = "No supporter online";
        this.clientJoinedSupportMessage = "The Client %client% has joined the support channel";
        this.clientJoinedSupportPokeMessage = "%client% need help";

        this.channelId = 4;
        this.supportOnlineMaxClients = 200;
        this.supportOfflineMaxClients = 0;
        this.clientSupportChannelMaxClients = 0;
        this.clientSupportChannelCodecQuality = 10;
        this.clientSupportChannelNeededJoinPower = 50;
        this.clientSupportChannelNeededTalkPower = 2;

        this.idleTime = 1800;

        this.supportGroupIds = List.of(6);
        this.ignoredGroupIds = List.of(10);

        this.createChannelIfJoin = true;
        this.isClientStickyIfJoin = true;
        this.supportOnlineUnlimitedClients = true;
        this.supportOfflineUnlimitedClients = true;
        this.pokeSupporterIfJoin = true;
        this.privateMessageSupporterIfJoin = true;
        this.privateMessageClientIfJoin = true;
    }
}
