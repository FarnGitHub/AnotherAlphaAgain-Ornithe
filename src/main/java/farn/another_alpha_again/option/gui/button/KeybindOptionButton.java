package farn.another_alpha_again.option.gui.button;

import farn.another_alpha_again.option.EnumOptions;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import org.lwjgl.input.Keyboard;

public class KeybindOptionButton extends ButtonWidget implements OptionButton{
	EnumOptions enumOptions;
	boolean selected = false;

	public KeybindOptionButton(int id, EnumOptions enumOptions, Screen theScreen) {
		super(id, 0, 0, 256, 20, Keyboard.getKeyName((int)enumOptions.optionValue.value));
		this.enumOptions = enumOptions;
	}

	@Override
	public void onClicked() {
		if(!selected) {
			selected = true;
			this.message = "> " + Keyboard.getKeyName((int)this.enumOptions.optionValue.value) + " <";
		}
	}

	public void renderWithCustomPos(int x, int y, int mouseX, int mouseY, Minecraft mc) {
		this.x = x;
		this.y = y;
		super.render(mc, mouseX, mouseY);
	}

	@Override
	public void setFocused(boolean z1) {
		if(!z1) {
			selected = false;
			this.message = Keyboard.getKeyName((int)this.enumOptions.optionValue.value);
		}
	}

	public void onPressedKey(int keyIndex) {
		if(selected) {
			enumOptions.optionValue.value = keyIndex;
			setDisplayValue();
		}
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
		this.message = Keyboard.getKeyName((int)this.enumOptions.optionValue.value);
		selected = false;
	}
}
