package dev.rosebud.ramel.fabric;

import dev.rosebud.ramel.Config;
import net.fabricmc.api.ModInitializer;

public class RamelFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        Config.init();
    }
}
