package com.ordana.dimensional_tears;

import com.ordana.dimensional_tears.networking.RiftingParticleS2CMessage;
import com.ordana.dimensional_tears.reg.ModTags;
//? fabric {
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.loader.api.FabricLoader;
//?} else {
/*import com.ordana.dimensional_tears.neoforge.reg.ModFluidTypes;
*///?}
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.carver.ConfiguredWorldCarver;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.neoforged.fml.loading.FMLPaths;
import net.neoforged.neoforge.network.PacketDistributor;
import org.jetbrains.annotations.Contract;

import java.nio.file.Path;

@SuppressWarnings("unused")
public class DimensionalTearsPlatform {

    @Contract
    public static void addFeatureToBiome(GenerationStep.Decoration step, TagKey<Biome> tagKey, ResourceKey<PlacedFeature> feature) {
        //? fabric
        BiomeModifications.addFeature(BiomeSelectors.tag(tagKey), step, feature);
    }

    @Contract
    public static void addCarverToBiome(TagKey<Biome> tagKey, ResourceKey<ConfiguredWorldCarver<?>> carver) {
        //? fabric
        BiomeModifications.addCarver(BiomeSelectors.tag(tagKey), carver);
    }

    public static boolean isEyeInDimTears(Entity entity) {
        return entity.isEyeInFluid(ModTags.DIMENSIONAL_TEARS);
//        return DimensionalTearsRoot.isInitiated() && entity.isEyeInFluidType(ModFluidTypes.DIMENSIONAL_TEARS_TYPE.get());
    }

    @Contract
    public static double getDimTearsHeight(Entity entity) {
        return entity.getFluidHeight(ModTags.DIMENSIONAL_TEARS);
//        return entity.getFluidTypeHeight(ModFluidTypes.DIMENSIONAL_TEARS_TYPE.get());
    }

    public static boolean isInDimTears(Entity entity) {
        return getDimTearsHeight(entity) > 0;
    }

    public static void addAlias(Registry<?> registry, String path) {
        addAlias(registry, path.replace(DimensionalTearsRoot.MOD_ID, "portal_fluid"), path);
    }

	public static void addAlias(Registry<?> registry, String oldPath, String newPath) {
        addAlias(registry, Identifier.fromNamespaceAndPath("portal_fluid", oldPath), DimensionalTearsRoot.res(newPath));
	}

    public static void addAlias(Registry<?> registry, Identifier oldPath, Identifier newPath) {
        registry.addAlias(oldPath, newPath);
    }

    public static Path getConfigDirectory() {
        //? fabric
        return FabricLoader.getInstance().getConfigDir();
        //? neoforge
        //return FMLPaths.CONFIGDIR.get();
    }

    public static void sendToAllClientPlayersInParticleRange(ServerLevel serverLevel, BlockPos blockPos, RiftingParticleS2CMessage riftingParticleS2CMessage) {
        serverLevel.players().forEach(player -> {
           //? fabric {
           ServerPlayNetworking.send(player, riftingParticleS2CMessage);
           //?} else {
           /*PacketDistributor.sendToPlayer(player, riftingParticleS2CMessage);
           *///?}
        });
    }

    public static SimpleParticleType simpleParticle() {
        //? fabric
        return net.fabricmc.fabric.api.particle.v1.FabricParticleTypes.simple();
        //? neoforge
        //return new SimpleParticleType(false);
    }

    public interface RegisterMessagesEvent {
        void registerClientBound(CustomPacketPayload.TypeAndCodec<RegistryFriendlyByteBuf, RiftingParticleS2CMessage> type);
    }

    public interface CreativeTabEvent {
        ResourceKey<CreativeModeTab> tabKey();
        void insertAfter(ItemStack anchor, ItemStack newItem);
        void insertBefore(ItemStack anchor, ItemStack newItem);
    }
}
