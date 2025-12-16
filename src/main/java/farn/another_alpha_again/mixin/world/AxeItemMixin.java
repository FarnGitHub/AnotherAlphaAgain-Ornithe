package farn.another_alpha_again.mixin.world;

import net.minecraft.block.Block;
import net.minecraft.item.AxeItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AxeItem.class)
public class AxeItemMixin {

	@Shadow
	private static Block[] EFFECTIVE_BLOCKS;

	@Inject(method="<clinit>", at = @At("TAIL"))
	private static void moreTools(CallbackInfo ci) {
		EFFECTIVE_BLOCKS = new Block[]{Block.PLANKS, Block.BOOKSHELF, Block.LOG, Block.CHEST, Block.FENCE, Block.JUKEBOX, Block.WOODEN_PRESSURE_PLATE, Block.WALL_SIGN, Block.WOODEN_DOOR, Block.STANDING_SIGN, Block.CRAFTING_TABLE, Block.OAK_STAIRS, Block.BOOKSHELF, Block.FENCE, Block.WOODEN_PRESSURE_PLATE};
	}
}
