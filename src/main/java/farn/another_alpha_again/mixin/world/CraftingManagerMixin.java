package farn.another_alpha_again.mixin.world;

import farn.another_alpha_again.Main;
import farn.another_alpha_again.option.EnumOptions;
import net.minecraft.block.Block;
import net.minecraft.crafting.CraftingManager;
import net.minecraft.crafting.recipe.ShapedRecipe;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(CraftingManager.class)
public abstract class CraftingManagerMixin {

	@Shadow
	private List<ShapedRecipe> recipes;

	@Shadow
	abstract void registerShaped(ItemStack result, Object... args);

	@Inject(method = "<init>", at = @At("TAIL"))
	public void onInit(CallbackInfo ci) {
		if(Main.toggleDoubleSlabRecipe()) {
			for (int i = 0; i < this.recipes.size(); i++) {
				ShapedRecipe recipe = this.recipes.get(i);
				ShapedRecipeAccessor accessor = (ShapedRecipeAccessor) recipe;
				ItemStack output = accessor.getResult();

				if (output != null && (output.itemId == Block.STONE_SLAB.id || output.itemId == Block.STONE_STAIRS.id || output.itemId == Block.OAK_STAIRS.id)) {
					// Create a new ItemStack with count 6
					ItemStack newOutput = new ItemStack(output.itemId, 8);
					if(output.itemId == Block.STONE_SLAB.id) {
						newOutput = new ItemStack(output.itemId, 6);
					}

					// Create new ShapedRecipe with same width, height, ingredients, but new output stack
					ShapedRecipe newRecipe = new ShapedRecipe(
						accessor.getWidth(),
						accessor.getHeight(),
						accessor.getIngredients(),
						newOutput
					);

					this.recipes.set(i, newRecipe);
				}
			}
		}
		if((boolean)EnumOptions.WOOL_RECIPE.optionValue.value) {
			registerShaped(new ItemStack(Block.WOOL), "##", "##", '#', Item.STRING);
		}
	}
}
