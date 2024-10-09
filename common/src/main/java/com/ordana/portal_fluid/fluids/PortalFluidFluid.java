package com.ordana.portal_fluid.fluids;

import com.ordana.portal_fluid.PortalFluidRoot;
import com.ordana.portal_fluid.reg.ModFluids;
import com.ordana.portal_fluid.reg.ModItems;
import com.ordana.portal_fluid.reg.ModParticles;
import com.ordana.portal_fluid.reg.ModSoundEvents;
import net.mehvahdjukaar.moonlight.api.client.ModFluidRenderProperties;
import net.mehvahdjukaar.moonlight.api.fluids.ModFlowingFluid;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;
import java.util.function.Supplier;

public class PortalFluidFluid extends ModFlowingFluid {

    public PortalFluidFluid(Properties properties, Supplier<? extends LiquidBlock> block) {
        super(properties, block);
    }

    @Override
    public ModFluidRenderProperties createRenderProperties() {
        return new PortalFluidFluidRenderer(
                PortalFluidRoot.res("block/portal_fluid"),
                PortalFluidRoot.res("block/portal_fluid_flowing"),
                -1,
                PortalFluidRoot.res("block/portal_fluid_overlay"),
                PortalFluidRoot.res("block/portal_fluid_overlay"),
                new Vec3(133, 0, 0));

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
        return Optional.of(ModSoundEvents.PORTAL_FLUID_BUCKET_FILL.get());
    }

    @Override
    protected ParticleOptions getDripParticle() {
        return ParticleTypes.DRIPPING_OBSIDIAN_TEAR;
    }


    public void animateTick(Level level, BlockPos pos, FluidState state, RandomSource random) {
        BlockPos blockPos = pos.above();
        if (level.getBlockState(blockPos).isAir() && !level.getBlockState(blockPos).isSolidRender(level, blockPos)) {
            if (random.nextInt(20) == 0) {
                double d = (double)pos.getX() + random.nextDouble();
                double e = (double)pos.getY() + 1.0D;
                double f = (double)pos.getZ() + random.nextDouble();
                level.addParticle(ModParticles.PORTAL_FLAME.get(), d, e + 0.2, f, 0.0D, 0.0D, 0.0D);
                //level.playLocalSound(d, e, f, SoundEvents.LAVA_POP, SoundSource.BLOCKS, 0.2F + random.nextFloat() * 0.2F, 0.9F + random.nextFloat() * 0.15F, false);
            }

            if (random.nextInt(200) == 0) {
                level.playLocalSound(pos.getX(), pos.getY(), pos.getZ(), ModSoundEvents.PORTAL_FLUID_AMBIENT.get(), SoundSource.BLOCKS, 0.2F + random.nextFloat() * 0.2F, 0.9F + random.nextFloat() * 0.15F, false);
            }
        }

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
        return 5;
    }

    @Override
    protected float getExplosionResistance() {
        return 100f;
    }

    @Override
    public boolean isSource(@NotNull FluidState state) {
        return false;
    }

    @Override
    public int getAmount(@NotNull FluidState state) {
        return 0;
    }

    public static class Flowing extends PortalFluidFluid {
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
        public boolean isSource(@NotNull FluidState state) {
            return false;
        }
    }

    public static class Source extends PortalFluidFluid {
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