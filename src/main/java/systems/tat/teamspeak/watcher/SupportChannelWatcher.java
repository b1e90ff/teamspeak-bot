package systems.tat.teamspeak.watcher;

import com.github.theholywaffle.teamspeak3.api.ChannelProperty;
import com.github.theholywaffle.teamspeak3.api.wrapper.Client;
import lombok.extern.slf4j.Slf4j;
import systems.tat.teamspeak.TeamSpeak;
import systems.tat.teamspeak.annotation.InNewThread;
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
@InNewThread
public class SupportChannelWatcher {

    private static final AtomicBoolean running = new AtomicBoolean(false);
    private static final LinkedList<Integer> availableSupporter = new LinkedList<>();


    private SupportChannelWatcher() {
    }

    public static void start() {
        if (running.get()) {
            log.warn("Trying to start SupportChannel Watcher, but it is already running!");
            log.warn("If this is not the case, please restart the bot and report this!");
            log.warn("In case you need help, feel free to contact the developer!");
            return;
        }

        log.info("Starting SupportChannel Watcher...");
        log.info("Activating SupportEvent listener...");
        TeamSpeak.getTs3API().addTS3Listeners(new SupportListener());
        log.info("Watching for Supporter in SupportChannel with the ID {}", BotConfiguration.getSupportChannelConfig().getChannelId());
        running.set(true);
        runWatcher();
    }

    public static void stop() {
        if (!running.get()) {
            log.warn("Trying to stop SupportChannel Watcher, but it is not running!");
            log.warn("Please report this issue with some information about your setup!");
            log.warn("In case you need help, feel free to contact the developer!");
            return;
        }

        log.info("Stopping SupportChannel Watcher...");
        log.info("Deactivating SupportEvent listener...");
        TeamSpeak.getTs3API().removeTS3Listeners(new SupportListener());
        running.set(false);
    }

    @SuppressWarnings("BusyWait")
    private static void runWatcher() {
        while (running.get()) {
            try {
                Thread.sleep(BotConfiguration.getSupportChannelConfig().getInterval() * 1000L);
                availableSupporter.clear();

                // check if there are any supporter available
                TeamSpeak.getTs3API().getClients().stream()
                        .filter(client -> IntStream.of(client.getServerGroups()).anyMatch(BotConfiguration.getSupportChannelConfig().getSupportGroupIds()::contains))
                        .filter(client -> !isClientAFKOrNotOnDuty(client))
                        .forEach(client -> availableSupporter.add(client.getId()));

                // Change channel name
                changeChannelName();
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
                || client.getIdleTime() > BotConfiguration.getSupportChannelConfig().getIdleTime()
                || BotConfiguration.getChannelConfig().getAfkChannelIds().contains(client.getChannelId())
                || !client.isChannelCommander();
    }

    public synchronized static boolean isSupporterAvailable() {
        return availableSupporter.size() > 0;
    }
    public synchronized static LinkedList<Integer> getAvailableSupporter() {
        return availableSupporter;
    }
}