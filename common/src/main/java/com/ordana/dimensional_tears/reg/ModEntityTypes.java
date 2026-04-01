package com.ordana.dimensional_tears.reg;

import com.ordana.dimensional_tears.DimensionalTearsPlatform;
import com.ordana.dimensional_tears.DimensionalTearsRoot;
import com.ordana.dimensional_tears.blocks.DimensionalTearsBlock;
import com.ordana.dimensional_tears.blocks.DimensionalTearsCauldronBlock;
import com.ordana.dimensional_tears.entity.DimensionalBear;
import com.ordana.dimensional_tears.fluids.DimensionalTearsFluid;
import net.mehvahdjukaar.moonlight.api.platform.PlatHelper;
import net.mehvahdjukaar.moonlight.api.platform.RegHelper;
import net.minecraft.core.cauldron.CauldronInteraction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;

import java.util.function.Supplier;
import java.util.function.UnaryOperator;

import static net.mehvahdjukaar.moonlight.api.platform.RegHelper.registerEntityType;

public interface ModEntityTypes {

    Supplier<EntityType<DimensionalBear>> DIMENSIONAL_BEAR = registerEntityType(
            "dimensional_bear",
            DimensionalBear::new,
            MobCategory.CREATURE,
            builder -> builder
                    .sized(1.4F, 1.4F)
                    .clientTrackingRange(10)
    );

    private static <T extends Entity> Supplier<EntityType<T>> registerEntityType(String name, EntityType.EntityFactory<T> factory, MobCategory category, UnaryOperator<EntityType.Builder<T>> operation) {
        EntityType.Builder<T> builder = EntityType.Builder.of(factory, category);
        return RegHelper.registerEntityType(DimensionalTearsRoot.res(name), operation.apply(builder));
    }

    static void init() {

    }

}
