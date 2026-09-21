package dyl.csu.mixin;


import dyl.csu.utils.FutureBlockBreak;
import dyl.csu.utils.LeafBreakHandler;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static net.minecraft.registry.tag.BlockTags.LOGS;

@Mixin(Block.class)
public class LogBreakMixin {

    @Inject(
            method = "afterBreak",
            at = @At("RETURN")
    )
    private void afterBreak(World world, PlayerEntity player, BlockPos pos, BlockState state, BlockEntity blockEntity, ItemStack tool, CallbackInfo ci) {
        if (!world.isClient() && (state.getBlock() instanceof PillarBlock) && state.equals(LOGS)) {
            BlockPos upPosition = pos.up();
            BlockState upState = world.getBlockState(upPosition);

            // trigger chain break on the leaf block above a log
            if (upState.getBlock() instanceof LeavesBlock) {
                FutureBlockBreak futureLeafBreak = new FutureBlockBreak((ServerWorld) world, upPosition, 0);
                LeafBreakHandler.addFutureBreak(futureLeafBreak);
            }
        }
    }
}