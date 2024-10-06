package io.ix0rai.ramel.neoforge.service;

import io.ix0rai.ramel.service.ConfigProvider;
import net.neoforged.fml.loading.FMLPaths;

import java.nio.file.Path;

public class NeoForgeConfigProvider implements ConfigProvider {
    @Override
    public Path getConfigDir() {
        return FMLPaths.CONFIGDIR.get();
    }
}
