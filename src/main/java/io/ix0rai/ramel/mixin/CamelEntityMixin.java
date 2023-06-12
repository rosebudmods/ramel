package io.ix0rai.ramel.mixin;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.passive.CamelEntity;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(CamelEntity.class)
public abstract class CamelEntityMixin extends LivingEntity {
    protected CamelEntityMixin(EntityType<? extends CamelEntity> entityType, World world) {
        super(entityType, world);
    }

    @SuppressWarnings("ConstantConditions")
    @Inject(method = "tick", at = @At("HEAD"))
    public void ram(CallbackInfo ci) {
        if (((Object) this) instanceof CamelEntity camel && camel.isDashing()) {
            List<Entity> toRam = this.getWorld().getOtherEntities(this, this.getBoundingBox().expand(0.5), Entity::isLiving);
            for (Entity entity : toRam) {
                if (entity instanceof LivingEntity living) {
                    living.playSound(SoundEvents.ENTITY_PLAYER_ATTACK_KNOCKBACK, 1f, 1f);
                    living.takeKnockback(0.4f, MathHelper.sin(this.getYaw() * ((float)Math.PI / 180)), -MathHelper.cos(this.getYaw() * ((float)Math.PI / 180)));
                }
            }
        }
    }
}
