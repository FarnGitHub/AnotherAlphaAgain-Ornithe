package farn.another_alpha_again.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import farn.another_alpha_again.Main;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.HitResult;
import net.minecraft.world.World;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GameRenderer.class)
public class GameRendererMixin {

	@Inject(method="render", at = @At("TAIL"))
	public void frameUpdate(float tickDelta, CallbackInfo ci) {
		Main.tick();
	}

	@ModifyVariable(method = "transformCamera", at = @At("STORE"), ordinal = 1)
	private float rayCastValueFix(float value)
	{
		return value + (Main.front ? 180.F : 0.F);
	}

	@Unique
	public Vec3d flipYAroundArbitraryY(Vec3d vec, float arbitraryY)
	{
		double reflectedY = arbitraryY + (arbitraryY - vec.y);
		return Vec3d.of(vec.x, reflectedY, vec.z);
	}

	@Inject(method = "transformCamera", at = @At(value = "INVOKE", target = "Lorg/lwjgl/opengl/GL11;glRotatef(FFFF)V", ordinal = 0))
	private void glRotateCameraFix(float par1, CallbackInfo ci)
	{
		if (Main.front)
			GL11.glRotatef(180.0f, 0.0f, 1.0f, 0.0f);
	}

	@WrapOperation(method = "transformCamera", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;rayTrace(Lnet/minecraft/util/math/Vec3d;Lnet/minecraft/util/math/Vec3d;)Lnet/minecraft/world/HitResult;"))
	private HitResult weirdYUpsideDownRayCastFix(World instance, Vec3d arg2, Vec3d vec3f, Operation<HitResult> original)
	{
		return instance.rayTrace(arg2, Main.front ? flipYAroundArbitraryY(vec3f, (float) Main.mc.player.y) : vec3f);
	}
}
