package com.ruin9999.networkbroadcast;

import com.google.inject.Inject;
import com.velocitypowered.api.command.CommandManager;
import com.velocitypowered.api.command.CommandMeta;
import com.velocitypowered.api.command.RawCommand;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.plugin.annotation.DataDirectory;
import com.velocitypowered.api.proxy.ProxyServer;
import dev.dejvokep.boostedyaml.YamlDocument;
import dev.dejvokep.boostedyaml.dvs.versioning.BasicVersioning;
import dev.dejvokep.boostedyaml.settings.dumper.DumperSettings;
import dev.dejvokep.boostedyaml.settings.general.GeneralSettings;
import dev.dejvokep.boostedyaml.settings.loader.LoaderSettings;
import dev.dejvokep.boostedyaml.settings.updater.UpdaterSettings;
import org.slf4j.Logger;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Objects;


@Plugin(
        id = "networkbroadcast",
        name = "Broadcast Message",
        version = "1.0.0-SNAPSHOT",
        description = "Broadcasts messages to all connected velocity servers!",
        authors = "Ruin9999"
)
public class NetworkBroadcast {
    public final static String NETWORKBROADCAST_BROADCAST = "networkbroadcast.broadcast";

    private final ProxyServer proxyServer;
    private final Logger logger;

    private static YamlDocument config;

    @Inject
    public NetworkBroadcast(ProxyServer proxyServer, Logger logger, @DataDirectory Path dataDirectory) {
        this.proxyServer = proxyServer;
        this.logger = logger;

        //Boosted Yaml configuration
        try {
            config = YamlDocument.create(new File(dataDirectory.toFile(), "config.yml"),
                    Objects.requireNonNull(getClass().getResourceAsStream("/config.yml")),
                    GeneralSettings.DEFAULT,
                    LoaderSettings.builder().setAutoUpdate(true).build(),
                    DumperSettings.DEFAULT,
                    UpdaterSettings.builder().setVersioning(new BasicVersioning("file-version"))
                            .setOptionSorting(UpdaterSettings.OptionSorting.SORT_BY_DEFAULTS).build());
            config.update();
            config.save();
        } catch (IOException e) {
            logger.error("Could not create/load config file!");
        }
    }

    @Subscribe
    public void onProxyInitialization(ProxyInitializeEvent event) {
        registerCommands();
    }

    private void registerCommands() {
        CommandManager commandManager = proxyServer.getCommandManager();
        CommandMeta commandMeta = commandManager.metaBuilder("networkbroadcast").plugin(this).build();
        RawCommand rawCommand = new BroadcastCommand(proxyServer);
        commandManager.register(commandMeta, rawCommand);

        logger.info("Registered Commands");
    }

    private void registerListeners() {

        logger.info("Registered Listeners");
    }

    public static YamlDocument getConfig() {
        return config;
    }
}
