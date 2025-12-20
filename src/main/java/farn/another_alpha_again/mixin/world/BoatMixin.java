package farn.another_alpha_again.mixin.world;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import farn.another_alpha_again.AnotherAlphaAgain;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.vehicle.BoatEntity;
import net.minecraft.item.Item;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BoatEntity.class)
public class BoatMixin {

	@WrapOperation(method="tick", at = @At(value = "FIELD", target = "Lnet/minecraft/entity/vehicle/BoatEntity;collidingHorizontally:Z"))
	public boolean collidable(BoatEntity instance, Operation<Boolean> original) {
		return !AnotherAlphaAgain.boatBreakless() && original.call(instance);
	}

	@WrapOperation(
		method = "damage(Lnet/minecraft/entity/Entity;I)Z",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/entity/vehicle/BoatEntity;dropItem(IIF)Lnet/minecraft/entity/ItemEntity;"
		)
	)
	private ItemEntity redirectDropItem(BoatEntity boat, int i, int x, float v, Operation<ItemEntity> original) {
		if (i == Item.BOAT.id) {
			return boat.dropItem(Item.BOAT.id, 1, 0.0F);
		}
		return AnotherAlphaAgain.boatBreakless() ? null : original.call(boat, i,x,v);
	}

	@Inject(method="damage", at = @At(value="INVOKE", target = "Lnet/minecraft/entity/vehicle/BoatEntity;remove()V", shift = At.Shift.BEFORE))
	public void dropTheDamnBoat(Entity boat, int i, CallbackInfoReturnable<Boolean> cir) {
		((BoatEntity)(Object)this).dropItem(Item.BOAT.id, 1, 0.0F);
	}
}
