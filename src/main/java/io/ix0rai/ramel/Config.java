package io.ix0rai.ramel;

import net.fabricmc.loader.api.FabricLoader;
import org.quiltmc.config.api.ReflectiveConfig;
import org.quiltmc.config.api.annotations.Comment;
import org.quiltmc.config.api.annotations.FloatRange;
import org.quiltmc.config.api.serializers.TomlSerializer;
import org.quiltmc.config.api.values.TrackedValue;
import org.quiltmc.config.implementor_api.ConfigEnvironment;
import org.quiltmc.config.implementor_api.ConfigFactory;

public class Config extends ReflectiveConfig {
	private static final String FORMAT = "toml";
	private static final String FAMILY = "ramel";
	private static final ConfigEnvironment ENVIRONMENT = new ConfigEnvironment(FabricLoader.getInstance().getConfigDir(), FORMAT, TomlSerializer.INSTANCE);
	public static final Config INSTANCE = ConfigFactory.create(ENVIRONMENT, FAMILY, "ramel", Config.class);

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
