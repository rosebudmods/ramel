package dev.rosebud.ramel.neoforge;

import dev.rosebud.ramel.Ramel;
import dev.rosebud.ramel.neoforge.client.RamelNeoForgeClient;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.loading.FMLEnvironment;

@Mod(Ramel.MODID)
public class RamelNeoForge {

    public RamelNeoForge(ModContainer modContainer, IEventBus modEventBus) {
        if(FMLEnvironment.dist.isClient()) {
            RamelNeoForgeClient.init(modContainer);
        }
    }
}
