package systems.tat.teamspeak.model.config;

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
public class SupportChannelConfig implements Serializable {

    private boolean isModuleEnabled;
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
