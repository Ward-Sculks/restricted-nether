package me.wardsculks.restrictednether.mixin;

import me.wardsculks.restrictednether.RestrictedNether;
import net.minecraft.entity.Entity;
import net.minecraft.server.PlayerManager;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.TeleportTarget;
import net.minecraft.world.WorldProperties;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(ServerPlayerEntity.class)
public abstract class MixinServerPlayerEntity extends MixinEntity {

    @Inject(at = @At(value = "FIELD", opcode = Opcodes.PUTFIELD,
                     target = "Lnet/minecraft/server/network/ServerPlayerEntity;enteredNetherPos:Lnet/minecraft/util/math/Vec3d;"),
            method = "moveToWorld(Lnet/minecraft/server/world/ServerWorld;)Lnet/minecraft/entity/Entity;",
            locals = LocalCapture.CAPTURE_FAILSOFT)
    public void moveToWorld(ServerWorld destination, CallbackInfoReturnable<Entity> cir, ServerWorld serverWorld, RegistryKey registryKey, WorldProperties worldProperties, PlayerManager playerManager, TeleportTarget teleportTarget) {
        RestrictedNether.LOGGER.info("movetoworld mixin executes");
        // Sets this.netherSpawnPos during teleportation to Nether
        this.netherSpawnPos = teleportTarget.position;
        RestrictedNether.LOGGER.info(this.netherSpawnPos.toString());
    }

    // WS-81 TODO: mixin readNbt, writeNbt to include this.netherSpawnPos from EntityMixin

}
