package io.ix0rai.ramel.fabric.service;

import io.ix0rai.ramel.service.ConfigProvider;
import net.fabricmc.loader.api.FabricLoader;

import java.nio.file.Path;

public class FabricConfigProvider implements ConfigProvider {

    @Override
    public Path getConfigDir() {
        return FabricLoader.getInstance().getConfigDir();
    }
}
