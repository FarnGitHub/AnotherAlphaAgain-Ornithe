package farn.another_alpha_again.mixin.world;

import farn.another_alpha_again.Main;
import net.minecraft.block.Block;
import net.minecraft.block.entity.FurnaceBlockEntity;
import net.minecraft.item.Item;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(FurnaceBlockEntity.class)
public class FurnaceBlockEntityMixin {

	@Inject(method="getResult", at = @At("HEAD"), cancellable = true)
	public void addWoodToCoalRecipe(int input, CallbackInfoReturnable<Integer> cir) {
		if(Main.logToCoalSmelt() && input == Block.LOG.id) {
			cir.setReturnValue(Item.COAL.id);
		}
	}
}
