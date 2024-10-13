package dev.rosebud.ramel.client;

import com.mojang.serialization.Codec;
import folk.sisby.kaleido.lib.quiltconfig.api.Constraint;
import folk.sisby.kaleido.lib.quiltconfig.api.values.TrackedValue;
import dev.rosebud.ramel.Config;
import net.minecraft.client.Minecraft;
import net.minecraft.client.OptionInstance;
import net.minecraft.client.Options;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.options.OptionsSubScreen;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.Nullable;

import java.util.stream.StreamSupport;

public class RamelConfigScreen extends OptionsSubScreen {
	public RamelConfigScreen(@Nullable Screen parent) {
		super(parent, Minecraft.getInstance().options, Component.translatable("ramel.config.title"));
	}

	@Override
    @SuppressWarnings({"unchecked", "DataFlowIssue"})
	protected void addOptions() {
		this.list.addSmall(StreamSupport.stream(Config.INSTANCE.values().spliterator(), false)
				.map(value -> createOptional((TrackedValue<Float>) value)).toArray(OptionInstance[]::new));
	}

	private static OptionInstance<Float> createOptional(TrackedValue<Float> trackedValue) {
		Constraint.Range<Float> range = null;

		for (Constraint<Float> c : trackedValue.constraints()) {
			if (c instanceof Constraint.Range<Float> constraintRange) {
				range = constraintRange;
			}
		}

		String stringValue = trackedValue.key().toString();

		if (range == null) {
			throw new RuntimeException("value must have float range constraint: " + stringValue);
		}

		float min = range.min();
		float max = range.max();

		return new OptionInstance<>("ramel.config." + stringValue, OptionInstance.cachedConstantTooltip(Component.translatable("ramel.config.tooltip." + stringValue)), (text, value) -> Options.genericValueLabel(text, Component.translatable("ramel.config.value." + stringValue, value)), (new OptionInstance.IntRange((int) (min * 10), (int) (max * 10))).xmap((intValue) -> intValue / 10.0F, (doubleValue) -> (int) (doubleValue * 10.0D)), Codec.floatRange(min, max), trackedValue.value(), trackedValue::setValue);
	}
}
