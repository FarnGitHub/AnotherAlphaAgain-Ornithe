package farn.another_alpha_again.mixin.skin;

import farn.another_alpha_again.Main;
import net.minecraft.client.resource.ResourceDownloadThread;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(ResourceDownloadThread.class)
public class ResourceThreadMixin {
	@ModifyConstant(method = "run", constant = @Constant(stringValue = "http://s3.amazonaws.com/MinecraftResources/"), remap = false)
	private String getResourcesUrl(String def) {
		return Main.getResourceUrl();
	}
}
