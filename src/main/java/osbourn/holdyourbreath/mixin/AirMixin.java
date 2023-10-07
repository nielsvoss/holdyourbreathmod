package osbourn.holdyourbreath.mixin;

import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.registry.tag.FluidTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import osbourn.holdyourbreath.BreathingManager;
import osbourn.holdyourbreath.HoldYourBreath;

@Mixin(LivingEntity.class)
abstract class AirMixin extends Entity {
    @Shadow protected int playerHitTimer;

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
}
