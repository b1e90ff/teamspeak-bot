package systems.tat.teamspeak.listener;

import com.github.theholywaffle.teamspeak3.api.ChannelProperty;
import com.github.theholywaffle.teamspeak3.api.event.ClientMovedEvent;
import com.github.theholywaffle.teamspeak3.api.event.TS3EventAdapter;
import com.github.theholywaffle.teamspeak3.api.exception.TS3CommandFailedException;
import com.github.theholywaffle.teamspeak3.api.wrapper.ClientInfo;
import lombok.extern.slf4j.Slf4j;
import systems.tat.teamspeak.TeamSpeak;
import systems.tat.teamspeak.config.BotConfiguration;
import systems.tat.teamspeak.watcher.SupportChannelWatcher;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.stream.IntStream;

/**
 * ToDo: Comment this class
 *
 * @author : Niklas Tat
 * @since : 04.10.2022
 */
@Slf4j
public class SupportListener extends TS3EventAdapter {
    @Override
    public void onClientMoved(ClientMovedEvent e) {
        ClientInfo clientInfo = TeamSpeak.getTs3API().getClientInfo(e.getClientId());

        // Check if the target is the Support Channel
        if (e.getTargetChannelId() != BotConfiguration.getSupportChannelConfig().getChannelId())
            return;

        // Check if the client is in a supporter group or ignored group
        if (IntStream.of(clientInfo.getServerGroups()).anyMatch(BotConfiguration.getSupportChannelConfig().getSupportGroupIds()::contains)
                || IntStream.of(clientInfo.getServerGroups()).anyMatch(BotConfiguration.getSupportChannelConfig().getIgnoredGroupIds()::contains))
            return;

        LinkedList<Integer> availableSupporter = SupportChannelWatcher.getAvailableSupporter();

        // Check if support is Online
        if (!SupportChannelWatcher.isSupporterAvailable()) {
            TeamSpeak.getTs3API().sendPrivateMessage(e.getClientId(),
                    BotConfiguration
                            .getSupportChannelConfig()
                            .getSupportOfflineJoinMessage()
                            .replace("%supporter%",
                                    String.valueOf(availableSupporter.size())));
            log.info("Client {} tried to join SupportChannel, but no Supporter is online!", clientInfo.getNickname());
            return;
        }

        // Notify the client that he is moved to a supporter
        if (BotConfiguration.getSupportChannelConfig().isPrivateMessageClientIfJoin()) {
            TeamSpeak.getTs3API().sendPrivateMessage(clientInfo.getId(),
                    BotConfiguration
                            .getSupportChannelConfig()
                            .getSupportOnlineJoinMessage()
                            .replace("%supporter%",
                                    String.valueOf(availableSupporter.size())));
        }

        // Notify the supporters (Private Message)
        if (BotConfiguration.getSupportChannelConfig().isPrivateMessageSupporterIfJoin()) {
            SupportChannelWatcher.getAvailableSupporter().forEach(supporter -> TeamSpeak.getTs3API().sendPrivateMessage(supporter,
                    BotConfiguration
                            .getSupportChannelConfig()
                            .getClientJoinedSupportMessage()
                            .replace("%client%", "[URL=" + clientInfo.getClientURI() + "]" + clientInfo.getNickname() + "[/URL]")));
        }

        try {
            // Notify the supporters (Poke Message)
            if (BotConfiguration.getSupportChannelConfig().isPokeSupporterIfJoin()) {
                SupportChannelWatcher.getAvailableSupporter().forEach(supporter -> TeamSpeak.getTs3API().pokeClient(supporter,
                        BotConfiguration
                                .getSupportChannelConfig()
                                .getClientJoinedSupportPokeMessage()
                                .replace("%client%", "[URL=" + clientInfo.getClientURI() + "]" + clientInfo.getNickname() + "[/URL]")));
            }
        } catch (TS3CommandFailedException ex) {
            log.error("The Poke message if a client joins is too long!", ex);
        }

        log.info("Client {} joined SupportChannel and {} supporter are Online!", clientInfo.getNickname(), availableSupporter.size());
        // Create the support channel for the client
        createSupportChannel(clientInfo);
    }

    private void createSupportChannel(ClientInfo clientInfo) {
        // Default channel Permissions
        HashMap<ChannelProperty, String> channelProperties = new HashMap<>();

        channelProperties.put(ChannelProperty.CHANNEL_FLAG_PERMANENT, "0");
        channelProperties.put(ChannelProperty.CHANNEL_FLAG_MAXCLIENTS_UNLIMITED, String.valueOf(BotConfiguration.getSupportChannelConfig().getClientSupportChannelMaxClients()));
        channelProperties.put(ChannelProperty.CHANNEL_CODEC_QUALITY, String.valueOf(BotConfiguration.getSupportChannelConfig().getClientSupportChannelCodecQuality()));
        channelProperties.put(ChannelProperty.CPID, String.valueOf(BotConfiguration.getSupportChannelConfig().getChannelId()));

        SimpleDateFormat formattedDate = new SimpleDateFormat();
        formattedDate.applyPattern("HH:mm");
        String channelName = clientInfo.getNickname() + " (" + formattedDate.format(new Date()) + ")";

        int createdChannelId = TeamSpeak.getTs3API().createChannel(channelName, channelProperties);
        TeamSpeak.getTs3API().addChannelPermission(createdChannelId, "i_channel_needed_join_power", BotConfiguration.getSupportChannelConfig().getClientSupportChannelNeededJoinPower());
        TeamSpeak.getTs3API().addChannelPermission(createdChannelId, "b_client_is_sticky", BotConfiguration.getSupportChannelConfig().isClientStickyIfJoin() ? 1 : 0);
        TeamSpeak.getTs3API().addChannelPermission(createdChannelId, "i_channel_needed_join_power", BotConfiguration.getSupportChannelConfig().getClientSupportChannelNeededJoinPower());
        log.info("Created SupportChannel {} for Client {}", channelName, clientInfo.getNickname());
        TeamSpeak.getTs3API().moveClient(clientInfo.getId(), createdChannelId);
        log.info("Moved Client {} to SupportChannel {}", clientInfo.getNickname(), channelName);

        // Move Query back to default channel
        TeamSpeak.moveToDefault();
    }
}
