package systems.tat.teamspeak.watcher;

import lombok.extern.slf4j.Slf4j;
import systems.tat.teamspeak.TeamSpeak;
import systems.tat.teamspeak.annotation.Watcher;
import systems.tat.teamspeak.config.BotConfiguration;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.IntStream;

/**
 * ToDo: Comment this class
 *
 * @author : Niklas Tat
 * @since : 04.10.2022
 */
@Slf4j
@Watcher
public class AFKWatcher {

    private static final AtomicBoolean running = new AtomicBoolean(false);

    private AFKWatcher() {}

    public static void start() {
        if (running.get()) {
            log.warn("Trying to start AFK Watcher, but it is already running!");
            log.warn("If this is not the case, please restart the bot and report this!");
            log.warn("In case you need help, feel free to contact the developer!");
            return;
        }

        log.info("Starting AFK Watcher...");
        log.info("Watching for any AFK clients...");
        running.set(true);
        runWatcher();
    }

    public static void stop() {
        if (!running.get()) {
            log.warn("Trying to stop AFK Watcher, but it is not running!");
            log.warn("Please report this issue with some information about your setup!");
            log.warn("In case you need help, feel free to contact the developer!");
            return;
        }

        log.info("Stopping AFK Watcher...");
        running.set(false);
    }

    @SuppressWarnings("BusyWait")
    private static void runWatcher() {
        while (running.get()) {
            try {
                // check for any AFK Clients and if the clients is ignored (already in AFK, in ignored Group...)
                TeamSpeak.getTs3API().getClients().stream()
                        // Check if client is not a Query Client
                        .filter(client -> !client.isServerQueryClient())
                        // Is client idle
                        .filter(client -> client.getIdleTime() >= BotConfiguration.getAfkConfig().getIdleTime() * 1000L)
                        // Is client not already in AFK Channel
                        .filter(client -> !BotConfiguration.getGlobalChannelConfig().getAfkChannelIds().contains(client.getChannelId()))
                        // Is client not in ignored channel
                        .filter(client -> !BotConfiguration.getAfkConfig().getIgnoredChannelIds().contains(client.getChannelId()))
                        // Is client not group not ignored
                        .filter(client -> IntStream.of(client.getServerGroups()).noneMatch(BotConfiguration.getAfkConfig().getIgnoredGroupIds()::contains))
                        // Is client not ignored
                        .filter(client -> !BotConfiguration.getAfkConfig().getIgnoredClientUniqueIds().contains(client.getUniqueIdentifier()))
                        // Move client to AFK Channel
                        .forEach(client -> {
                            // The First AFK Channel is the default AFK Channel
                            TeamSpeak.getTs3API().moveClient(client.getId(), BotConfiguration.getGlobalChannelConfig().getAfkChannelIds().get(0));
                            log.info("Moved client '{}' to AFK Channel cause of inactivity ({} seconds)", client.getNickname(), client.getIdleTime() / 1000L);
                        });
                // Move Query back to default Channel
                TeamSpeak.moveToDefault();

                Thread.sleep(BotConfiguration.getAfkConfig().getInterval() * 1000L);
            } catch (Exception ex) {
                log.error("Error while running AFK Watcher!", ex);
                log.error("If this error occurs often, please report this issue with some information about your setup!");
                log.error("In case you need help, feel free to contact the developer!");
            }
        }
        log.info("AFK Watcher stopped!");
    }
}
