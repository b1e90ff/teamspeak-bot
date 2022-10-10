package systems.tat.teamspeak.watcher;

import com.github.theholywaffle.teamspeak3.api.ChannelProperty;
import com.github.theholywaffle.teamspeak3.api.wrapper.Client;
import lombok.extern.slf4j.Slf4j;
import systems.tat.teamspeak.TeamSpeak;
import systems.tat.teamspeak.annotation.Watcher;
import systems.tat.teamspeak.config.BotConfiguration;
import systems.tat.teamspeak.listener.SupportListener;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.IntStream;

/**
 * ToDo: Comment this class
 *
 * @author : Niklas Tat
 * @since : 29.09.2022
 */
@Slf4j
@Watcher
public class SupportChannelWatcher extends Thread {

    private static final AtomicBoolean running = new AtomicBoolean(false);
    private static final LinkedList<Integer> availableSupporter = new LinkedList<>();

    public void run() {
        if (BotConfiguration.getSupportChannelConfig().isWatcherEnabled()) {
            log.info("SupportChannelWatcher is enabled and will be started");
            running.set(true);
            runWatcher();
        }
    }

    @SuppressWarnings("BusyWait")
    private static void runWatcher() {
        while (running.get()) {
            try {
                availableSupporter.clear();

                // check if there are any supporter available
                TeamSpeak.getTs3API().getClients().stream()
                        .filter(client -> IntStream.of(client.getServerGroups()).anyMatch(BotConfiguration.getSupportChannelConfig().getSupportGroupIds()::contains))
                        .filter(client -> !isClientAFKOrNotOnDuty(client))
                        .forEach(client -> availableSupporter.add(client.getId()));

                // Change channel name
                changeChannelName();

                Thread.sleep(BotConfiguration.getSupportChannelConfig().getInterval() * 1000L);
            } catch (Exception ex) {
                log.error("Error while running SupportChannel Watcher!", ex);
                log.error("If this error occurs often, please report this issue with some information about your setup!");
                log.error("In case you need help, feel free to contact the developer!");
            }
        }
        log.info("SupportChannel Watcher stopped!");
    }

    private static void changeChannelName() {
        HashMap<ChannelProperty, String> channelProperties = new HashMap<>();

        final boolean supporterAvailable = availableSupporter.size() > 0;

        channelProperties.put(ChannelProperty.CHANNEL_NAME,
                supporterAvailable ?
                BotConfiguration.getSupportChannelConfig().getOpenChannelName().replace("%supporter%", String.valueOf(availableSupporter.size())) :
                BotConfiguration.getSupportChannelConfig().getClosedChannelName().replace("%supporter%", String.valueOf(availableSupporter.size())));

        // Check if channel name is already correct if yes return
        if (channelProperties.get(ChannelProperty.CHANNEL_NAME).equals(TeamSpeak.getTs3API().getChannelInfo(BotConfiguration.getSupportChannelConfig().getChannelId()).getName())) {
            return;
        }

        channelProperties.put(ChannelProperty.CHANNEL_MAXCLIENTS,
                supporterAvailable ?
                        String.valueOf(BotConfiguration.getSupportChannelConfig().getSupportOnlineMaxClients()) :
                        String.valueOf(BotConfiguration.getSupportChannelConfig().getSupportOfflineMaxClients()));
        channelProperties.put(ChannelProperty.CHANNEL_FLAG_MAXCLIENTS_UNLIMITED,
                supporterAvailable ?
                        BotConfiguration.getSupportChannelConfig().isSupportOnlineUnlimitedClients() ? "1" : "0" :
                        BotConfiguration.getSupportChannelConfig().isSupportOfflineUnlimitedClients() ? "1" : "0");

        log.info("Changing SupportChannel name to: " + channelProperties.get(ChannelProperty.CHANNEL_NAME));
        TeamSpeak.getTs3API().editChannel(BotConfiguration.getSupportChannelConfig().getChannelId(), channelProperties);
    }

    private static boolean isClientAFKOrNotOnDuty(Client client) {
        return client.isAway()
                || client.isOutputMuted()
                || client.getIdleTime() > BotConfiguration.getSupportChannelConfig().getIdleTime() * 1000L
                || BotConfiguration.getGlobalChannelConfig().getAfkChannelIds().contains(client.getChannelId())
                || !client.isChannelCommander();
    }

    public synchronized static boolean isSupporterAvailable() {
        return availableSupporter.size() > 0;
    }
    public synchronized static LinkedList<Integer> getAvailableSupporter() {
        return availableSupporter;
    }
}