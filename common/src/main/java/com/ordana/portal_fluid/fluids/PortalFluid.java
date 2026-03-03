package com.ordana.portal_fluid.fluids;

import com.ordana.portal_fluid.reg.ModFluids;
import com.ordana.portal_fluid.reg.ModItems;
import com.ordana.portal_fluid.reg.ModSoundEvents;
import com.ordana.portal_fluid.reg.ModTags;
import com.ordana.portal_fluid.util.PortalFluidVisuals;
import net.mehvahdjukaar.moonlight.api.client.ModFluidRenderProperties;
import net.mehvahdjukaar.moonlight.api.fluids.ModFlowingFluid;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.AABB;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;
import java.util.function.Supplier;

public abstract class PortalFluid extends ModFlowingFluid {

    public PortalFluid(Properties properties, Supplier<? extends LiquidBlock> block) {
        super(properties, block);
    }

    @Override
    public ModFluidRenderProperties createRenderProperties() {
        return new PortalFluidRenderer();
    }

    @NotNull
    public Fluid getFlowing() {
        return ModFluids.FLOWING_PORTAL_FLUID.get();
    }

    @NotNull
    public Fluid getSource() {
        return ModFluids.PORTAL_FLUID.get();
    }

    @NotNull
    public Item getBucket() {
        return ModItems.PORTAL_FLUID_BUCKET.get();
    }

    @NotNull
    public Optional<SoundEvent> getPickupSound() {
        return Optional.of(ModSoundEvents.BUCKET_FILL_PORTAL_FLUID.get());
    }

    @Override
    protected ParticleOptions getDripParticle() {
        return ParticleTypes.DRIPPING_OBSIDIAN_TEAR;
    }

    @Override
    public void animateTick(Level level, BlockPos blockPos, FluidState fluidState, RandomSource randomSource) {
        PortalFluidVisuals.onAnimateTick(level, blockPos.above(), randomSource);
    }

    @Override
    protected int getSlopeFindDistance(@NotNull LevelReader level) {
        return 4;
    }

    @Override
    protected int getDropOff(@NotNull LevelReader level) {
        return 1;
    }

    @Override
    public int getTickDelay(@NotNull LevelReader level) {
        return 10;
    }

    @Override
    protected float getExplosionResistance() {
        return 100.0F;
    }

    public static boolean isTouching(Level level, AABB boundingBox) {
        int minX = Mth.floor(boundingBox.minX);
        int maxX = Mth.ceil(boundingBox.maxX);
        int minY = Mth.floor(boundingBox.minY);
        int maxY = Mth.ceil(boundingBox.maxY);
        int minZ = Mth.floor(boundingBox.minZ);
        int maxZ = Mth.ceil(boundingBox.maxZ);

        boolean bl = false;
        BlockPos.MutableBlockPos mutableBlockPos = new BlockPos.MutableBlockPos();

        for (int x = minX; x < maxX; x++) {
            for (int y = minY; y < maxY; y++) {
                for (int z = minZ; z < maxZ; z++) {
                    mutableBlockPos.set(x, y, z);
                    FluidState fluidState = level.getFluidState(mutableBlockPos);

                    if (fluidState.is(ModTags.PORTAL_FLUID)) {
                        float height = y + fluidState.getHeight(level, mutableBlockPos);
                        bl |= boundingBox.minY < height;
                    }
                }
            }
        }

        return bl;
    }

    public static class Flowing extends PortalFluid {

        public Flowing(Properties properties, Supplier<? extends LiquidBlock> block) {
            super(properties, block);
        }

        @Override
        protected void createFluidStateDefinition(StateDefinition.Builder<Fluid, FluidState> builder) {
            super.createFluidStateDefinition(builder);
            builder.add(LEVEL);
        }

        @Override
        public int getAmount(@NotNull FluidState state) {
            return state.getValue(LEVEL);
        }

        @Override
        public boolean isSource(FluidState fluidState) {
            return false;
        }

    }

    public static class Source extends PortalFluid {

        public Source(Properties properties, Supplier<? extends LiquidBlock> block) {
            super(properties, block);
        }

        @Override
        public int getAmount(@NotNull FluidState state) {
            return 8;
        }

        @Override
        public boolean isSource(@NotNull FluidState state) {
            return true;
        }

    }

}