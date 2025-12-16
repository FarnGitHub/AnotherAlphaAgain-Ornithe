package farn.another_alpha_again.sub;

import java.util.ArrayList;
import java.util.List;

import com.mojang.blaze3d.platform.Lighting;
import farn.another_alpha_again.mixin.AEI.GuiAccessor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiElement;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.inventory.menu.InventoryMenuScreen;
import net.minecraft.client.render.Window;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

public class AlwaysEnoughItems {
	private static List<ItemStack> itemList = new ArrayList<>();
	private static ItemRenderer itemRenderer = new ItemRenderer();
	private static int page;
	private static int mouseX;
	private static int mouseY;
	private static boolean clicked;
	private static boolean mouseLeft;
	private static boolean mouseRight;
	public static boolean enabled;
	private static boolean holdDeleteClick = false;
	public static boolean delHover;
	private static final GuiAccessor drawer = (GuiAccessor)(new GuiElement());

	static {
		for(int var2 = 0; var2 < Item.BY_ID.length; ++var2) {
			if(Item.BY_ID[var2] != null) {
				itemList.add(new ItemStack(Item.BY_ID[var2]));
			}
		}
	}

	public static void tick(Minecraft mc, Screen screen) {
		if (enabled && mc != null && mc.world != null) {
			if (screen instanceof InventoryMenuScreen) {
				mouseLeft = false;
				mouseRight = false;

				if (!Mouse.isButtonDown(0) && !Mouse.isButtonDown(1)) {
					clicked = false;
				} else if (!clicked) {
					mouseLeft = Mouse.isButtonDown(0);
					mouseRight = Mouse.isButtonDown(1);
					clicked = true;
				}

				Window window = new Window(mc.width, mc.height);
				int width = window.getWidth();
				int height = window.getHeight();

				mouseX = Mouse.getX() * width / mc.width;
				mouseY = height - Mouse.getY() * height / mc.height - 1;

				int selectorWidth = 20 + screen.width / 7;
				if (selectorWidth < 100)
					selectorWidth = 100;

				int panelLeftX = screen.width / 2 - selectorWidth;

				Lighting.turnOff();
				drawer.fillRect(
					screen.width,
					screen.height,
					screen.width - panelLeftX,
					0,
					0x68000000   // 1744830464
				);

				final int slotSize = 20;

				int leftoverSpace = panelLeftX % slotSize;
				int columns = panelLeftX / slotSize;
				int rows = screen.height / slotSize - 1;

				int itemsPerPage = columns * rows;
				if (itemsPerPage == 0) itemsPerPage = 1;

				int maxPage = (int) Math.ceil(itemList.size() / (double) itemsPerPage) - 1;

				int buttonY = screen.height - 22;

				int leftButtonX = screen.width - panelLeftX + 4;
				int leftButtonMaxX = leftButtonX + slotSize;
				int leftButtonMaxY = buttonY + slotSize;

				boolean mouseOverLeft = mouseX > leftButtonX && mouseX < leftButtonMaxX &&
					mouseY > buttonY && mouseY < leftButtonMaxY;

				if (mouseOverLeft) {
					drawer.fillRect(leftButtonX, buttonY, leftButtonMaxX, leftButtonMaxY, 0x51000000);

					if (mouseLeft) {
						page--;
						if (page < 0) page = maxPage;
					}
				}


				int rightButtonX = screen.width - 25;
				int rightButtonMaxX = rightButtonX + slotSize;

				boolean mouseOverRight = mouseX > rightButtonX && mouseX < rightButtonMaxX &&
					mouseY > buttonY && mouseY < leftButtonMaxY;

				if (mouseOverRight) {
					drawer.fillRect(rightButtonX, buttonY, rightButtonMaxX, leftButtonMaxY, 0x51000000);

					if (mouseLeft) {
						page++;
						if (page > maxPage) page = 0;
					}
				}

				if (columns * rows * page > itemList.size())
					page = maxPage;

				int startIndex = page * itemsPerPage;



				OUTER:
				for (int row = 0; row < rows; row++) {
					for (int col = 0; col < columns; col++) {

						int itemIndex = startIndex + (row * columns + col);
						if (itemIndex >= itemList.size())
							break OUTER;

						ItemStack item = itemList.get(itemIndex);

						int x = screen.width - panelLeftX + leftoverSpace / 2 + col * slotSize;
						int y = row * slotSize;

						int maxX = x + slotSize;
						int maxY = y + slotSize;

						boolean hovered = mouseX > x && mouseX < maxX && mouseY > y && mouseY < maxY;

						if (hovered) {
							drawer.fillRect(x, y, maxX, maxY, 0x51000000);

							if (mouseRight) {
								mc.player.inventory.insertStack(item.copy());
							}

							if (mouseLeft) {
								ItemStack fullStack = item.copy();
								fullStack.size = item.getMaxSize();
								mc.player.inventory.insertStack(fullStack);
							}
						}

						drawItem(screen, mc, item, x + 2, y + 2);
					}
				}

				renderDeleteButton(mc, screen);

				String pageText = (page + 1) + "/" + (maxPage + 1);

				screen.drawString(
					mc.textRenderer,
					pageText,
					screen.width - panelLeftX / 2 - mc.textRenderer.getWidth(pageText) / 2,
					screen.height - 15,
					0xFFFFFF
				);

				screen.drawString(mc.textRenderer, "<",
					screen.width - panelLeftX + 12,
					screen.height - 15,
					0xFFFFFF);

				screen.drawString(mc.textRenderer, ">",
					screen.width - 12 - mc.textRenderer.getWidth(">"),
					screen.height - 15,
					0xFFFFFF);
			}
		}
	}

	private static void drawItem(Screen screen, Minecraft var1, ItemStack stack, int x, int y) {
		if(stack != null && stack.getItem() != null) {
			GL11.glDisable(GL11.GL_BLEND);
			GL11.glEnable(GL12.GL_RESCALE_NORMAL);
			GL11.glPushMatrix();
			GL11.glRotatef(120.0F, 1.0F, 0.0F, 0.0F);
			Lighting.turnOn();
			GL11.glPopMatrix();
			((GuiAccessor) screen).fillRect(0, 0, 0, 0, -1);
			itemRenderer.renderGuiItem(var1.textRenderer, var1.textureManager, stack, x, y);
			itemRenderer.renderItemInfo(var1.textRenderer, var1.textureManager, new ItemStack(stack.itemId, 0, stack.metadata), x, y);
			Lighting.turnOff();
			GL11.glDisable(GL12.GL_RESCALE_NORMAL);
		}
	}

	public static void renderDeleteButton(Minecraft mc, Screen screen) {
		int delBtnWidth = 100;
		int delBtnHeight = 25;
		int delBtnX = 0;
		int delBtnY = screen.height - delBtnHeight;                                  // top-right corner

		delHover = mouseX >= delBtnX && mouseX <= delBtnX + delBtnWidth &&
			mouseY >= delBtnY && mouseY <= delBtnY + delBtnHeight;

		drawer.fillRect(
			delBtnX,
			delBtnY,
			delBtnX + delBtnWidth,
			delBtnY + delBtnHeight,
			delHover ? 0x51000000 : 0x68000000
		);

		String delText = "DELETE";
		screen.drawString(
			mc.textRenderer,
			delText,
			delBtnX + (delBtnWidth - mc.textRenderer.getWidth(delText)) / 2,
			delBtnY + (delBtnHeight / 3),
			0xFFFFFF
		);

		if (delHover && clicked && mc.player.inventory.cursorStack != null) {
			if(!holdDeleteClick) {
				holdDeleteClick = true;
				mc.player.inventory.cursorStack = null;
			}
		} else {
			holdDeleteClick = false;
		}
	}
}
