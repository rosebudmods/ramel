package io.ix0rai.ramel.mixin;

import io.ix0rai.ramel.Config;
import java.util.Objects;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.passive.CamelEntity;
import net.minecraft.network.packet.s2c.play.EntityVelocityUpdateS2CPacket;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(CamelEntity.class)
public abstract class CamelEntityMixin extends LivingEntity {
    @Shadow public abstract boolean isDashing();

    protected CamelEntityMixin(EntityType<? extends CamelEntity> entityType, World world) {
        super(entityType, world);
    }

    @Inject(method = "tick", at = @At("HEAD"))
    private void inject$tick(CallbackInfo ci) {
        if (!this.isDashing() || this.getWorld().isClient()) {
            return;
        }

        int speedEffectModifier = this.hasStatusEffect(StatusEffects.SPEED) ? Objects.requireNonNull(this.getStatusEffect(StatusEffects.SPEED)).getAmplifier() + 1 : 0;
        int slowEffectModifier = this.hasStatusEffect(StatusEffects.SLOWNESS) ? Objects.requireNonNull(this.getStatusEffect(StatusEffects.SLOWNESS)).getAmplifier() + 1 : 0;
        double speedAdjustedImpact = MathHelper.clamp(this.getMovementSpeed() * 1.65, .2, 3.0) + .25 * (speedEffectModifier - slowEffectModifier);

        float rammingRange = Config.INSTANCE.additionalRammingRange.value() * (isBaby() ? 0.5F : 1.0F);
        float rammingDamage = Config.INSTANCE.rammingDamage.value() * (isBaby() ? 0.5F : 1.0F);
        float knockBackMultiplier = Config.INSTANCE.knockbackMultiplier.value() * (isBaby() ? 0.5F : 1.0F);

        DamageSource source = this.getDamageSources().mobAttack(this.getPrimaryPassenger() != null ? this.getPrimaryPassenger() : this);

        this.getWorld().getOtherEntities(this, this.getBounds().expand(rammingRange), Entity::isAlive).stream()
                .filter(e -> e instanceof LivingEntity && !this.getPassengerList().contains(e))
                .forEach(e -> {
                    LivingEntity entity = (LivingEntity) e;

                    entity.playSound(SoundEvents.ENTITY_PLAYER_ATTACK_KNOCKBACK);
                    entity.damage(source, rammingDamage);
                    final double blockedImpact = entity.blockedByShield(source) ? .5 : 1.0;

                    entity.takeKnockback(blockedImpact * speedAdjustedImpact * knockBackMultiplier,
                            MathHelper.sin(this.getPitch() * ((float) Math.PI / 180)), -MathHelper.cos(this.getPitch() * ((float) Math.PI / 180)));
                    if (entity instanceof ServerPlayerEntity player) {
                        // The player won't feel any effects if we don't update the velocity
                        player.networkHandler.send(new EntityVelocityUpdateS2CPacket(player));
                    }
                });
    }
}
