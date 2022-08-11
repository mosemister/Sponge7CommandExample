package org.example;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.state.GameStartedServerEvent;
import org.spongepowered.api.plugin.Plugin;

@Plugin(id = "example", name = "Example", version = "1.0.0")
public class SpongePlugin {

    @Listener
    public void onServerStart(GameStartedServerEvent event){
        Sponge.getCommandManager()
                .register(this, ExampleCommands.registerCommands(), "example");
    }
}
