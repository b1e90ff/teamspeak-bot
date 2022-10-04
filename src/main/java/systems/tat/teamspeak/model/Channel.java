package systems.tat.teamspeak.model;

import lombok.Builder;
import lombok.Data;

import java.util.HashMap;

/**
 * ToDo: Comment this class
 *
 * @author : Niklas Tat
 * @since : 04.10.2022
 */
@Data
@Builder
public class Channel {

    private String name;

    private int parenChannelId;
    // 0 = UNLIMITED
    private int maxClientsInChannel;
    // 1 - 10
    private int channelCodecQuality;
    private int minChannel;
    private int maxChannel;
    private int freeChannel;

    private HashMap<String, Integer> channelPermissions;
}
