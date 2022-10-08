package systems.tat.teamspeak.model;

import lombok.Builder;
import lombok.Data;

/**
 * ToDo: Comment this class
 *
 * @author : Niklas Tat
 * @since : 08.10.2022
 */
@Data
@Builder
public class Group {

    private String header;

    // If emptyMessage is empty, the group will be ignored
    private String emptyMessage;
    private String name;
    private String template;

    private int groupId;
}
