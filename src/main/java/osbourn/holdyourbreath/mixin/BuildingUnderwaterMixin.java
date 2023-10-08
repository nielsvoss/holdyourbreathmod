package osbourn.holdyourbreath.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.DoorBlock;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemPlacementContext;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(BlockItem.class)
abstract class BuildingUnderwaterMixin extends Item {
    @Shadow public abstract Block getBlock();

    public BuildingUnderwaterMixin(Settings settings) {
        super(settings);
    }

    @ModifyExpressionValue(
            method = "place(Lnet/minecraft/item/ItemPlacementContext;)Lnet/minecraft/util/ActionResult;",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemPlacementContext;canPlace()Z"))
    private boolean preventPlacingUnderwater(boolean original, ItemPlacementContext context) {
        boolean isWater = context.getWorld().getBlockState(context.getBlockPos()).getBlock().equals(Blocks.WATER);
        boolean isForbiddenBlock = this.getBlock() instanceof DoorBlock;
        return original && !(isWater && isForbiddenBlock);
    }
}
