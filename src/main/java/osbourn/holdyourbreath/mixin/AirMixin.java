package osbourn.holdyourbreath.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import osbourn.holdyourbreath.HoldYourBreath;
import osbourn.holdyourbreath.HoldYourBreathConfig;

@Mixin(LivingEntity.class)
abstract class AirMixin extends Entity {
    public AirMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    @Unique
    private boolean holdYourBreath_wasPlayerUnderwaterThisTick = false;

    @Inject(method = "baseTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;getNextAirUnderwater(I)I"))
    public void recordDrowning(CallbackInfo ci) {
        if (!this.getWorld().isClient()) {
            if ((Object) this instanceof PlayerEntity player) {
                holdYourBreath_wasPlayerUnderwaterThisTick = true;
                if (!HoldYourBreath.breathingManager.isHoldingBreath(player)) {
                    HoldYourBreath.breathingManager.setDrowning(player, true);
                }
            }
        }
    }

    @Inject(method = "baseTick", at = @At("RETURN"))
    public void recordNotDrowning(CallbackInfo ci) {
        if (!this.getWorld().isClient()) {
            if ((Object) this instanceof PlayerEntity player) {
                if (holdYourBreath_wasPlayerUnderwaterThisTick) {
                    // The player might still be underwater when we set this to false, but in that case it will probably
                    // get set to true next tick by recordDrowning
                    holdYourBreath_wasPlayerUnderwaterThisTick = false;

                    if (HoldYourBreathConfig.allowRecoveringBreathUnderwater && HoldYourBreath.breathingManager.isHoldingBreath(player)) {
                        HoldYourBreath.breathingManager.setDrowning(player, false);
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
    @ModifyExpressionValue(method = "baseTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;getNextAirUnderwater(I)I"))
    public int onlyReduceAirIfDrowning(int originalReturnValue) {
        if (HoldYourBreathConfig.breathHoldingEnabled) {
            if (!this.getWorld().isClient()) {
                if ((Object) this instanceof PlayerEntity player) {
                    if (!HoldYourBreath.breathingManager.isDrowning(player)) {
                        return this.getAir();
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
