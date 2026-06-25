package dev.rosebud.ramel.mixin;

import dev.rosebud.ramel.Config;
import net.minecraft.network.protocol.game.ClientboundSetEntityMotionPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.camel.Camel;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Camel.class)
public abstract class CamelEntityMixin extends LivingEntity {
    @Shadow public abstract boolean isDashing();

    private CamelEntityMixin(EntityType<? extends Camel> entityType, Level level) {
        super(entityType, level);
        throw new UnsupportedOperationException();
    }

    @Inject(method = "tick", at = @At("HEAD"))
    private void inject$tick(CallbackInfo ci) {
        if (!this.isDashing() || this.level().isClientSide()) {
            return;
        }

        /* Speed & Slowness affect knockback velocity */
        MobEffectInstance speedEffect = this.getEffect(MobEffects.SPEED);
        MobEffectInstance slowEffect = this.getEffect(MobEffects.SLOWNESS);
        int speedEffectModifier = speedEffect == null ? 0 : speedEffect.getAmplifier() + 1;
        int slowEffectModifier = slowEffect == null ? 0 : slowEffect.getAmplifier() + 1;
        double speedAdjustedImpact = Mth.clamp(this.getSpeed() * 1.65, .2, 3.0) + .25 * (speedEffectModifier - slowEffectModifier);

        /* Set parameters from config */
        float babyModifier = this.isBaby() ? .5F : 1.0F;
        float rammingRange = Config.INSTANCE.additionalRammingRange.value() * babyModifier;
        float rammingDamage = Config.INSTANCE.rammingDamage.value() * babyModifier;
        float knockbackMultiplier = Config.INSTANCE.knockbackMultiplier.value() * babyModifier;
        float knockupMultiplier = Config.INSTANCE.knockupMultiplier.value() * babyModifier;

        Entity sourceEntity = this.getControllingPassenger();
        DamageSource source = this.damageSources().mobAttack(sourceEntity instanceof LivingEntity attacker ? attacker : this);

        /* Apply knockback, knockup velocity and damage to all hit entities */
        this.level().getEntitiesOfClass(LivingEntity.class, this.getBoundingBox().inflate(rammingRange),
                /* Exclude this camel and its passengers from ramming effects */
                e -> e.isAlive() && e != this && !this.getPassengers().contains(e))
                .forEach(entity -> {
                    entity.playSound(SoundEvents.PLAYER_ATTACK_KNOCKBACK);
                    entity.hurt(source, rammingDamage);

                    double shieldedMultiplier = entity.isBlocking() ? .5 : 1.0;
                    double knockbackStrength = shieldedMultiplier * speedAdjustedImpact * knockbackMultiplier;
                    double knockupStrength = Mth.clamp(speedAdjustedImpact * 0.15 * knockupMultiplier, 0.0, 2.0);

                    entity.knockback(knockbackStrength, this.getX() - entity.getX(), this.getZ() - entity.getZ());
                    entity.push(0.0, knockupStrength, 0.0);

                    if (entity instanceof ServerPlayer player) {
                        player.connection.send(new ClientboundSetEntityMotionPacket(player));
                    }
                });
    }
}
