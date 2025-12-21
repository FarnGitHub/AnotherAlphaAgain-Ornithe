package farn.another_alpha_again.option;

import org.lwjgl.input.Keyboard;

public enum EnumOptions {
	LEAF_DECAY_FIX("Leaf Decay Fix", true),
	AEI_TOGGLE("AEI Toggle Key", Keyboard.KEY_O),
	SKIN_FIX_URL("Skin Url", "https://betacraft.uk/MinecraftSkins/%s.png"),
	SOUND_FIX_URL("Resource Url", "http://s3.betacraft.uk:11702/MinecraftResources/"),
	DOUBLE_SLAB_RECIPE("Double Slab Recipe (Require Restart)", true),
	BETTER_BOAT("Breakless Boat", true),
	STAIRS_DROP("Stair drop stair", true),
	SLAB_DROP_2("Double Slab Drop", true),
	LOG_TO_COAL("Wooden Log To Coal Smelting Recipe)", true),
	WOOL_RECIPE("4x4 String Recipe For Wool (Require Restart)", true);

	public final EnumOptionType type;

	public final OptionValue optionValue;

	public final Object finalValue;

	public String optionsName;

	EnumOptions(String optionsName, String text) {
		finalValue = text;
		this.type = EnumOptionType.STRING;
		this.optionsName = optionsName;
		this.optionValue = new OptionValue<>(text, String.class);
	}

	EnumOptions(String optionsName, boolean bool) {
		finalValue = bool;
		this.type = EnumOptionType.TOGGLE;
		this.optionsName = optionsName;
		this.optionValue = new OptionValue<>(bool, Boolean.class);
	}

	EnumOptions(String optionsName, int keybind) {
		finalValue = keybind;
		this.type = EnumOptionType.KEYBIND;
		this.optionsName = optionsName;
		this.optionValue = new OptionValue<>(keybind, Integer.class);
	}
}
