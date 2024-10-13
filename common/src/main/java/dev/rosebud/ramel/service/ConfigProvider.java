package dev.rosebud.ramel.service;

import folk.sisby.kaleido.lib.quiltconfig.api.serializers.TomlSerializer;
import folk.sisby.kaleido.lib.quiltconfig.implementor_api.ConfigEnvironment;

import java.nio.file.Path;
import java.util.ServiceLoader;

public interface ConfigProvider {

    Path getConfigDir();

    default ConfigEnvironment createConfigEnvironment() {
        return new ConfigEnvironment(getConfigDir(), "toml", TomlSerializer.INSTANCE);
    }

    static ConfigProvider load() {
        return ServiceLoader.load(ConfigProvider.class).findFirst().orElseThrow(() -> new IllegalStateException("No platform implementation found for " + ConfigProvider.class.getCanonicalName()));
    }
}
