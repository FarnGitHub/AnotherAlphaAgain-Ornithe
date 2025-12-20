package farn.another_alpha_again.sub;

import farn.another_alpha_again.AnotherAlphaAgain;
import farn.another_alpha_again.mixin.gui.GuiAccessor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiElement;
import net.minecraft.client.render.Window;
import net.minecraft.util.math.MathHelper;

import java.awt.*;
import java.text.DecimalFormat;

public class DebugHudExtraDrawer {
	public static GuiElement drawer = new GuiElement();
	public static GuiAccessor extendedDrawer = (GuiAccessor) drawer;
	public static DecimalFormat coordFormat = new DecimalFormat("#.00");
	private static int darkGrey = (new Color(64, 64, 64, 128)).getRGB();
	private static Minecraft mc;

	public static void renderDebugHud(Window scaleRender) {
		if(mc == null) {
			mc = AnotherAlphaAgain.mc;
			return;
		}
		String a112 = "Minecraft Alpha v1.1.2_01";
		extendedDrawer.fillRect(0, 0, mc.textRenderer.getWidth(a112) + 2, 10, darkGrey);
		mc.textRenderer.drawWithShadow(a112, 2, 2, 16777215);
		if(AnotherAlphaAgain.debugScreen) {
			long l15 = Runtime.getRuntime().maxMemory();
			long m26 = Runtime.getRuntime().totalMemory();
			long n34 = Runtime.getRuntime().freeMemory();
			long o36 = m26 - n34;
			float rawHour = (mc.world.getTimeOfDay(1.0F) * 24.0F + 12.0F) % 24.0F;
			int hour = MathHelper.floor(rawHour);
			int min = MathHelper.floor(rawHour * 60F) - hour * 60;
			String[] textToRenderLeft = new String[]{
				mc.fpsDebugString,
				compactCoordDisplay(mc.player.x, mc.player.y, mc.player.z),
				"Facing: " + getFacing(mc.player.yaw),
				"Day: " + mc.world.ticks / 24000L,
				"Time: " + String.format("%02d:%02d", hour, min),
				"Seed: " + mc.world.seed
			};
			String[] textToRenderRight = new String[]{
				"Used memory: " + o36 * 100L / l15 + "% (" + o36 / 1024L / 1024L + "MB) of " + l15 / 1024L / 1024L + "MB",
				"Allocated memory: " + m26 * 100L / l15 + "% (" + m26 / 1024L / 1024L + "MB)"
			};
			int[] leftYPos = new int[]{
				11,
				31,
				40,
				58,
				67,
				76
			};
			int[] rightYpos = new int[]{1, 12};
			for(int index = 0; index < textToRenderLeft.length; ++index) {
				extendedDrawer.fillRect(0, leftYPos[index] - 1, mc.textRenderer.getWidth(textToRenderLeft[index]) + 2, leftYPos[index] + 8, darkGrey);
				mc.textRenderer.drawWithShadow(textToRenderLeft[index], 2, leftYPos[index], 16777215);
			}
			int width = scaleRender.getWidth();
			for(int index = 0; index < textToRenderRight.length; ++ index) {
				extendedDrawer.fillRect(width - mc.textRenderer.getWidth(textToRenderRight[index]) - 2, rightYpos[index] - 1, width, rightYpos[index] + 10, darkGrey);
				mc.textRenderer.drawWithShadow(textToRenderRight[index], width - mc.textRenderer.getWidth(textToRenderRight[index]), rightYpos[index], 16777215);
			}

		}
	}

	private static String formattingDecimal(double db) {
		return coordFormat.format(db);
	}

	private static String compactCoordDisplay(Double s1, Double s2, Double s3) {
		return String.format("XYZ: %s/%s/%s", formattingDecimal(s1), formattingDecimal(s2), formattingDecimal(s3));
	}

	private static String getFacing(float yaw) {
		int dir = MathHelper.floor((double)(yaw * 4.0F / 360.0F) + 0.5D) & 3;
		switch(dir) {
			case 0: return "South";
			case 1: return "West";
			case 2: return "North";
			case 3: return "East";
			default: return "Unknown";
		}
	}
}
