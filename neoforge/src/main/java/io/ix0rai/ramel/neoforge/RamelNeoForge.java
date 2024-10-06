package io.ix0rai.ramel.neoforge;

import io.ix0rai.ramel.Ramel;
import io.ix0rai.ramel.neoforge.client.RamelNeoForgeClient;
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
