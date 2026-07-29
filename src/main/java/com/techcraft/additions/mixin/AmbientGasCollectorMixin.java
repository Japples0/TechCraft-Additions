package com.techcraft.additions.mixin;

import com.techcraft.additions.integration.DimensionAtmospheres;
import mekanism.api.chemical.Chemical;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(targets = "com.jerry.mekmm.common.tile.machine.TileEntityAmbientGasCollector")
public abstract class AmbientGasCollectorMixin {
    @Inject(method = "suck", at = @At("HEAD"), cancellable = true, remap = false)
    private void techcraft_additions$limitAtmosphereDimensions(BlockPos pos, CallbackInfoReturnable<Boolean> cir) {
        Level level = ((BlockEntity) (Object) this).getLevel();
        if (level == null || DimensionAtmospheres.getAtmosphere(level).isEmpty()) {
            cir.setReturnValue(false);
        }
    }

    @ModifyArg(
            method = "suck",
            at = @At(
                    value = "INVOKE",
                    target = "Lmekanism/api/chemical/ChemicalStack;<init>(Lnet/minecraft/core/Holder;J)V",
                    remap = false
            ),
            index = 0,
            remap = false
    )
    private Holder<Chemical> techcraft_additions$selectAtmosphereChemical(Holder<Chemical> original) {
        Level level = ((BlockEntity) (Object) this).getLevel();
        if (level != null) {
            return DimensionAtmospheres.getAtmosphere(level).orElse(original);
        }
        return original;
    }
}
