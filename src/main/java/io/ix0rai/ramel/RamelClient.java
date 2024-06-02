package io.ix0rai.ramel;

import io.ix0rai.ramel.client.RamelConfigScreen;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.minecraft.client.MinecraftClient;

public class RamelClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ClientCommandRegistrationCallback.EVENT.register((dispatcher, buildCtx) -> dispatcher.register(
            ClientCommandManager.literal("ramel").executes(ctx -> {
                MinecraftClient client = ctx.getSource().getClient();
                client.send(() -> client.setScreen(new RamelConfigScreen(null)));
                return 1;
            })
        ));
    }
}
