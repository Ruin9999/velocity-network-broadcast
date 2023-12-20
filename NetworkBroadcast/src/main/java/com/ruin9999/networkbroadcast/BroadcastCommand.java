package com.ruin9999.networkbroadcast;

import com.velocitypowered.api.command.RawCommand;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import dev.dejvokep.boostedyaml.YamlDocument;
import dev.dejvokep.boostedyaml.route.Route;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;

import java.util.Collection;

public final class BroadcastCommand implements RawCommand {
    private final ProxyServer proxyServer;
    private final boolean usePrefix;
    private final String prefix;
    private final MiniMessage miniMessage = MiniMessage.miniMessage();
    BroadcastCommand(ProxyServer proxyServer) {
        this.proxyServer = proxyServer;

        YamlDocument config = NetworkBroadcast.getConfig();
        usePrefix = config.getBoolean(Route.from("use-prefix"));
        prefix = config.getString(Route.from("prefix"));
    }

    @Override
    public void execute(Invocation invocation) {
        Collection<Player> players = proxyServer.getAllPlayers();
        String broadcastString;
        if (usePrefix) broadcastString = prefix + invocation.arguments();
        else broadcastString = invocation.arguments();

        Component broadcastComponent = miniMessage.deserialize(broadcastString);
        for (Player player : players) player.sendMessage(broadcastComponent);
    }

    public boolean hasPermission(final Invocation invocation) {
        return invocation.source().hasPermission(NetworkBroadcast.NETWORKBROADCAST_BROADCAST);
    }
}
