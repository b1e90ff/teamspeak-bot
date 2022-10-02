package systems.tat.teamspeak.watcher;

import com.github.theholywaffle.teamspeak3.api.wrapper.Client;
import lombok.extern.slf4j.Slf4j;
import systems.tat.teamspeak.annotation.InNewThread;
import systems.tat.teamspeak.config.BotConfiguration;

import java.util.LinkedList;
import java.util.concurrent.atomic.AtomicBoolean;

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
    private final LinkedList<String> availableSupporter = new LinkedList<>();


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
        running.set(true);
    }

    public static void stop() {
        if (!running.get()) {
            log.warn("Trying to stop SupportChannel Watcher, but it is not running!");
            log.warn("Please report this issue with some information about your setup!");
            log.warn("In case you need help, feel free to contact the developer!");
            return;
        }

        log.info("Stopping SupportChannel Watcher...");
        running.set(false);
    }

    private boolean isClientAFKOrNotDuty(Client client) {
        return client.isAway()
                || client.isOutputMuted()
                || client.getIdleTime() > BotConfiguration.getSupportChannelConfig().getIdleTime()
                || !BotConfiguration.getChannelConfig().getAfkChannelIds().contains(client.getChannelId())
                || !client.isChannelCommander();
    }
}
