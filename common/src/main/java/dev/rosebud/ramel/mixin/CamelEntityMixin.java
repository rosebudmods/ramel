package dev.rosebud.ramel.mixin;

import com.google.common.base.MoreObjects;
import dev.rosebud.ramel.Config;
import net.minecraft.network.protocol.game.ClientboundSetEntityMotionPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.camel.Camel;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Objects;

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

        ServerLevel level = (ServerLevel) this.level();

        int speedEffectModifier = this.hasEffect(MobEffects.SPEED) ? Objects.requireNonNull(this.getEffect(MobEffects.SPEED)).getAmplifier() + 1 : 0;
        int slowEffectModifier = this.hasEffect(MobEffects.SLOWNESS) ? Objects.requireNonNull(this.getEffect(MobEffects.SLOWNESS)).getAmplifier() + 1 : 0;
        double speedAdjustedImpact = Mth.clamp(this.getSpeed() * 1.65, .2, 3.0) + .25 * (speedEffectModifier - slowEffectModifier);

        float babyModifier = this.isBaby() ? 0.5F : 1.0F;
        float rammingRange = Config.INSTANCE.additionalRammingRange.value() * babyModifier;
        float rammingDamage = Config.INSTANCE.rammingDamage.value() * babyModifier;
        float knockbackMultiplier = Config.INSTANCE.knockbackMultiplier.value() * babyModifier;
        float knockupMultiplier = Config.INSTANCE.knockupMultiplier.value() * babyModifier;

        DamageSource source = this.damageSources().mobAttack(MoreObjects.firstNonNull(this.getControllingPassenger(), this));

        level.getEntities(this, this.getBoundingBox().inflate(rammingRange), Entity::isAlive).stream()
                .filter(e -> e instanceof LivingEntity && !this.getPassengers().contains(e))
                .forEach(e -> {
                    LivingEntity entity = (LivingEntity) e;
                    boolean blockedImpact = ramel$blocksImpact(entity, source);

                    if (entity.hurtServer(level, source, rammingDamage)) {
                        EnchantmentHelper.doPostAttackEffects(level, entity, source);
                    }

                    entity.playSound(SoundEvents.PLAYER_ATTACK_KNOCKBACK);

                    double knockbackStrength = (blockedImpact ? .5 : 1.0) * speedAdjustedImpact * knockbackMultiplier;
                    double knockupStrength = Mth.clamp(speedAdjustedImpact * 0.15 * knockupMultiplier, 0.0, 2.0);

                    entity.knockback(knockbackStrength, this.getX() - entity.getX(), this.getZ() - entity.getZ());
                    entity.push(0.0, knockupStrength, 0.0);
                    if (entity instanceof ServerPlayer player) {
                        // The player won't feel any effects if we don't update the velocity
                        player.connection.send(new ClientboundSetEntityMotionPacket(player));
                    }
                });
    }

    // moj removed LivingEntity.isDamageSourceBlocked after 1.21.1 and this is the best i can do for directional blocking
    @Unique
    private static boolean ramel$blocksImpact(LivingEntity entity, DamageSource source) {
    Vec3 sourcePosition = source.getSourcePosition();
    if (!entity.isBlocking() || sourcePosition == null) return false;

    Vec3 view = entity.calculateViewVector(0.0F, entity.getYHeadRot());
    Vec3 direction = sourcePosition.subtract(entity.position()).multiply(1.0, 0.0, 1.0).normalize();
    return direction.dot(view) > 0.0;
  }
}
