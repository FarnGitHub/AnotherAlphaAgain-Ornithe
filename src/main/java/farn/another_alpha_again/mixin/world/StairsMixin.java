package farn.another_alpha_again.mixin.world;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import farn.another_alpha_again.AnotherAlphaAgain;
import net.minecraft.block.StairsBlock;
import net.minecraft.entity.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

import java.util.Random;

@Mixin(StairsBlock.class)
public class StairsMixin {

	StairsBlock theBlock = (StairsBlock) (Object) (this);

	@WrapMethod(method="getDropItem")
	public int dropStairs(int metadata, Random random, Operation<Integer> original) {
		return AnotherAlphaAgain.dropStairs() ? this.theBlock.id : original.call(metadata, random);
	}

	@WrapMethod(method="dropItems(Lnet/minecraft/world/World;IIIIF)V")
	public void dropSelfItem1(World world, int x, int y, int z, int metadata, float luck, Operation<Void> original) {
		if (AnotherAlphaAgain.dropStairs()) {
			actualDropItem(world,x,y,z);
		} else {
			original.call(world, x, y, z, metadata, luck);
		}
	}

	@WrapMethod(method="dropItems(Lnet/minecraft/world/World;IIII)V")
	public void dropSelfItem1(World world, int x, int y, int z, int metadata, Operation<Void> original) {
		if (AnotherAlphaAgain.dropStairs()) {
			actualDropItem(world,x,y,z);
		} else {
			original.call(world, x, y, z, metadata);
		}
	}

	@Unique
	public void actualDropItem(World world, int x, int y, int z) {
		if (!world.isMultiplayer) {
			float f = 0.7F;
			double d = (double) (world.random.nextFloat() * f) + (double) (1.0F - f) * 0.5D;
			double d1 = (double) (world.random.nextFloat() * f) + (double) (1.0F - f) * 0.5D;
			double d2 = (double) (world.random.nextFloat() * f) + (double) (1.0F - f) * 0.5D;
			ItemEntity entityitem = new ItemEntity(world, (double) x + d, (double) y + d1, (double) z + d2, new ItemStack(theBlock.id));
			entityitem.pickUpDelay = 10;
			world.addEntity(entityitem);
		}
	}

}
