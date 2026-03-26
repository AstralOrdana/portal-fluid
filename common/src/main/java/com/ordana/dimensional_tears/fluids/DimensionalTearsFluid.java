package com.ordana.dimensional_tears.fluids;

import com.ordana.dimensional_tears.configs.CommonConfigs;
import com.ordana.dimensional_tears.reg.*;
import com.ordana.dimensional_tears.util.DimensionalTearsAmbience;
import com.ordana.dimensional_tears.util.TeleportHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.TicketType;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;

public abstract class DimensionalTearsFluid extends FlowingFluid {

    public static final int LUMINANCE = 5;

    @Override
    protected boolean canConvertToSource(Level level) {
        return CommonConfigs.DIMENSIONAL_TEARS_SOURCE_CONVERSION.get();
    }

    /**
     * Spawns the drops directly at the overworld spawn position. More performant than spawning the item entities and having them teleport of their own accord.
     */
    @Override
    protected void beforeDestroyingBlock(LevelAccessor levelAccessor, BlockPos blockPos, BlockState blockState) {
        if (levelAccessor instanceof ServerLevel serverLevel && serverLevel.getGameRules().getBoolean(GameRules.RULE_DOBLOCKDROPS)) {
            BlockEntity blockEntity = blockState.hasBlockEntity() ? levelAccessor.getBlockEntity(blockPos) : null;
            List<ItemStack> drops = Block.getDrops(blockState, serverLevel, blockPos, blockEntity);

            if (drops.isEmpty())
                return;

            boolean poppedResource = false;

            ServerLevel overworld = serverLevel.getServer().overworld();
            BlockPos heightmapPos = overworld.getHeightmapPos(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, overworld.getSharedSpawnPos());

            for (ItemStack itemStack : drops) {
                if (!itemStack.isEmpty()) {
                    Block.popResource(overworld, heightmapPos, itemStack);
                    poppedResource = true;
                }
            }

            if (poppedResource) {
                overworld.getChunkSource().addRegionTicket(TicketType.PORTAL, new ChunkPos(heightmapPos), 3, heightmapPos);
                TeleportHelper.playTeleportSound(serverLevel, blockPos.getCenter());
            }
        }
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
    protected boolean canBeReplacedWith(FluidState fluidState, BlockGetter blockGetter, BlockPos blockPos, Fluid fluid, Direction direction) {
        return direction == Direction.DOWN && !this.isSame(fluid);
    }

    @Override
    public void animateTick(Level level, BlockPos blockPos, FluidState fluidState, RandomSource randomSource) {
        DimensionalTearsAmbience.tryAnimate(level, blockPos, fluidState.getHeight(level, blockPos), randomSource);
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
        return CommonConfigs.FLUID_FLOWING_TICK_RATE.get();
    }

    @Override
    protected float getExplosionResistance() {
        return 100.0F;
    }

    @Override
    @NotNull
    protected BlockState createLegacyBlock(FluidState fluidState) {
        return ModBlocks.DIMENSIONAL_TEARS.get().defaultBlockState().setValue(LiquidBlock.LEVEL, getLegacyLevel(fluidState));
    }

    @Override
    public boolean isSame(Fluid fluid) {
        return fluid == ModFluids.DIMENSIONAL_TEARS.get() || fluid == ModFluids.FLOWING_DIMENSIONAL_TEARS.get();
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

    public static void move(LivingEntity livingEntity, double gravity, boolean falling, Vec3 original) {
        double oldY = livingEntity.getY();

        livingEntity.moveRelative(0.02F, original);
        livingEntity.move(MoverType.SELF, livingEntity.getDeltaMovement());

        applyDelta(livingEntity, v -> v.multiply(0.5, 0.8, 0.5));
        applyDelta(livingEntity, v -> livingEntity.getFluidFallingAdjustedMovement(gravity, falling, v));

        Vec3 vec = livingEntity.getDeltaMovement().add(0.0, 0.6 - livingEntity.getY() + oldY, 0.0);

        if (livingEntity.horizontalCollision && livingEntity.isFree(vec.x, vec.y, vec.z))
            applyDelta(livingEntity, v -> v.with(Direction.Axis.Y, 0.3));
    }

    private static void applyDelta(LivingEntity livingEntity, UnaryOperator<Vec3> operation) {
        livingEntity.setDeltaMovement(operation.apply(livingEntity.getDeltaMovement()));
    }

    public static double motionScale() {
        return 0.07 * (1.0 / CommonConfigs.FLUID_FLOWING_TICK_RATE.get());
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