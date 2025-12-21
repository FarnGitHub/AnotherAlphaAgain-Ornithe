package farn.another_alpha_again.option.gui;

import java.util.List;

import com.mojang.blaze3d.vertex.BufferBuilder;
import farn.another_alpha_again.AnotherAlphaAgain;
import farn.another_alpha_again.option.EnumOptionType;
import farn.another_alpha_again.option.EnumOptions;
import farn.another_alpha_again.option.gui.button.BooleanOptionButton;
import farn.another_alpha_again.option.gui.button.KeybindOptionButton;
import farn.another_alpha_again.option.gui.button.OptionButton;
import farn.another_alpha_again.option.gui.button.TextBoxOptionButton;
import net.minecraft.client.Minecraft;

import net.minecraft.client.gui.widget.ButtonWidget;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

public class OptionEntryListWidget {
	private final Minecraft mc;
	private final int width;
	private final int height;
	protected final int top = 32;
	protected final int bottom;
	private final int right;
	private final int left;
	protected final int posZ;
	private int scrollUpButtonID;
	private int scrollDownButtonID;
	private float initialClickY = -2.0F;
	private float scrollMultiplier;
	private float amountScrolled;
	private int selectedElement = -1;
	private long lastClicked = 0L;
	private int posX = 0;
	private int mouseX;
	private int mouseY;
	final AnotherAlphaOptionScreen optionScreen;
	OptionButton[] theOptionButtons = new OptionButton[EnumOptions.values().length];
	int selectedSlot = -1;

	public OptionEntryListWidget(AnotherAlphaOptionScreen guiTexturePacks1) {
		this.optionScreen = guiTexturePacks1;
		this.mc = guiTexturePacks1.getMinecraft();
		this.width = guiTexturePacks1.width;
		this.height = guiTexturePacks1.height;
		this.bottom = guiTexturePacks1.height - 55 + 4;
		this.posZ = 36;
		this.left = 0;
		this.right = guiTexturePacks1.width;
		for (EnumOptions theEnum : EnumOptions.values()) {
			if(theEnum.type == EnumOptionType.TOGGLE) {
				theOptionButtons[theEnum.ordinal()] = new BooleanOptionButton(theEnum.ordinal(), theEnum, guiTexturePacks1);
			} else if(theEnum.type == EnumOptionType.STRING) {
				theOptionButtons[theEnum.ordinal()] = new TextBoxOptionButton(this.mc.textRenderer, theEnum);
			} else if(theEnum.type == EnumOptionType.KEYBIND) {
				theOptionButtons[theEnum.ordinal()] = new KeybindOptionButton(theEnum.ordinal(), theEnum, guiTexturePacks1);
			}
		}
	}

	protected void func_27260_a(int i1, int i2, BufferBuilder tessellator3) {
	}

	protected void func_27255_a(int i1, int i2) {
	}

	protected void func_27257_b(int i1, int i2) {
	}

	public void registerScrollButtons(List list1, int i2, int i3) {
		this.scrollUpButtonID = i2;
		this.scrollDownButtonID = i3;
	}

	private void bindAmountScrolled() {
		int i1 = this.getContentHeight() - (this.bottom - this.top - 4);
		if(i1 < 0) {
			i1 /= 2;
		}

		if(this.amountScrolled < 0.0F) {
			this.amountScrolled = 0.0F;
		}

		if(this.amountScrolled > (float)i1) {
			this.amountScrolled = (float)i1;
		}

	}

	public void actionPerformed(ButtonWidget guiButton1) {
		if(guiButton1.active) {
			if(guiButton1.id == this.scrollUpButtonID) {
				this.amountScrolled -= (float)(this.posZ * 2 / 3);
				this.initialClickY = -2.0F;
				this.bindAmountScrolled();
			} else if(guiButton1.id == this.scrollDownButtonID) {
				this.amountScrolled += (float)(this.posZ * 2 / 3);
				this.initialClickY = -2.0F;
				this.bindAmountScrolled();
			}
		}

	}

	public void drawScreen(int i1, int i2, float f3) {
		mouseX = i1;
		mouseY = i2;
		this.drawBackground();
		int i4 = this.getSize();
		int i5 = this.width / 2 + 124;
		int i6 = i5 + 6;
		int i7;
		int i8;
		int i9;
		int i10;
		int i11;
		if(Mouse.isButtonDown(0)) {
			if(this.initialClickY == -1.0F) {
				boolean z12 = true;
				if(i2 >= this.top && i2 <= this.bottom) {
					int i13 = this.width / 2 - 110;
					i7 = this.width / 2 + 110;
					i8 = i2 - this.top - this.posX + (int)this.amountScrolled - 4;
					i9 = i8 / this.posZ;
					if(i1 >= i13 && i1 <= i7 && i9 >= 0 && i8 >= 0 && i9 < i4) {
						boolean z14 = i9 == this.selectedElement && System.currentTimeMillis() - this.lastClicked < 250L;
						this.elementClicked(i9, z14);
						this.selectedElement = i9;
						this.lastClicked = System.currentTimeMillis();
					} else if(i1 >= i13 && i1 <= i7 && i8 < 0) {
						this.func_27255_a(i1 - i13, i2 - this.top + (int)this.amountScrolled - 4);
						z12 = false;
					}

					if(i1 >= i5 && i1 <= i6) {
						this.scrollMultiplier = -1.0F;
						i11 = this.getContentHeight() - (this.bottom - this.top - 4);
						if(i11 < 1) {
							i11 = 1;
						}

						i10 = (int)((float)((this.bottom - this.top) * (this.bottom - this.top)) / (float)this.getContentHeight());
						if(i10 < 32) {
							i10 = 32;
						}

						if(i10 > this.bottom - this.top - 8) {
							i10 = this.bottom - this.top - 8;
						}

						this.scrollMultiplier /= (float)(this.bottom - this.top - i10) / (float)i11;
					} else {
						this.scrollMultiplier = 1.0F;
					}

					if(z12) {
						this.initialClickY = (float)i2;
					} else {
						this.initialClickY = -2.0F;
					}
				} else {
					this.initialClickY = -2.0F;
				}
			} else if(this.initialClickY >= 0.0F) {
				this.amountScrolled -= ((float)i2 - this.initialClickY) * this.scrollMultiplier;
				this.initialClickY = (float)i2;
			}
		} else {
			this.initialClickY = -1.0F;
		}

		this.bindAmountScrolled();
		GL11.glDisable(GL11.GL_LIGHTING);
		GL11.glDisable(GL11.GL_FOG);
		BufferBuilder tessellator16 = BufferBuilder.INSTANCE;
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.mc.textureManager.load("/dirt.png"));
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		float f17 = 32.0F;
		tessellator16.start();
		tessellator16.color(2105376);
		tessellator16.vertex((double)this.left, (double)this.bottom, 0.0D, (double)((float)this.left / f17), (double)((float)(this.bottom + (int)this.amountScrolled) / f17));
		tessellator16.vertex((double)this.right, (double)this.bottom, 0.0D, (double)((float)this.right / f17), (double)((float)(this.bottom + (int)this.amountScrolled) / f17));
		tessellator16.vertex((double)this.right, (double)this.top, 0.0D, (double)((float)this.right / f17), (double)((float)(this.top + (int)this.amountScrolled) / f17));
		tessellator16.vertex((double)this.left, (double)this.top, 0.0D, (double)((float)this.left / f17), (double)((float)(this.top + (int)this.amountScrolled) / f17));
		tessellator16.end();
		i7 = this.width / 2 - 92 - 16;
		i8 = this.top + 4 - (int)this.amountScrolled;

		int i18;
		for(i9 = 0; i9 < i4; ++i9) {
			i11 = i8 + i9 * this.posZ + this.posX;
			i10 = this.posZ - 4;
			if(i11 <= this.bottom && i11 + i10 >= this.top) {
				this.drawSlot(i9, i7, i11, i10, tessellator16);
			}
		}

		GL11.glDisable(GL11.GL_DEPTH_TEST);
		byte b19 = 4;
		this.overlayBackground(0, this.top, 255, 255);
		this.overlayBackground(this.bottom, this.height, 255, 255);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glDisable(GL11.GL_ALPHA_TEST);
		GL11.glShadeModel(GL11.GL_SMOOTH);
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		tessellator16.start();
		this.setColorRGBA_I(0, 0);
		tessellator16.vertex((double)this.left, (double)(this.top + b19), 0.0D, 0.0D, 1.0D);
		tessellator16.vertex((double)this.right, (double)(this.top + b19), 0.0D, 1.0D, 1.0D);
		this.setColorRGBA_I(0, 255);
		tessellator16.vertex((double)this.right, (double)this.top, 0.0D, 1.0D, 0.0D);
		tessellator16.vertex((double)this.left, (double)this.top, 0.0D, 0.0D, 0.0D);
		tessellator16.end();
		tessellator16.start();
		this.setColorRGBA_I(0, 255);
		tessellator16.vertex((double)this.left, (double)this.bottom, 0.0D, 0.0D, 1.0D);
		tessellator16.vertex((double)this.right, (double)this.bottom, 0.0D, 1.0D, 1.0D);
		this.setColorRGBA_I(0, 0);
		tessellator16.vertex((double)this.right, (double)(this.bottom - b19), 0.0D, 1.0D, 0.0D);
		tessellator16.vertex((double)this.left, (double)(this.bottom - b19), 0.0D, 0.0D, 0.0D);
		tessellator16.end();
		i11 = this.getContentHeight() - (this.bottom - this.top - 4);
		if(i11 > 0) {
			int xOffset = 20;
			int barLeft  = i5 + xOffset;
			int barRight = i6 + xOffset;
			i10 = (this.bottom - this.top) * (this.bottom - this.top) / this.getContentHeight();
			if(i10 < 32) {
				i10 = 32;
			}

			if(i10 > this.bottom - this.top - 8) {
				i10 = this.bottom - this.top - 8;
			}

			i18 = (int)this.amountScrolled * (this.bottom - this.top - i10) / i11 + this.top;
			if(i18 < this.top) {
				i18 = this.top;
			}

			tessellator16.start();
			this.setColorRGBA_I(0, 255);
			tessellator16.vertex((double)barLeft, (double)this.bottom, 0.0D, 0.0D, 1.0D);
			tessellator16.vertex((double)barRight, (double)this.bottom, 0.0D, 1.0D, 1.0D);
			tessellator16.vertex((double)barRight, (double)this.top, 0.0D, 1.0D, 0.0D);
			tessellator16.vertex((double)barLeft, (double)this.top, 0.0D, 0.0D, 0.0D);
			tessellator16.end();
			tessellator16.start();
			this.setColorRGBA_I(8421504, 255);
			tessellator16.vertex((double)barLeft, (double)(i18 + i10), 0.0D, 0.0D, 1.0D);
			tessellator16.vertex((double)barRight, (double)(i18 + i10), 0.0D, 1.0D, 1.0D);
			tessellator16.vertex((double)barRight, (double)i18, 0.0D, 1.0D, 0.0D);
			tessellator16.vertex((double)barLeft, (double)i18, 0.0D, 0.0D, 0.0D);
			tessellator16.end();
			tessellator16.start();
			this.setColorRGBA_I(12632256, 255);
			tessellator16.vertex((double)barLeft, (double)(i18 + i10 - 1), 0.0D, 0.0D, 1.0D);
			tessellator16.vertex((double)(barRight - 1), (double)(i18 + i10 - 1), 0.0D, 1.0D, 1.0D);
			tessellator16.vertex((double)(barRight - 1), (double)i18, 0.0D, 1.0D, 0.0D);
			tessellator16.vertex((double)barLeft, (double)i18, 0.0D, 0.0D, 0.0D);
			tessellator16.end();
		}

		this.func_27257_b(i1, i2);
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glShadeModel(GL11.GL_FLAT);
		GL11.glEnable(GL11.GL_ALPHA_TEST);
		GL11.glDisable(GL11.GL_BLEND);
	}

	private void overlayBackground(int i1, int i2, int i3, int i4) {
		BufferBuilder tessellator5 = BufferBuilder.INSTANCE;
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.mc.textureManager.load("/dirt.png"));
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		float f6 = 32.0F;
		tessellator5.start();
		this.setColorRGBA_I(4210752, i4);
		tessellator5.vertex(0.0D, (double)i2, 0.0D, 0.0D, (double)((float)i2 / f6));
		tessellator5.vertex((double)this.width, (double)i2, 0.0D, (double)((float)this.width / f6), (double)((float)i2 / f6));
		this.setColorRGBA_I(4210752, i3);
		tessellator5.vertex((double)this.width, (double)i1, 0.0D, (double)((float)this.width / f6), (double)((float)i1 / f6));
		tessellator5.vertex(0.0D, (double)i1, 0.0D, 0.0D, (double)((float)i1 / f6));
		tessellator5.end();
	}

	private void setColorRGBA_I(int i1, int i2) {
		int i3 = i1 >> 16 & 255;
		int i4 = i1 >> 8 & 255;
		int i5 = i1 & 255;
		BufferBuilder.INSTANCE.color(i3, i4, i5, i2);
	}

	protected int getSize() {
		return EnumOptions.values().length;
	}

	protected void elementClicked(int i1, boolean z2) {
		if(this.theOptionButtons[i1] != null) {
			optionScreen.getMinecraft().soundSystem.play("random.click", 1.0F, 1.0F);
			this.theOptionButtons[i1].onClicked();
			selectedSlot = i1;
		}
	}

	protected boolean isSelected(int i1) {
		return selectedSlot == i1;
	}

	protected int getContentHeight() {
		return this.getSize() * 36;
	}

	protected void drawBackground() {
		this.optionScreen.renderBackground();
	}

	protected void drawSlot(int i1, int i2, int i3, int i4, BufferBuilder tessellator5) {
		EnumOptions theEnum = EnumOptions.values()[i1];
		if(this.theOptionButtons[i1] != null) {
			int xPos = (this.width / 2) - (this.theOptionButtons[i1].getWidth() / 2);
			this.optionScreen.drawString(this.optionScreen.getMinecraft().textRenderer, theEnum.optionsName, xPos, i3 + 1, 0xFFFFFF);
			this.theOptionButtons[i1].setFocused(isSelected(i1));
			this.theOptionButtons[i1].renderWithCustomPos(xPos, i3 + 12, mouseX, mouseY, AnotherAlphaAgain.mc);
		} else {
			this.optionScreen.drawString(this.optionScreen.getMinecraft().textRenderer, theEnum.optionsName, i2+ 2, i3 + 1, 0xFFFFFF);
		}
	}
}
