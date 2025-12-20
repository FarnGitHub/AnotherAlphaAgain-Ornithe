package farn.another_alpha_again.mixin.world;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import farn.another_alpha_again.AnotherAlphaAgain;
import net.minecraft.block.LeavesBlock;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;

import java.util.Random;

@Mixin(LeavesBlock.class)
public class LeavesBlockMixin {

	LeavesBlock blockThis = ((LeavesBlock)(Object)this);

	@WrapMethod(method="tick")
	public void tickWrap(World world, int x, int y, int z, Random random, Operation<Void> original) {
		if(AnotherAlphaAgain.getLeafDecayFix()) {
			if(!world.getMaterial(x, y - 1, z).isSolid()) {
				for(int i = x - 4; i <= x + 4; ++i) {
					for(int j = y - 3; j <= y; ++j) {
						for(int k = z - 4; k <= z + 4; ++k) {
							if(world.getBlock(i, j, k) == 17) {
								return;
							}
						}
					}
				}
				blockThis.dropItems(world, x, y, z, world.getBlockMetadata(x, y, z));
				world.setBlock(x, y, z, 0);
			}
		} else {
			original.call(world, x, y, z, random);
		}
	}
}
