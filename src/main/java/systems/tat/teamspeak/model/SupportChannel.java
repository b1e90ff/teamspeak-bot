package systems.tat.teamspeak.model;

import lombok.Data;

import java.io.Serializable;

/**
 * ToDo: Comment this class
 *
 * @author : Niklas Tat
 * @since : 29.09.2022
 */
@Data
public class SupportChannel implements Serializable {

    private int channelId;
    private long idleTime;
}
