package farn.another_alpha_again.mixin.gui;

import net.minecraft.client.gui.GuiElement;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(GuiElement.class)
public interface GuiAccessor {

	@Invoker("fill")
	void fillRect(int x, int y, int sizeX, int sizeY, int color);
}
