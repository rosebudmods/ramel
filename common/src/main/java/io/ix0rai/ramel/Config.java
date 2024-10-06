package io.ix0rai.ramel;

import folk.sisby.kaleido.api.ReflectiveConfig;
import folk.sisby.kaleido.lib.quiltconfig.api.annotations.Comment;
import folk.sisby.kaleido.lib.quiltconfig.api.annotations.FloatRange;
import folk.sisby.kaleido.lib.quiltconfig.api.values.TrackedValue;
import io.ix0rai.ramel.service.ConfigProvider;

public class Config extends ReflectiveConfig {
	private static final String FAMILY = Ramel.MODID;
	public static final Config INSTANCE = create(ConfigProvider.load().createConfigEnvironment(), FAMILY, "ramel", Config.class);

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
