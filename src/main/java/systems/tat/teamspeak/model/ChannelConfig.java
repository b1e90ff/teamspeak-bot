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
public class ChannelConfig {

        private boolean isModuleEnabled;
        private int interval;

        private List<Channel> channels;

}
