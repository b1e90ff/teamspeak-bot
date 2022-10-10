package systems.tat.teamspeak.watcher;

import com.github.theholywaffle.teamspeak3.api.ChannelProperty;
import com.github.theholywaffle.teamspeak3.api.wrapper.Client;
import com.github.theholywaffle.teamspeak3.api.wrapper.ServerGroupClient;
import lombok.extern.slf4j.Slf4j;
import systems.tat.teamspeak.TeamSpeak;
import systems.tat.teamspeak.annotation.Watcher;
import systems.tat.teamspeak.config.BotConfiguration;
import systems.tat.teamspeak.model.Group;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * ToDo: Comment this class
 *
 * @author : Niklas Tat
 * @since : 07.10.2022
 */
@Watcher
@Slf4j
public class TeamOverviewWatcher extends Thread {

    private static final AtomicBoolean running = new AtomicBoolean(false);

    private static final StringBuilder newDescription = new StringBuilder();
    private static int lastGroupId = 0;

    public void run() {
        if (BotConfiguration.getTeamOverviewConfig().isWatcherEnabled()) {
            log.info("TeamOverviewWatcher is enabled and will be started");
            running.set(true);
            runWatcher();
        }
    }

    @SuppressWarnings("BusyWait")
    private static void runWatcher() {
        while (running.get()) {
            try {
                newDescription.append(BotConfiguration.getTeamOverviewConfig().getHeader());

                // Creation new Description
                BotConfiguration.getTeamOverviewConfig().getGroups()
                        .forEach(TeamOverviewWatcher::updateGroup);

                // Set the footer
                newDescription.append(BotConfiguration.getTeamOverviewConfig().getFooter());

                // Check if description is the same in the overview channel
                BotConfiguration.getTeamOverviewConfig().getChannelIds().stream()
                        .map(TeamSpeak.getTs3API()::getChannelInfo)
                        .forEach(channelInfo -> {
                            String lastDescription = channelInfo.getDescription();

                            if (!lastDescription.equals(newDescription.toString())) {
                                Map<ChannelProperty, String> properties = new HashMap<>();
                                (properties).put(ChannelProperty.CHANNEL_DESCRIPTION, newDescription.toString());
                                TeamSpeak.getTs3API().editChannel(channelInfo.getId(), properties);
                                log.info("Updated TeamOverview Description in Channel '{}' with ID '{}'", channelInfo.getName(), channelInfo.getId());
                            }
                        });

                // Reset the description, lastGroup and wait for the next update
                newDescription.delete(0, newDescription.length());
                lastGroupId = 0;

                Thread.sleep(BotConfiguration.getTeamOverviewConfig().getInterval() * 1000L);
            } catch (Exception ex) {
                log.error("Error while running TeamOverview Watcher!", ex);
                log.error("If this error occurs often, please report this issue with some information about your setup!");
                log.error("In case you need help, feel free to contact the developer!");
            }
        }
        log.info("TeamOverview Watcher stopped!");
    }

    private static void updateGroup(Group group) {
        if (lastGroupId == 0)
            lastGroupId = group.getGroupId();

        if (lastGroupId != group.getGroupId()) {
            newDescription.append(BotConfiguration.getTeamOverviewConfig().getGroupSeparator());
            lastGroupId = group.getGroupId();
        }

        // Group name
        newDescription.append(group.getHeader().replace("%group%", group.getName()));

        // Check if group is Empty
        if (TeamSpeak.getTs3API().getServerGroupClients(group.getGroupId()).isEmpty()) {
            newDescription.append(group.getEmptyMessage()
                    .replace("%group%", group.getName()));
            return;
        }

        TeamSpeak.getTs3API().getServerGroupClients(group.getGroupId())
                .forEach(client -> newDescription.append(updateClient(client, group)));
    }

    private static String updateClient(ServerGroupClient serverGroupClient, Group group) {
        // Add client to description
        return group.getTemplate()
                .replace("%client%", "[URL=client://"
                        + serverGroupClient.getClientDatabaseId()
                        + "/"
                        + serverGroupClient.getUniqueIdentifier()
                        + "~"
                        + serverGroupClient.getNickname()
                        + "]"
                        + serverGroupClient.getNickname()
                        + "[/URL]")
                .replace("%status%", getClientStatus(serverGroupClient)) +
                BotConfiguration.getTeamOverviewConfig().getClientSeparator();
    }

    private static String getClientStatus(ServerGroupClient serverGroupClient) {
        // Client is offline
        if (!TeamSpeak.getTs3API().isClientOnline(serverGroupClient.getUniqueIdentifier()))
            return BotConfiguration.getTeamOverviewConfig().getOfflineMessage();

        Client client = TeamSpeak.getTs3API().getClientByUId(serverGroupClient.getUniqueIdentifier());

        // Client is afk
        if (client.isAway()
                || client.isOutputMuted()
                || client.getIdleTime() > BotConfiguration.getSupportChannelConfig().getIdleTime()
                || BotConfiguration.getGlobalChannelConfig().getAfkChannelIds().contains(client.getChannelId()))
            return BotConfiguration.getTeamOverviewConfig().getAwayMessage();

        // Client is not on duty
        if (!client.isChannelCommander())
            return BotConfiguration.getTeamOverviewConfig().getNotAvailableMessage();

        return BotConfiguration.getTeamOverviewConfig().getOnlineMessage();
    }
}
