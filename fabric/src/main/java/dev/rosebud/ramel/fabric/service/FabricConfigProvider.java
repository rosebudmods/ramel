package dev.rosebud.ramel.fabric.service;

import dev.rosebud.ramel.service.ConfigProvider;
import net.fabricmc.loader.api.FabricLoader;

import java.nio.file.Path;

public class FabricConfigProvider implements ConfigProvider {

    @Override
    public Path getConfigDir() {
        return FabricLoader.getInstance().getConfigDir();
    }
}
