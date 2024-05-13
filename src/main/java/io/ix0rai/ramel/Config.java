package io.ix0rai.ramel;

import folk.sisby.kaleido.api.ReflectiveConfig;
import folk.sisby.kaleido.lib.quiltconfig.api.annotations.Comment;
import folk.sisby.kaleido.lib.quiltconfig.api.annotations.FloatRange;
import folk.sisby.kaleido.lib.quiltconfig.api.serializers.TomlSerializer;
import folk.sisby.kaleido.lib.quiltconfig.api.values.TrackedValue;
import folk.sisby.kaleido.lib.quiltconfig.implementor_api.ConfigEnvironment;
import net.fabricmc.loader.api.FabricLoader;

public class Config extends ReflectiveConfig {
	private static final String FORMAT = "toml";
	private static final String FAMILY = "ramel";
	private static final ConfigEnvironment ENVIRONMENT = new ConfigEnvironment(FabricLoader.getInstance().getConfigDir(), FORMAT, TomlSerializer.INSTANCE);
	public static final Config INSTANCE = create(ENVIRONMENT, FAMILY, "ramel", Config.class);

	@Comment("The amount of extra range beyond the camel's normal hitbox, in blocks, that the ramming effect will apply.")
	@Comment("Value will be halved for baby camels.")
	@FloatRange(min = 0.0f, max = 2.5f)
	public final TrackedValue<Float> additionalRammingRange = this.value(0.5f);

	@Comment("The amount of damage dealt to entities rammed, in half-hearts.")
	@Comment("Value will be halved for baby camels.")
	@FloatRange(min = 0.0f, max = 10f)
	public final TrackedValue<Float> rammingDamage = this.value(2.0f);

	@Comment("The knockback multiplier applied to entities rammed. 0.0 will be no knockback, 2.0 will be twice the normal knockback.")
	@Comment("Value will be halved for baby camels.")
	@FloatRange(min = 0.0f, max = 5.0f)
	public final TrackedValue<Float> knockbackMultiplier = this.value(1.0f);
}
