package farn.another_alpha_again.mixin.world;

import net.minecraft.crafting.recipe.ShapedRecipe;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(ShapedRecipe.class)
public interface ShapedRecipeAccessor {

	@Accessor("result")
	ItemStack getResult();

	@Accessor("ingredients")
	public int[] getIngredients();

	@Accessor("width")
	public int getWidth();

	@Accessor("height")
	public int getHeight();

}
