package io.ix0rai.ramel.neoforge.client;

import io.ix0rai.ramel.client.RamelConfigScreen;
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
