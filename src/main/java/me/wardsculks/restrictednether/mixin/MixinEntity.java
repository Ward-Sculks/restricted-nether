package me.wardsculks.restrictednether.mixin;

import java.lang.Math;

import me.wardsculks.restrictednether.RestrictedNether;

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
    private boolean msgReceived;
    @Shadow public World world;
    @Shadow private BlockPos blockPos;
    @Shadow protected abstract void tickPortalCooldown();
    @Shadow public abstract void resetPortalCooldown();
    @Shadow public abstract boolean isPlayer();
    @Shadow public abstract void sendMessage(Text message);

    public double distanceFromLastPortal() {
        // Calculates horizontal distance between player and point of the portal, which was used to enter the Nether
        if (netherSpawnPos == null) return 0.0;

        int px = this.blockPos.getX();
        int pz = this.blockPos.getZ();
        double npx = this.netherSpawnPos.getX();
        double npz = this.netherSpawnPos.getZ();

        double result = Math.abs(Math.sqrt(Math.pow((npx-px), 2) + Math.pow((npz-pz), 2)));
        RestrictedNether.LOGGER.info("Distance between portals: {}", result);
        return result;
    }

    @Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;hasVehicle()Z"),
            method = "tickPortal()V", cancellable = true)
    // hasVehicle() is executed during final condition check for teleporting to other dimension
    protected void wsRestrictedNether$distanceRestriction(CallbackInfo ci) {
        if (!this.isPlayer()) return;

        RegistryKey<World> world = this.world.getRegistryKey();
        if (world == World.NETHER && this.distanceFromLastPortal() > 100) {
            if (!this.msgReceived) {
                this.sendMessage(Text.literal("Ви не можете скористатися настільки віддаленими порталами"));
                this.msgReceived = true;
            }
            this.resetPortalCooldown();
            ci.cancel();
        }
        this.tickPortalCooldown();
    }

}

