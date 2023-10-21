package osbourn.holdyourbreath.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.registry.tag.FluidTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import osbourn.holdyourbreath.HoldYourBreath;
import osbourn.holdyourbreath.HoldYourBreathConfig;

@Mixin(LivingEntity.class)
abstract class AirMixin extends Entity {
    public AirMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    @Inject(method = "baseTick", at = @At("HEAD"))
    public void recordDrowning(CallbackInfo ci) {
        if (!this.getWorld().isClient()) {
            if ((Object) this instanceof PlayerEntity player) {
                if (this.isSubmergedIn(FluidTags.WATER) && !this.getWorld().getBlockState(BlockPos.ofFloored(this.getX(), this.getEyeY(), this.getZ())).isOf(Blocks.BUBBLE_COLUMN)) {
                    if (!HoldYourBreath.breathingManager.isHoldingBreath(player)) {
                        HoldYourBreath.breathingManager.setDrowning(player, true);
                    }
                } else {
                    HoldYourBreath.breathingManager.setDrowning(player, false);
                }
            }
        }
    }

    /**
     * Also see the similarly named method in AirMixinClient, which is for display only.
     */
    @ModifyExpressionValue(method = "baseTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;canBreatheInWater()Z"))
    public boolean onlyReduceAirIfDrowning(boolean originalReturnValue) {
        if (HoldYourBreathConfig.breathHoldingEnabled) {
            if (!this.getWorld().isClient()) {
                if ((Object) this instanceof PlayerEntity player) {
                    if (!HoldYourBreath.breathingManager.isDrowning(player)) {
                        return true;
                    }
                }
            }
        }
        return originalReturnValue;
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

    @ModifyConstant(method = "baseTick", constant = @Constant(floatValue = 2.0F))
    public float modifyDrowningDamage(float originalDamageAmount) {
        // Make player move downward
        if (!this.getWorld().isClient() && HoldYourBreathConfig.drowningDamageDownwardForce != 0.0F) {
            this.addVelocity(0.0, -HoldYourBreathConfig.drowningDamageDownwardForce, 0.0);
            this.velocityModified = true;
        }

        return originalDamageAmount * HoldYourBreathConfig.drowningDamageMultiplier;
    }
}
