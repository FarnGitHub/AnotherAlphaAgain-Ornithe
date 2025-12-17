package farn.another_alpha_again.sub;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import net.minecraft.client.Minecraft;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;
import java.nio.ByteBuffer;
import java.util.Date;

public class ScreenShot extends Thread {
	private static final DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd_HH.mm.ss");
	public static final File screenShotDirectory = new File(Minecraft.getRunDirectory(), "screenshots");
	public int width, height;
	public byte[] pixelData;

	private ScreenShot() {
	}

	static {
		screenShotDirectory.mkdir();
	}

	@Override
	public void run() {
		try {
			int[] imageData = new int[width * height];

			for (int x = 0; x < width; x++) {
				for (int y = 0; y < height; y++) {
					int src = x + (height - y - 1) * width;
					int r = pixelData[src * 3] & 255;
					int g = pixelData[src * 3 + 1] & 255;
					int b = pixelData[src * 3 + 2] & 255;
					imageData[x + y * width] = 0xFF000000 | r << 16 | g << 8 | b;
				}
			}

			BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
			img.setRGB(0, 0, width, height, imageData, 0, width);
			File file;
			for(int i6 = 1; (file = new File(screenShotDirectory, dateFormat.format(new Date()) + (i6 == 1 ? "" : "_" + i6) + ".png")).exists(); ++i6) {
			}
			ImageIO.write(img, "png", file);
			queueChatMessage("Saved screenshot as " + file.getName());
		} catch (Exception e) {
			queueChatMessage("Failed to save: " + e);
		}
	}

	public static void take(int widthR, int heightR) {
		ScreenShot thread = new ScreenShot();
		thread.width = widthR;
		thread.height = heightR;
		ByteBuffer buffer = BufferUtils.createByteBuffer(thread.width * thread.height * 3);
		GL11.glPixelStorei(GL11.GL_PACK_ALIGNMENT, 1);
		GL11.glPixelStorei(GL11.GL_UNPACK_ALIGNMENT, 1);
		GL11.glReadPixels(0, 0, thread.width, thread.height, GL11.GL_RGB, GL11.GL_UNSIGNED_BYTE, buffer);
		thread.pixelData = new byte[thread.width * thread.height * 3];
		buffer.get(thread.pixelData);
		thread.start();
	}

	private static void queueChatMessage(String msg) {
		QueueDispatcher.queueChatMessage(msg);
	}

}
