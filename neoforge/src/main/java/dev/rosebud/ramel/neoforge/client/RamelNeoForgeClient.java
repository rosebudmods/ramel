package dev.rosebud.ramel.neoforge.client;

import dev.rosebud.ramel.Ramel;
import dev.rosebud.ramel.client.RamelConfigScreen;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;

@Mod(value = Ramel.MODID, dist = Dist.CLIENT)
public class RamelNeoForgeClient {

    public RamelNeoForgeClient(ModContainer modContainer) {
        modContainer.<IConfigScreenFactory>registerExtensionPoint(IConfigScreenFactory.class, () -> (modContainer1, screen) -> new RamelConfigScreen(screen));
    }
}
