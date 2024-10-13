package dev.rosebud.ramel.neoforge.client;

import dev.rosebud.ramel.client.RamelConfigScreen;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.fml.ModContainer;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;

@OnlyIn(Dist.CLIENT)
public class RamelNeoForgeClient {

    public static void init(ModContainer modContainer) {
        modContainer.<IConfigScreenFactory>registerExtensionPoint(IConfigScreenFactory.class, () -> (modContainer1, screen) -> new RamelConfigScreen(screen));
    }
}
