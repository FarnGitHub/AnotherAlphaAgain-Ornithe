package farn.another_alpha_again.mixin.gui;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import farn.another_alpha_again.AnotherAlphaAgain;
import farn.another_alpha_again.sub.DebugHudExtraDrawer;
import net.minecraft.client.gui.GameGui;
import net.minecraft.client.gui.GuiElement;
import net.minecraft.client.render.TextRenderer;
import net.minecraft.client.render.Window;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(GameGui.class)
public abstract class IngameHudMixin extends GuiElement {

	@ModifyExpressionValue(method="render", at = @At(value = "INVOKE", target = "Lorg/lwjgl/input/Keyboard;isKeyDown(I)Z", ordinal = 0))
	public boolean modifyDebugViewCondition(boolean original) {
		return false;
	}

	@WrapOperation(method="render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/TextRenderer;drawWithShadow(Ljava/lang/String;III)V", ordinal = 4))
	public void renderExtra(TextRenderer instance, String text, int x, int y, int color, Operation<Void> original, @Local Window scaleRender) {
		DebugHudExtraDrawer.renderDebugHud(scaleRender);
	}
}
