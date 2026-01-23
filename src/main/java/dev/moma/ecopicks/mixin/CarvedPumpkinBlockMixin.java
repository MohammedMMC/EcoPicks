package dev.moma.ecopicks.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import dev.moma.ecopicks.entity.ModEntities;
import dev.moma.ecopicks.entity.custom.LeafeeEntity;
import net.minecraft.block.BlockState;
import net.minecraft.block.CarvedPumpkinBlock;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

@Mixin(CarvedPumpkinBlock.class)
public class CarvedPumpkinBlockMixin {
	@Inject(at = @At("TAIL"), method = "onBlockAdded")
	private void onLeafeePlaced(BlockState state, World world, BlockPos pos, BlockState oldState, boolean notify, CallbackInfo ci) {
		if (!oldState.isOf(state.getBlock()) && !world.isClient) {
			BlockPos belowPos = pos.down();
			BlockState belowState = world.getBlockState(belowPos);
			if (belowState.isIn(BlockTags.LEAVES)) {
				world.removeBlock(pos, false);
				world.removeBlock(belowPos, false);

				LeafeeEntity leafee = new LeafeeEntity(ModEntities.LEAFEE, world);
				leafee.setPosition(belowPos.getX() + 0.5, belowPos.getY(), belowPos.getZ() + 0.5);
				world.spawnEntity(leafee);
			}
		}
	}
}