package farn.another_alpha_again.mixin.skin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import farn.another_alpha_again.AnotherAlphaAgain;
import net.minecraft.client.entity.living.player.RemotePlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(RemotePlayerEntity.class)
public class MultiPlayerSkinMixin {

	@WrapOperation(method="<init>", at = @At(value = "FIELD", target = "Lnet/minecraft/client/entity/living/player/RemotePlayerEntity;skin:Ljava/lang/String;", ordinal = 0))
	public void skinFix(RemotePlayerEntity instance, String value, Operation<Void> original, @Local(argsOnly = true, index = 2) String username) {
		instance.skin = AnotherAlphaAgain.formatSkinFix(username);
	}

}
