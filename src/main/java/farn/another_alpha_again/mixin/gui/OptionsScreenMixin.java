package farn.another_alpha_again.mixin.gui;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import farn.another_alpha_again.option.gui.AnotherAlphaOptionScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.options.OptionsScreen;
import net.minecraft.client.options.GameOptions;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(OptionsScreen.class)
public class OptionsScreenMixin extends Screen {

	@WrapOperation(method="buttonClicked", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/options/GameOptions;setValue(II)V"))
	public void setValueWrapped(GameOptions instance, int i, int j, Operation<Void> original) {
		if(i == 6) {
			this.minecraft.openScreen(new AnotherAlphaOptionScreen((OptionsScreen)(Object)this));
		} else {
			original.call(instance,i,j);
		}
	}
}
