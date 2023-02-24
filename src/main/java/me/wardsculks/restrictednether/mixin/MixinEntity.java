package me.wardsculks.restrictednether.mixin;

import java.lang.Math;

import net.minecraft.entity.Entity;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


@Mixin(Entity.class)
public abstract class MixinEntity{

    public Vec3d netherSpawnPos;
    @Shadow public World world;
    @Shadow private BlockPos blockPos;
    @Shadow protected abstract void tickPortalCooldown();
    @Shadow public abstract void resetPortalCooldown();
    @Shadow public abstract boolean isPlayer();

    protected double distanceFromLastPortal() {
        // Calculates horizontal distance between player and point of the portal, which was used to enter the Nether
        if (netherSpawnPos == null) return 0.0;

        int px = this.blockPos.getX();
        int pz = this.blockPos.getZ();
        double npx = this.netherSpawnPos.getX();
        double npz = this.netherSpawnPos.getZ();

        return Math.abs(Math.sqrt(Math.pow((npx-px), 2) + Math.pow((npz-pz), 2)));
    }

    @Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;hasVehicle()Z"),
            method = "tickPortal()V", cancellable = true)
    // hasVehicle() is executed during final condition check for teleporting to other dimension
    protected void wsRN$distanceRestriction(CallbackInfo ci) {
        if (!this.isPlayer()) return;

        RegistryKey<World> world = this.world.getRegistryKey();
        if (world == World.NETHER && this.distanceFromLastPortal() > 100) {
            this.toastMessage(Text.literal("Ви не можете скористатися настільки віддаленими порталами!"));
            this.resetPortalCooldown();
            ci.cancel();
        }
        this.tickPortalCooldown();
    }

    protected abstract void toastMessage(Text message);

}
