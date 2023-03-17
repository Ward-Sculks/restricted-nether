package me.wardsculks.restrictednether.mixin;

import net.minecraft.block.BlockState;
import net.minecraft.block.EndPortalBlock;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(EndPortalBlock.class)
public class EndPortalBlockMixin {

    /**
     * @author WardSculks
     * @reason Overwriting with empty body to restrict teleportation to End
     */
    @Overwrite
    public void onEntityCollision(BlockState state, World world, BlockPos pos, Entity entity) {}

}
