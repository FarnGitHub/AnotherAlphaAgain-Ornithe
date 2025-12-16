package farn.another_alpha_again.mixin.world;

import net.minecraft.block.Block;
import net.minecraft.item.AxeItem;
import net.minecraft.item.PickaxeItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PickaxeItem.class)
public class PickAxeItemMixin {

	@Shadow
	private static Block[] EFFECTIVE_BLOCKS;

	@Inject(method="<clinit>", at = @At("TAIL"))
	private static void moreTools(CallbackInfo ci) {
		EFFECTIVE_BLOCKS = new Block[]{
			Block.COBBLESTONE,
			Block.DOUBLE_STONE_SLAB,
			Block.STONE_SLAB,
			Block.STONE,
			Block.MOSSY_COBBLESTONE,
			Block.IRON_ORE,
			Block.IRON_BLOCK,
			Block.COAL_ORE,
			Block.GOLD_BLOCK,
			Block.GOLD_ORE,
			Block.DIAMOND_ORE,
			Block.DIAMOND_BLOCK,
			Block.ICE,
			Block.REDSTONE_ORE,
			Block.LIT_REDSTONE_ORE,
			Block.FURNACE,
			Block.LIT_FURNACE,
			Block.RAIL,
			Block.STONE_STAIRS,
			Block.LEVER,
			Block.STONE_PRESSURE_PLATE,
			Block.IRON_DOOR,
			Block.STONE_BUTTON
		};
	}
}
