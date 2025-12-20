package farn.another_alpha_again.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import farn.another_alpha_again.AnotherAlphaAgain;
import farn.another_alpha_again.sub.GuiException;
import net.minecraft.client.Minecraft;
import net.minecraft.client.MinecraftApplet;
import net.minecraft.client.gui.screen.SaveConflictScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.world.World;
import net.minecraft.world.storage.exception.SessionLockException;
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
public abstract class MinecraftMixin {

	@Shadow
	public World world;

	@Shadow
	public abstract void setWorld(World world);

	@Shadow
	public abstract void openScreen(Screen screen);

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

	@WrapMethod(method="tick")
	public void wrapCrashTick(Operation<Void> original) {
		try {
			original.call();
		} catch (RuntimeException e) {
			e.printStackTrace();
			if(e instanceof SessionLockException) {
				this.world = null;
				this.setWorld(null);
				this.openScreen(new SaveConflictScreen());
			} else {
				this.setWorld(null);
				this.openScreen(new GuiException(e));
			}

		}
	}

	@WrapOperation(method="run", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/GameRenderer;render(F)V"))
	public void renderCrashed(GameRenderer instance, float tickDelta, Operation<Void> original) {
		try {
			original.call(instance, tickDelta);
		} catch (RuntimeException e) {
			e.printStackTrace();
			if(world != null) {
				this.setWorld(null);
			}
			this.openScreen(new GuiException(e));
		}
	}

	@Inject(method="<init>", at = @At("TAIL"))
	public void minecraftEarlyInit(Component component, Canvas canvas, MinecraftApplet minecraftApplet, int i, int j, boolean bl, CallbackInfo ci) {
		AnotherAlphaAgain.mc = (Minecraft) (Object)this;
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
		AnotherAlphaAgain.handleKeyPressed(Keyboard.getEventKey(), Keyboard.getEventKeyState());
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
		AnotherAlphaAgain.handleKeyPressed(Keyboard.getEventKey(), Keyboard.getEventKeyState());
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
			AnotherAlphaAgain.handleKeyPressed(Keyboard.getEventKey(), Keyboard.getEventKeyState());
	}

	@WrapOperation(method = "tick", at = @At(value = "INVOKE", target = "Lorg/lwjgl/input/Keyboard;getEventKey()I", ordinal = 4))
	public int redir(Operation<Integer> original)
	{
		if (Keyboard.getEventKey() == 63)
		{
			if (((Minecraft) (Object) this).options.debugEnabled)
			{
				if (!AnotherAlphaAgain.front)
				{
					AnotherAlphaAgain.front = true;
				}
				else
				{
					((Minecraft) (Object) this).options.debugEnabled = false;
					AnotherAlphaAgain.front = false;
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
