package io.ix0rai.ramel.mixin;

import org.quiltmc.config.api.Constraint;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(Constraint.Range.class)
public interface RangeAccessor {
	@Accessor("max")
	Object getMax();

	@Accessor("min")
	Object getMin();
}
