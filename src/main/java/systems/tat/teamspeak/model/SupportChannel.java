package systems.tat.teamspeak.model;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * ToDo: Comment this class
 *
 * @author : Niklas Tat
 * @since : 29.09.2022
 */
@Data
@Builder
public class SupportChannel implements Serializable {

    private String closedChannelName;
    private String openChannelName;
    private String supportOnlineJoinMessage;
    private String supportOfflineJoinMessage;
    private String clientJoinedSupportMessage;

    private int channelId;
    private int supportOnlineMaxClients;
    private int supportOfflineMaxClients;
    private int clientSupportChannelMaxClients;
    private int clientSupportChannelCodecQuality;
    private int clientSupportChannelNeededJoinPower;
    private int clientSupportChannelNeededTalkPower;

    private long idleTime;

    private List<Integer> supportGroupIds;
    private List<Integer> ignoredGroupIds;

    private boolean createChannelIfJoin;
    private boolean isClientStickyIfJoin;
    private boolean supportOnlineUnlimitedClients;
    private boolean supportOfflineUnlimitedClients;
    private boolean pokeSupporterIfJoin;
    private boolean privateMessageSupporterIfJoin;
    private boolean privateMessageClientIfJoin;
}
