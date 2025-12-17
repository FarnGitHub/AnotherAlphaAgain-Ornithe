package farn.another_alpha_again.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import farn.another_alpha_again.Main;
import net.minecraft.client.Minecraft;
import net.minecraft.client.MinecraftApplet;
import net.minecraft.client.gui.screen.Screen;
import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.PixelFormat;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.awt.*;

@Mixin(Minecraft.class)
public class MinecraftMixin {

	@WrapOperation(method="init", at = @At(value = "INVOKE", target = "Lorg/lwjgl/opengl/Display;create()V", ordinal = 0))
	public void createDepthBitFix(Operation<Void> original) throws LWJGLException {
		PixelFormat pixelformat = new PixelFormat();
		pixelformat = pixelformat.withDepthBits(24);
		Display.create(pixelformat);
	}

	@ModifyExpressionValue(method="run", at = @At(value="FIELD", target = "Lnet/minecraft/client/options/GameOptions;fpsLimit:Z"))
	public boolean cancelFpsLimit(boolean original) {
		return false;
	}

	@Inject(method="<init>", at = @At("TAIL"))
	public void minecraftEarlyInit(Component component, Canvas canvas, MinecraftApplet minecraftApplet, int i, int j, boolean bl, CallbackInfo ci) {
		Main.mc = (Minecraft) (Object)this;
	}

	@Inject(
		method = "tick",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/client/gui/screen/Screen;handleKeyboard()V",
			shift = At.Shift.AFTER
		)
	)
	private void stationapi_keyPressInGUI(CallbackInfo ci) {
		Main.handleKeyPressed(Keyboard.getEventKey(), Keyboard.getEventKeyState());
	}

	@Inject(
		method = "tick",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/client/Minecraft;isMultiplayer()Z",
			shift = At.Shift.BEFORE,
			ordinal = 0
		)
	)
	private void stationapi_keyPressInGame(CallbackInfo ci) {
		Main.handleKeyPressed(Keyboard.getEventKey(), Keyboard.getEventKeyState());
	}

	@Inject(
		method = "tick()V",
		at = @At(
			value = "INVOKE",
			target = "Lorg/lwjgl/input/Keyboard;getEventKeyState()Z",
			ordinal = 1,
			shift = At.Shift.BEFORE,
			remap = false
		)
	)
	private void stationapi_keyReleased(CallbackInfo ci) {
		if (!Keyboard.getEventKeyState())
			Main.handleKeyPressed(Keyboard.getEventKey(), Keyboard.getEventKeyState());
	}

	@WrapOperation(method = "tick", at = @At(value = "INVOKE", target = "Lorg/lwjgl/input/Keyboard;getEventKey()I", ordinal = 4))
	public int redir(Operation<Integer> original)
	{
		if (Keyboard.getEventKey() == 63)
		{
			if (((Minecraft) (Object) this).options.debugEnabled)
			{
				if (!Main.front)
				{
					Main.front = true;
				}
				else
				{
					((Minecraft) (Object) this).options.debugEnabled = false;
					Main.front = false;
				}
			}
			else
			{
				((Minecraft) (Object) this).options.debugEnabled = true;
			}
		}
		return 0;
	}
}
