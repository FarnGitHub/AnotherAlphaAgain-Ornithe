package farn.another_alpha_again.option.gui.button;

import farn.another_alpha_again.option.EnumOptions;
import net.minecraft.client.Minecraft;

public interface OptionButton {

	public void onClicked();

	public void renderWithCustomPos(int x, int y, int mouseX, int mouseY, Minecraft mc);

	public void setFocused(boolean z1);

	public int getWidth();

	public EnumOptions getOptionsEnum();

	public void setDisplayValue();
}
