package io.ix0rai.ramel.client;

import com.mojang.serialization.Codec;
import folk.sisby.kaleido.lib.quiltconfig.api.Constraint;
import folk.sisby.kaleido.lib.quiltconfig.api.values.TrackedValue;
import io.ix0rai.ramel.Config;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.option.SimpleOptionsScreen;
import net.minecraft.client.option.GameOptions;
import net.minecraft.client.option.Option;
import net.minecraft.text.Text;
import org.jetbrains.annotations.Nullable;

import java.util.stream.StreamSupport;

@Environment(EnvType.CLIENT)
public class RamelConfigScreen extends SimpleOptionsScreen {
	@SuppressWarnings("unchecked")
	public RamelConfigScreen(@Nullable Screen parent) {
		super(parent, MinecraftClient.getInstance().options, Text.translatable("ramel.config.title"),
				StreamSupport.stream(Config.INSTANCE.values().spliterator(), false).map(value -> createOptional((TrackedValue<Float>) value)).toArray(Option[]::new)
		);
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

		return new Option<>(
				"ramel.config." + trackedValue.key().toString(),
				Option.constantTooltip(Text.translatable("ramel.config.tooltip." + trackedValue.key().toString())),
				(text, value) -> GameOptions.getGenericValueText(text, Text.translatable("ramel.config.value." + trackedValue.key().toString(), value)),
				new Option.IntRangeValueSet((int) ((float) range.min() * 10), (int) ((float) range.max() * 10)).withModifier(i -> (double) i / 10.0, double_ -> (int) (double_ * 10.0)),
				Codec.doubleRange((float) range.min(), (float) range.max()),
				(double) trackedValue.value(),
				value -> {
					trackedValue.setValue(value.floatValue());
				}
		);
	}
}