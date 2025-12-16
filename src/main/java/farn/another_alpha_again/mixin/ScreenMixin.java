package farn.another_alpha_again.mixin;

import farn.another_alpha_again.Main;
import net.minecraft.client.gui.screen.Screen;
import org.lwjgl.input.Keyboard;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Screen.class)
public class ScreenMixin {

	@Inject(method = "handleInputs", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screen/Screen;handleKeyboard()V", shift = At.Shift.BEFORE))
	public void keyInput(CallbackInfo ci) {
		Main.handleKeyPressed(Keyboard.getEventKey(), Keyboard.getEventKeyState());
	}

}
