package farn.another_alpha_again.mixin.skin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import farn.another_alpha_again.Main;
import net.minecraft.client.Session;
import net.minecraft.client.entity.living.player.InputPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InputPlayerEntity.class)
public class InputPlayerSkinMixin {

	@WrapOperation(method="<init>", at = @At(value = "FIELD", target = "Lnet/minecraft/client/entity/living/player/InputPlayerEntity;skin:Ljava/lang/String;", ordinal = 0))
	public void skinFix(InputPlayerEntity instance, String value, Operation<Void> original, @Local(argsOnly = true, index = 3) Session session) {
		instance.skin = Main.formatSkinFix(session.username);
	}



}
