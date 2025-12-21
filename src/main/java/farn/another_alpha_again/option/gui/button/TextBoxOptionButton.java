package farn.another_alpha_again.option.gui.button;

import farn.another_alpha_again.option.EnumOptions;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiElement;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.render.TextRenderer;

public class TextBoxOptionButton extends GuiElement implements OptionButton{
	private final TextRenderer fontRenderer;
	private int xPos;
	private int yPos;
	private final int width;
	private final int height;
	private String text;
	private int cursorCounter;
	public boolean isFocused = false;
	public boolean isEnabled = true;
	private EnumOptions enumOptions;

	public TextBoxOptionButton(TextRenderer kd2, EnumOptions theEnum) {
		this.fontRenderer = kd2;
		this.xPos = 0;
		this.yPos = 0;
		this.width = 256;
		this.height = 20;
		this.enumOptions = theEnum;
		this.setText((String) theEnum.optionValue.value);
	}

	public void setText(String string1) {
		this.text = string1;
		enumOptions.optionValue.value = this.text;
	}

	public String getText() {
		return this.text;
	}

	public void textboxKeyTyped(char c1, int i2) {
		if(this.isEnabled && this.isFocused) {
			if(c1 == 22) {
				String clipboardText = Screen.getClipboard();
				if(clipboardText == null) {
					clipboardText = "";
				}

				int i4 = 32 - this.text.length();
				if(i4 > clipboardText.length()) {
					i4 = clipboardText.length();
				}

				if(i4 > 0) {
					this.text = this.text + clipboardText.substring(0, i4);
				}
			}

			if(i2 == 14 && this.text.length() > 0) {
				this.text = this.text.substring(0, this.text.length() - 1);
			}

			if(getAllowedCharacters().indexOf(c1) >= 0) {
				this.text = this.text + c1;
			}
		}

	}

	public void setFocused(boolean z1) {
		if(z1 && !this.isFocused) {
			this.cursorCounter = 0;
		}

		this.isFocused = z1;
	}

	@Override
	public int getWidth() {
		return this.width;
	}

	public void drawTextBox() {
		this.fill(this.xPos - 1, this.yPos - 1, this.xPos + this.width + 1, this.yPos + this.height + 1, -6250336);
		this.fill(this.xPos, this.yPos, this.xPos + this.width, this.yPos + this.height, 0xFF000000);
		if(this.isEnabled) {
			boolean z1 = this.isFocused && this.cursorCounter / 6 % 2 == 0;
			this.drawString(this.fontRenderer, this.text + (z1 ? "_" : ""), this.xPos + 4, this.yPos + (this.height - 8) / 2, 14737632);
		} else {
			this.drawString(this.fontRenderer, this.text, this.xPos + 4, this.yPos + (this.height - 8) / 2, 7368816);
		}

	}

	private static String getAllowedCharacters() {
		return " !\"#$%&\'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_\'abcdefghijklmnopqrstuvwxyz{|}~\u2302\u00c7\u00fc\u00e9\u00e2\u00e4\u00e0\u00e5\u00e7\u00ea\u00eb\u00e8\u00ef\u00ee\u00ec\u00c4\u00c5\u00c9\u00e6\u00c6\u00f4\u00f6\u00f2\u00fb\u00f9\u00ff\u00d6\u00dc\u00f8\u00a3\u00d8\u00d7\u0192\u00e1\u00ed\u00f3\u00fa\u00f1\u00d1\u00aa\u00ba\u00bf\u00ae\u00ac\u00bd\u00bc\u00a1\u00ab\u00bb";
	}

	@Override
	public void onClicked() {

	}

	@Override
	public void renderWithCustomPos(int x, int y, int mouseX, int mouseY, Minecraft mc) {
		this.xPos = x;
		this.yPos = y;
		drawTextBox();
	}

	@Override
	public EnumOptions getOptionsEnum() {
		return enumOptions;
	}

	@Override
	public void setDisplayValue() {
		setText((String) getOptionsEnum().optionValue.value);
	}
}
