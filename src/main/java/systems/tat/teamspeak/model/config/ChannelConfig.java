package systems.tat.teamspeak.model.config;

import lombok.Data;
import systems.tat.teamspeak.model.Channel;

import java.util.HashMap;
import java.util.List;

/**
 * ToDo: Comment this class
 *
 * @author : Niklas Tat
 * @since : 04.10.2022
 */
@Data
public class ChannelConfig {

        private boolean isWatcherEnabled;
        private int interval;

        private List<Channel> channels;

        public ChannelConfig() {
                this.isWatcherEnabled = false;
                this.interval = 5;
                HashMap<String, Integer> channelPermissions = new HashMap<>();
                channelPermissions.put("i_channel_needed_modify_power", 75);
                channelPermissions.put("i_channel_needed_delete_power", 75);
                channelPermissions.put("i_channel_needed_join_power", 0);
                channelPermissions.put("i_ft_needed_file_upload_power", 75);
                channelPermissions.put("i_ft_needed_file_download_power", 75);
                channelPermissions.put("i_ft_needed_file_rename_power", 75);
                channelPermissions.put("i_ft_needed_file_browse_power", 75);
                channelPermissions.put("i_ft_needed_directory_create_power", 75);

                this.channels = List.of(Channel.builder()
                        .name("Public Talk ")
                        .parenChannelId(13)
                        .maxClientsInChannel(0)
                        .channelCodecQuality(10)
                        .minChannel(3)
                        .maxChannel(10)
                        .freeChannel(2)
                        .channelPermissions(channelPermissions)
                        .build());
        }
}
