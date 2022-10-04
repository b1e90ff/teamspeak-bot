package systems.tat.teamspeak.watcher;

import com.github.theholywaffle.teamspeak3.api.ChannelProperty;
import com.github.theholywaffle.teamspeak3.api.wrapper.Channel;
import lombok.extern.slf4j.Slf4j;
import systems.tat.teamspeak.TeamSpeak;
import systems.tat.teamspeak.annotation.Watcher;
import systems.tat.teamspeak.config.BotConfiguration;

import java.security.Key;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * ToDo: Comment this class
 *
 * @author : Niklas Tat
 * @since : 04.10.2022
 */
@Slf4j
@Watcher
public class ChannelWatcher {

    private static final AtomicBoolean running = new AtomicBoolean(false);

    private ChannelWatcher() {
    }

    public static void start() {
        if (running.get()) {
            log.warn("Trying to start Channel Watcher, but it is already running!");
            log.warn("If this is not the case, please restart the bot and report this!");
            log.warn("In case you need help, feel free to contact the developer!");
            return;
        }

        log.info("Starting Channel Watcher...");
        log.info("Watching for any Channel to handle...");
        running.set(true);
        runWatcher();
    }

    public static void stop() {
        if (!running.get()) {
            log.warn("Trying to stop Channel Watcher, but it is not running!");
            log.warn("Please report this issue with some information about your setup!");
            log.warn("In case you need help, feel free to contact the developer!");
            return;
        }

        log.info("Stopping Channel Watcher...");
        running.set(false);
    }

    @SuppressWarnings("BusyWait")
    private static void runWatcher() {
        while (running.get()) {
            try {
                Thread.sleep(BotConfiguration.getChannelConfig().getInterval() * 1000L);

                BotConfiguration.getChannelConfig().getChannels()
                        .forEach(channel -> {
                            // Getting all child channels of parent channel
                            List<Channel> channelInfos = TeamSpeak.getTs3API().getChannels().stream()
                                    .filter(channelInfo -> channelInfo.getParentChannelId() == channel.getParenChannelId())
                                    .toList();

                            final List<Channel> freeChannels = getFreeChannels(channelInfos);

                            // Check if free channels are less than the minimum
                            if (freeChannels.size() < channel.getMinChannel()
                                    && channelInfos.size() < channel.getMaxChannel()) {
                                // Create new channel
                                createChannel(channel, channelInfos);
                            }

                            // Check if free channels are more than the min
                            if (freeChannels.size() > channel.getMinChannel()) {
                                // Delete channel
                                deleteChannel(freeChannels, channel);
                            }

                            // Check the channel names
                            checkChannelNames(channelInfos, channel);
                        });
            } catch (Exception ex) {
                log.error("Error while running Channel Watcher!", ex);
                log.error("If this error occurs often, please report this issue with some information about your setup!");
                log.error("In case you need help, feel free to contact the developer!");
            }
        }
        log.info("Channel Watcher stopped!");
    }

    private static void checkChannelNames(List<Channel> channelInfos, systems.tat.teamspeak.model.Channel channel) {
        final AtomicInteger counter = new AtomicInteger(0);
        channelInfos.forEach(channelInfo -> {
            String channelName = channel.getName() + counter.incrementAndGet();
            if (!channelInfo.getName().equals(channelName)) {
                TeamSpeak.getTs3API().editChannel(channelInfo.getId(), ChannelProperty.CHANNEL_NAME, channelName);
            }
        });
    }

    private static void deleteChannel(List<Channel> freeChannels, systems.tat.teamspeak.model.Channel channelConfig) {
        // Get how many channels should be deleted
        final int deleteCount = freeChannels.size() - channelConfig.getMinChannel();

        for (int i = 0; i < deleteCount; i++) {
            // Get last channel
            final Channel channel = freeChannels.get(freeChannels.size() - 1);

            // Delete channel
            TeamSpeak.getTs3API().deleteChannel(channel.getId());
        }
    }

    private static void createChannel(systems.tat.teamspeak.model.Channel channelConfig, List<Channel> channelInfos) {
        // Set channel properties
        final HashMap<ChannelProperty, String> channelProperties = new HashMap<>();
        channelProperties.put(ChannelProperty.CHANNEL_FLAG_PERMANENT, "1");
        channelProperties.put(ChannelProperty.CHANNEL_CODEC_QUALITY, String.valueOf(channelConfig.getChannelCodecQuality()));
        channelProperties.put(ChannelProperty.CPID, String.valueOf(channelConfig.getParenChannelId()));

        if (channelConfig.getFreeChannel() == 0) {
            channelProperties.put(ChannelProperty.CHANNEL_FLAG_MAXCLIENTS_UNLIMITED, "1");
        } else {
            channelProperties.put(ChannelProperty.CHANNEL_MAXCLIENTS, String.valueOf(channelConfig.getMaxChannel()));
        }

        // Generate Channel Name
        String channelName = generateChannelName(channelConfig, channelInfos);

        // Create new channel
        int channelId = TeamSpeak.getTs3API().createChannel(channelName, channelProperties);

        // Set channel permissions
        channelConfig.getChannelPermissions().forEach((permissionsName, value) -> {
            TeamSpeak.getTs3API().addChannelPermission(channelId, permissionsName, value);
        });

        log.info("Created new channel {} with id {}", channelName, channelId);
    }

    private static String generateChannelName(systems.tat.teamspeak.model.Channel channelConfig, List<Channel> channelInfos) {
        final String channelName = channelConfig.getName();
        final AtomicInteger channelNumber = new AtomicInteger(0);

        // Get all channels
        List<Channel> channels = TeamSpeak.getTs3API().getChannels();

        // Get the highest channel number
        channelInfos.forEach(channel -> {
            final String channelNameWithoutPrefix = channel.getName().replace(channelName, "");
            final int channelNumberWithoutPrefix = Integer.parseInt(channelNameWithoutPrefix);

            if (channelNumberWithoutPrefix > channelNumber.get()) {
                channelNumber.set(channelNumberWithoutPrefix);
            }
        });

        // Return the new channel name
        return channelName + (channelNumber.get() + 1);
    }

    private static List<Channel> getFreeChannels(List<Channel> channels) {
        return channels.stream()
                .filter(channel -> channel.getTotalClients() == 0)
                .toList();
    }
}
