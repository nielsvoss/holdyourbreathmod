package osbourn.holdyourbreath.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.registry.tag.FluidTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import osbourn.holdyourbreath.BreathingManager;
import osbourn.holdyourbreath.HoldYourBreath;
import osbourn.holdyourbreath.HoldYourBreathConfig;

@Mixin(LivingEntity.class)
abstract class AirMixin extends Entity {
    public AirMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    @Inject(method = "baseTick", at = @At("HEAD"))
    public void recordDrowning(CallbackInfo ci) {
        if ((Object)this instanceof PlayerEntity player) {
            if (this.isSubmergedIn(FluidTags.WATER) && !this.getWorld().getBlockState(BlockPos.ofFloored(this.getX(), this.getEyeY(), this.getZ())).isOf(Blocks.BUBBLE_COLUMN)) {
                if (HoldYourBreath.breathingManager.getBreathingState(player) != BreathingManager.BreathingState.HOLDING_BREATH) {
                    HoldYourBreath.breathingManager.setDrowning(player, true);
                }
            } else {
                HoldYourBreath.breathingManager.setDrowning(player, false);
            }
        }
    }

    @Redirect(method = "baseTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;canBreatheInWater()Z"))
    public boolean onlyReduceAirIfDrowning(LivingEntity instance) {
        if (HoldYourBreathConfig.breathHoldingEnabled) {
            if (instance instanceof PlayerEntity player) {
                if (!HoldYourBreath.breathingManager.isDrowning(player)) {
                    return true;
                }
            }
        }
        return instance.canBreatheInWater();
    }

    @ModifyReturnValue(method = "getNextAirUnderwater", at = @At("RETURN"))
    public int modifyAirLossSpeed(int originalReturnValue, int originalAir) {
        if ((Object)this instanceof PlayerEntity) {
            int difference = originalAir - originalReturnValue;
            int newAir = originalAir - difference * HoldYourBreathConfig.airLossMultiplier;
            return Math.max(newAir, -20);
        } else {
            return originalReturnValue;
        }
    }

    @Redirect(method = "baseTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;damage(Lnet/minecraft/entity/damage/DamageSource;F)Z"))
    public boolean modifyDrowningDamage(LivingEntity instance, DamageSource source, float amount) {
        if (!source.equals(instance.getDamageSources().drown())) return instance.damage(source, amount);

        return instance.damage(source, amount * HoldYourBreathConfig.drowningDamageMultiplier);
    }
}
