package farn.another_alpha_again.option.gui.button;

import farn.another_alpha_again.option.EnumOptions;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;

public class BooleanOptionButton extends ButtonWidget implements OptionButton{
	EnumOptions enumOptions;

	public BooleanOptionButton(int id, EnumOptions enumOptions, Screen theScreen) {
		super(id, 0, 0, 256, 20, (boolean)enumOptions.optionValue.value ? "ON" : "OFF");
		this.enumOptions = enumOptions;
	}

	@Override
	public void onClicked() {
		this.enumOptions.optionValue.value = !(boolean)this.enumOptions.optionValue.value;
		setDisplayValue();
	}

	public void renderWithCustomPos(int x, int y, int mouseX, int mouseY, Minecraft mc) {
		this.x = x;
		this.y = y;
		super.render(mc, mouseX, mouseY);
	}

	@Override
	public void setFocused(boolean z1) {

	}

	@Override
	public int getWidth() {
		return this.width;
	}

	@Override
	public EnumOptions getOptionsEnum() {
		return enumOptions;
	}

	@Override
	public void setDisplayValue() {
		this.message = (boolean)enumOptions.optionValue.value ? "ON" : "OFF";
	}
}
