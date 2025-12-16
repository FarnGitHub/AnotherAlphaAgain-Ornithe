package farn.another_alpha_again;

import farn.another_alpha_again.option.EnumOptions;
import farn.another_alpha_again.sub.AlwaysEnoughItems;
import farn.another_alpha_again.sub.QueueDispatcher;
import farn.another_alpha_again.sub.ScreenShot;
import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.Minecraft;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.input.Keyboard;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.Properties;

public class Main implements ClientModInitializer {

	private static final Properties prop = new Properties();
	public static Minecraft mc;
	public static int cfgVersion = 1;
	public static final File cfgFile = new File(Minecraft.getRunDirectory(), "another_alpha_mod_again.cfg");
	public static final Logger LOGGER = LogManager.getLogger("Another Alpha Mod");

	public static void loadOption(File cfgFile) {
		if (!cfgFile.exists()) {
			saveOptions(cfgFile, false);
			return;
		}

		try (FileInputStream in = new FileInputStream(cfgFile)) {
			prop.load(in);
			for(EnumOptions option: EnumOptions.values()) {
				switch (option.type) {
					case TOGGLE:
						option.optionValue.value = Boolean.parseBoolean(prop.getProperty(option.name(), String.valueOf(option.optionValue.value)));
						break;
					case STRING:
						option.optionValue.value = prop.getProperty(option.name(), (String) option.optionValue.value);
						break;
					case KEYBIND:
						option.optionValue.value = Keyboard.getKeyIndex(prop.getProperty(option.name(), Keyboard.getKeyName((int)option.optionValue.value)));
				}
			}
			try {
				if(Integer.parseInt(prop.getProperty("VERSION", String.valueOf(0))) < cfgVersion) {
					saveOptions(cfgFile, false);
				}
			} catch (Exception e) {
				saveOptions(cfgFile, false);
			}
		} catch (Exception e) {
			LOGGER.error("Failed to load another_alpha_mod_again.cfg");
			LOGGER.error(e.getMessage());
		}
	}

	public static void saveOptions(File cfgFile, boolean forceReload) {
		try (PrintWriter writer = new PrintWriter(new FileWriter(cfgFile))) {
			writer.println("VERSION=" + 1);
			writer.println(" ");
			for(EnumOptions option: EnumOptions.values()) {
				switch(option.type) {
					case TOGGLE:
						writer.println("#" + option.optionsName);
						writer.println(option.name() + "=" + option.optionValue.value);
						writer.println(" ");
						break;
					case KEYBIND:
						writer.println("#" + option.optionsName);
						writer.println(option.name() + "=" + Keyboard.getKeyName((int)option.optionValue.value));
						writer.println(" ");
						break;
					case STRING:
						writer.println("#" + option.optionsName);
						writer.println(option.name() + "=" + option.optionValue.value);
						writer.println(" ");
				}
			}
		} catch (Exception e) {
			LOGGER.error("Failed to save another_alpha_mod_again.cfg");
			LOGGER.error(e.getMessage());
		}
		if(forceReload) {
			loadOption(cfgFile);
		}
	}

	public static String formatSkinFix(String username) {
		return String.format((String) EnumOptions.SKIN_FIX_URL.optionValue.value, username);
	}

	public static String getResourceUrl() {
		return (String)EnumOptions.SOUND_FIX_URL.optionValue.value;
	}

	public static boolean getLeafDecayFix() {
		return (boolean)EnumOptions.LEAF_DECAY_FIX.optionValue.value;
	}

	public static int getAEIToggleButton() {
		return (int)EnumOptions.AEI_TOGGLE.optionValue.value;
	}

	public static boolean toggleDoubleSlabRecipe() {
		return (boolean)EnumOptions.DOUBLE_SLAB_RECIPE.optionValue.value;
	}

	public static boolean boatBreakless() {
		return (boolean)EnumOptions.BETTER_BOAT.optionValue.value;
	}

	public static boolean dropStairs() {
		return (boolean)EnumOptions.STAIRS_DROP.optionValue.value;
	}

	public static boolean dropDoubleSlab() {
		return (boolean)EnumOptions.SLAB_DROP_2.optionValue.value;
	}

	public static boolean logToCoalSmelt() { return (boolean)EnumOptions.LOG_TO_COAL.optionValue.value;}

	public static void tick() {
		if(mc.screen != null) {
			AlwaysEnoughItems.tick(mc, mc.screen);
		}

		QueueDispatcher.runQueuedTasks();
	}

	public static void handleKeyPressed(int keyIndex, boolean downKey) {
		if(downKey) {
			if(mc.screen != null) {
				if(getAEIToggleButton() != 0 && keyIndex == getAEIToggleButton()) {
					AlwaysEnoughItems.enabled = !AlwaysEnoughItems.enabled;
				}
			}

			if(keyIndex == Keyboard.KEY_F2) {
				ScreenShot.take(mc.width, mc.height);
			}
		}
	}

	@Override
	public void onInitializeClient() {
		loadOption(cfgFile);
	}
}
