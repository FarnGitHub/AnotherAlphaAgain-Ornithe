package farn.another_alpha_again.option.gui;

import farn.another_alpha_again.AnotherAlphaAgain;
import farn.another_alpha_again.option.EnumOptions;
import farn.another_alpha_again.option.gui.button.KeybindOptionButton;
import farn.another_alpha_again.option.gui.button.OptionButton;
import farn.another_alpha_again.option.gui.button.TextBoxOptionButton;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.OptionButtonWidget;
import org.lwjgl.opengl.GL11;

public class AnotherAlphaOptionScreen extends Screen {
	protected Screen parent;
	private OptionEntryListWidget guiTexturePackSlot;

	public AnotherAlphaOptionScreen(Screen guiScreen1) {
		this.parent = guiScreen1;
		if(guiScreen1 instanceof AnotherAlphaOptionScreen) {
			this.parent = ((AnotherAlphaOptionScreen) guiScreen1).parent;

		}
	}

	public void init() {
		this.buttons.clear();
		this.buttons.add(new OptionButtonWidget(5, this.width / 2 - 154, this.height - 48, "Reset"));
		this.buttons.add(new OptionButtonWidget(6, this.width / 2 + 4, this.height - 48, "Done"));
		this.guiTexturePackSlot = new OptionEntryListWidget(this);
		this.guiTexturePackSlot.registerScrollButtons(this.buttons, 7, 8);
	}

	protected void keyPressed(char chr, int key) {
		boolean superKeyPresed = true;
		try {
			OptionButton optionButton = guiTexturePackSlot.theOptionButtons[guiTexturePackSlot.selectedSlot];
			if(optionButton instanceof TextBoxOptionButton) {
				((TextBoxOptionButton)optionButton).textboxKeyTyped(chr, key);
			} else if(optionButton instanceof KeybindOptionButton) {
				if(key <= 1) {
					superKeyPresed = false;
					((KeybindOptionButton)optionButton).onPressedKey(0);
				} else {
					((KeybindOptionButton)optionButton).onPressedKey(key);
				}
			}
		} catch (Exception e) {
		}
		if(superKeyPresed) super.keyPressed(chr, key);
	}

	protected void buttonClicked(ButtonWidget button) {
		if(button.active) {
			if(button.id == 6) {
				AnotherAlphaAgain.saveOptions(AnotherAlphaAgain.cfgFile, true);
				this.minecraft.openScreen(this.parent);
			} else if(button.id == 5) {
				for(OptionButton theEnum : this.guiTexturePackSlot.theOptionButtons) {
					theEnum.getOptionsEnum().optionValue.value = theEnum.getOptionsEnum().finalValue;
					theEnum.setDisplayValue();
				}
				AnotherAlphaAgain.saveOptions(AnotherAlphaAgain.cfgFile, true);
			} else {
				this.guiTexturePackSlot.actionPerformed(button);
			}
		}

	}

	public void render(int mouseX, int mouseY, float renderPartialTick) {
		GL11.glPushAttrib(GL11.GL_ENABLE_BIT | GL11.GL_COLOR_BUFFER_BIT);
		this.guiTexturePackSlot.drawScreen(mouseX, mouseY, renderPartialTick);
		this.drawCenteredString(this.textRenderer, "Extra Option", this.width / 2, 16, 0xFFFFFF);
		super.render(mouseX, mouseY, renderPartialTick);
		GL11.glPopAttrib();
	}

	public Minecraft getMinecraft() {
		return this.minecraft;
	}
}
