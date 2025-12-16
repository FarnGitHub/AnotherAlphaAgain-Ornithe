package farn.another_alpha_again.mixin;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import farn.another_alpha_again.Main;
import net.minecraft.client.Minecraft;
import net.minecraft.client.options.GameOptions;
import net.minecraft.client.render.texture.TextureManager;
import org.lwjgl.Sys;
import org.lwjgl.opengl.Display;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.io.File;

@Mixin(GameOptions.class)
public class GameOptionsMixin {

	@Shadow
	public boolean fpsLimit;

	@Shadow
	public boolean anaglyph;

	@Inject(method="setValue(II)V", at = @At(value = "FIELD", target = "Lnet/minecraft/client/options/GameOptions;fpsLimit:Z", shift = At.Shift.AFTER))
	public void setVsync(int i, int j, CallbackInfo ci) {
		Display.setVSyncEnabled(fpsLimit);
	}

	@WrapOperation(method="setValue(II)V", at = @At(value = "FIELD", target = "Lnet/minecraft/client/options/GameOptions;anaglyph:Z", ordinal = 1))
	public void disableAnaglyph(GameOptions instance, boolean value, Operation<Void> original) {
		anaglyph = false;
	}

	@WrapOperation(method="setValue(II)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/texture/TextureManager;reload()V"))
	public void disableAnaglyphReload(TextureManager instance, Operation<Void> original) {
		Sys.openURL("file://" + Main.cfgFile.getAbsolutePath());
	}

	@Inject(method="<init>(Lnet/minecraft/client/Minecraft;Ljava/io/File;)V", at = @At("TAIL"))
	public void setVsyncInit(Minecraft minecraft, File dir, CallbackInfo ci) {
		Display.setVSyncEnabled(fpsLimit);
	}

	@WrapMethod(method="translateValue")
	public String overrideOptionsName(int i, Operation<String> original) {
		switch(i) {
			case 7: return "VSync: " + (this.fpsLimit ? "ON" : "OFF");
			case 6: return "Extra Option";
			default: return original.call(i);
		}
	}

	@Inject(method="save", at =@At("TAIL"))
	public void saveOptions(CallbackInfo ci) {
		Main.saveOptions(Main.cfgFile, true);
	}
}
