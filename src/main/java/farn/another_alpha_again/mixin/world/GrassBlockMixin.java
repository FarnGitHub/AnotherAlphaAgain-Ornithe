package farn.another_alpha_again.mixin.world;

import net.minecraft.block.Block;
import net.minecraft.block.GrassBlock;
import net.minecraft.block.material.Material;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(GrassBlock.class)
public class GrassBlockMixin extends Block {
	protected GrassBlockMixin(int id) {
		super(id, Material.GRASS);
	}

	public int getSprite(int face) {
		return face == 1 ? 0 : (face == 0 ? 2 : 3);
	}
}
