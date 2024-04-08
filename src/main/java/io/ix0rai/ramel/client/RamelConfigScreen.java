package io.ix0rai.ramel.client;

import com.mojang.serialization.Codec;
import io.ix0rai.ramel.Config;
import io.ix0rai.ramel.mixin.RangeAccessor;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.option.SimpleOptionsScreen;
import net.minecraft.client.option.GameOptions;
import net.minecraft.client.option.Option;
import net.minecraft.text.Text;
import org.jetbrains.annotations.Nullable;
import org.quiltmc.config.api.Constraint;
import org.quiltmc.config.api.values.TrackedValue;

@Environment(EnvType.CLIENT)
public class RamelConfigScreen extends SimpleOptionsScreen {
	public RamelConfigScreen(@Nullable Screen parent) {
		super(parent, MinecraftClient.getInstance().options, Text.translatable("ramel.config.title"), new Option[]{
				createOptional(Config.INSTANCE.rammingDamage),
				createOptional(Config.INSTANCE.additionalRammingRange),
				createOptional(Config.INSTANCE.knockbackMultiplier),
		});
	}

	@SuppressWarnings("all")
	private static Option<Double> createOptional(TrackedValue<Float> trackedValue) {
		Constraint.Range<?> range = null;

		for (Constraint<?> c : trackedValue.constraints()) {
			if (c instanceof Constraint.Range<?> constraintRange) {
				range = constraintRange;
			}
		}

		if (range == null) {
			throw new RuntimeException("value must have float range constraint " + trackedValue);
		}

		final double max = (float) ((RangeAccessor) (Object) range).getMax();
		final double min = (float) ((RangeAccessor) (Object) range).getMin();

		return new Option<>(
				"ramel.config." + trackedValue.key().toString(),
				Option.constantTooltip(Text.translatable("ramel.config.tooltip." + trackedValue.key().toString())),
				(text, value) -> GameOptions.getGenericValueText(text, Text.translatable("ramel.config.value." + trackedValue.key().toString(), value)),
				new Option.IntRangeValueSet((int) min * 10, (int) max * 10).withModifier(i -> (double) i / 10.0, double_ -> (int) (double_ * 10.0)),
				Codec.doubleRange(min, max),
				(double) trackedValue.getDefaultValue(),
				value -> {
					trackedValue.setValue(value.floatValue());
				}
		);
	}
}
