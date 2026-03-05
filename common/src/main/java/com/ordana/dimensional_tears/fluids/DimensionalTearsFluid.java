package com.ordana.dimensional_tears.fluids;

import com.ordana.dimensional_tears.reg.*;
import com.ordana.dimensional_tears.util.DimensionalTearsVisuals;
import net.mehvahdjukaar.moonlight.api.client.ModFluidRenderProperties;
import net.mehvahdjukaar.moonlight.api.fluids.ModFlowingFluid;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.pathfinder.PathType;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;

public abstract class DimensionalTearsFluid extends ModFlowingFluid {

    public static final double MOTION_SCALE = 0.007; // same as nether lava
    private static final Properties PROPERTIES = ModFlowingFluid.properties()
        .supportsBoating(true)
        .motionScale(MOTION_SCALE)
        .canDrown(false)
        .canSwim(false)
        .adjacentPathType(PathType.LAVA) // needs fabric impl
        .fallDistanceModifier(0.0F)
        .lightLevel(5);

    public DimensionalTearsFluid() {
        super(PROPERTIES, ModBlocks.DIMENSIONAL_TEARS);
    }

    @Override
    public ModFluidRenderProperties createRenderProperties() {
        return new DimensionalTearsFluidRenderer();
    }

    @NotNull
    public Fluid getFlowing() {
        return ModFluids.FLOWING_DIMENSIONAL_TEARS.get();
    }

    @NotNull
    public Fluid getSource() {
        return ModFluids.DIMENSIONAL_TEARS.get();
    }

    @NotNull
    public Item getBucket() {
        return ModItems.DIMENSIONAL_TEARS_BUCKET.get();
    }

    @NotNull
    public Optional<SoundEvent> getPickupSound() {
        return Optional.of(ModSoundEvents.BUCKET_FILL_DIMENSIONAL_TEARS.get());
    }

    @Override
    protected ParticleOptions getDripParticle() {
        return ParticleTypes.DRIPPING_OBSIDIAN_TEAR;
    }

    @Override
    public void animateTick(Level level, BlockPos blockPos, FluidState fluidState, RandomSource randomSource) {
        DimensionalTearsVisuals.onAnimateTick(level, blockPos.above(), randomSource);
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

    public static double getHeight(Entity entity) {
        return entity.getFluidHeight(ModTags.DIMENSIONAL_TEARS);
    }

    public static boolean isIn(Entity entity) {
        return getHeight(entity) > 0.0;
    }

    public static boolean isBoatRowingIn(Level level, AABB boundingBox, Supplier<Double> waterLevelGetter, Consumer<Double> waterLevelSetter) {
        int minX = Mth.floor(boundingBox.minX),
            maxX = Mth.ceil(boundingBox.maxX),
            minY = Mth.floor(boundingBox.minY),
            maxY = Mth.ceil(boundingBox.minY + 0.001),
            minZ = Mth.floor(boundingBox.minZ),
            maxZ = Mth.ceil(boundingBox.maxZ);

        boolean inFluid = false;
        waterLevelSetter.accept(-Double.MAX_VALUE);

        for (BlockPos intersectingPos : BlockPos.betweenClosed(minX, minY, minZ, maxX, maxY, maxZ)) {
            FluidState fluidState = level.getFluidState(intersectingPos);

            if (fluidState.is(ModTags.DIMENSIONAL_TEARS)) {
                float fluidLevelAt = intersectingPos.getY() + fluidState.getHeight(level, intersectingPos);
                waterLevelSetter.accept(Math.max(fluidLevelAt, waterLevelGetter.get()));
                inFluid |= minY < fluidLevelAt;
            }
        }

        return inFluid;
    }

    public static void manipulateMovementIn(LivingEntity livingEntity, double gravity, boolean falling, Vec3 original) {
        double oldY = livingEntity.getY();

        livingEntity.moveRelative(0.02F, original);
        livingEntity.move(MoverType.SELF, livingEntity.getDeltaMovement());

        if (getHeight(livingEntity) <= livingEntity.getFluidJumpThreshold()) {
            manipulateDeltaMovement(livingEntity, movement -> movement.multiply(0.5, 0.8F, 0.5));
            manipulateDeltaMovement(livingEntity, movement -> livingEntity.getFluidFallingAdjustedMovement(gravity, falling, movement));
        }
        else manipulateDeltaMovement(livingEntity, movement -> movement.scale(0.5));

        if (gravity != 0.0)
            manipulateDeltaMovement(livingEntity, movement -> movement.add(0.0, -gravity / 4.0, 0.0));

        Vec3 vec = livingEntity.getDeltaMovement().add(0.0, 0.6 - livingEntity.getY() + oldY, 0.0);

        if (livingEntity.horizontalCollision && livingEntity.isFree(vec.x, vec.y, vec.z))
            manipulateDeltaMovement(livingEntity, movement -> movement.with(Direction.Axis.Y, 0.3));
    }

    private static void manipulateDeltaMovement(LivingEntity livingEntity, UnaryOperator<Vec3> operation) {
        livingEntity.setDeltaMovement(operation.apply(livingEntity.getDeltaMovement()));
    }

    public static class Flowing extends DimensionalTearsFluid {

        @Override
        protected void createFluidStateDefinition(StateDefinition.Builder<Fluid, FluidState> builder) {
            super.createFluidStateDefinition(builder.add(LEVEL));
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

    public static class Source extends DimensionalTearsFluid {

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