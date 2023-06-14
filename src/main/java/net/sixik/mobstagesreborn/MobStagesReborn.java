package net.sixik.mobstagesreborn;

import com.blamejared.crafttweaker.api.CraftTweakerAPI;
import com.mojang.logging.LogUtils;
import net.darkhax.bookshelf.BookshelfForge;
import net.darkhax.bookshelf.api.util.EntityHelper;
import net.darkhax.bookshelf.impl.BookshelfCommon;
import net.darkhax.gamestages.GameStageHelper;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.entity.living.LivingSpawnEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.*;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.network.PlayMessages;
import net.minecraftforge.registries.ForgeRegistries;
import net.sixik.mobstagesreborn.compact.crt.ActionAddEntityStage;
import org.slf4j.Logger;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(MobStagesReborn.MODID)
public class MobStagesReborn {
    public static final Map<String, MobStageInfo> GLOBAL_STAGE_INFO = new HashMap<>();
    public static final Map<String, Map<ResourceLocation, MobStageInfo>> DIMENSIONAL_STAGE_INFO = new HashMap<>();

    public static final String MODID = "mobstagesreborn";
    private static final Logger LOGGER = LogUtils.getLogger();

    public MobStagesReborn() {
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();


        bus.addListener(this::onLoadComplete);
        MinecraftForge.EVENT_BUS.register(this);
    }

    public void onLoadComplete (FMLLoadCompleteEvent event) {
        for (final Map.Entry<String, MobStageInfo> entry : GLOBAL_STAGE_INFO.entrySet()) {
            if (!ForgeRegistries.ENTITIES.containsKey(new ResourceLocation(entry.getValue().getEntityId()))) {
                CraftTweakerAPI.LOGGER.error("No entity found for " + entry.getValue().getEntityId());
            }
        }

        for (final String stage : DIMENSIONAL_STAGE_INFO.keySet()) {

            for (final Map.Entry<ResourceLocation, MobStageInfo> entry : DIMENSIONAL_STAGE_INFO.get(stage).entrySet()) {

                if (!ForgeRegistries.ENTITIES.containsKey(new ResourceLocation(entry.getValue().getEntityId()))) {

                    CraftTweakerAPI.LOGGER.error("No entity found for " + entry.getValue().getEntityId());
                }
            }
        }
    }
    public static MobStageInfo getOrCreateStageInfo (String stage, String entity) {
        final MobStageInfo info = GLOBAL_STAGE_INFO.getOrDefault(entity, new MobStageInfo(stage, entity));

        GLOBAL_STAGE_INFO.put(entity, info);
        return info;
    }

    public static MobStageInfo getOrCreateStageInfo (String stage, String entity, ResourceLocation dimension) {

        final Map<ResourceLocation, MobStageInfo> map = DIMENSIONAL_STAGE_INFO.getOrDefault(entity, new HashMap<>());
        final MobStageInfo info = map.getOrDefault(dimension, new MobStageInfo(stage, entity, dimension));

        map.put(dimension, info);
        DIMENSIONAL_STAGE_INFO.put(entity, map);
        return info;
    }

    public static boolean hasGlobalStage (String entityId) {

        return GLOBAL_STAGE_INFO.get(entityId) != null;
    }

    public static boolean hasDimensionStage (String entityId, ResourceLocation dimension) {

        return DIMENSIONAL_STAGE_INFO.containsKey(entityId) && DIMENSIONAL_STAGE_INFO.get(entityId).get(dimension) != null;
    }

    @SubscribeEvent
    public void checkSpawn (LivingSpawnEvent.CheckSpawn event) {

        final ResourceLocation id = event.getEntity().getType().getRegistryName();

        if (id != null) {

            final String name = id.toString();
            final ResourceLocation dimension = event.getEntity().level.dimension().getRegistryName();

            MobStageInfo info = null;

            if (hasDimensionStage(name, dimension)) {

                info = DIMENSIONAL_STAGE_INFO.get(name).get(dimension);

                if (!allowSpawning(info, event)) {
                    return;
                }
            }

            if (hasGlobalStage(name)) {

                info = GLOBAL_STAGE_INFO.get(name);

                if (!allowSpawning(info, event)) {
                    return;
                }
            }
        }
    }

    private static boolean allowSpawning (MobStageInfo info, LivingSpawnEvent.CheckSpawn event) {

        if (info != null) {

            // Check if spawners are ignored
            if (info.allowSpawners() && event.isSpawner()) {
                return true;
            }

            // Checks if players have the needed stage
            for (final Player player : event.getWorld().players()) {
                player.level.dimension().registry();
                if (GameStageHelper.hasStage(player, info.getStage()) && EntityHelper.getDistanceFromEntity(player, event.getEntity()) < info.getRange()) {
                    return true;
                }
            }

            // If a replacement exists, spawn it. event.getEntity().level.addFreshEntity()
            if (info.getReplacement() != null && !info.getReplacement().isEmpty()) {
                try {
                    final EntityType<Entity> entityList = (EntityType<Entity>) ForgeRegistries.ENTITIES.getValue(new ResourceLocation(info.getReplacement()));
                    final Entity entity = entityList.create(event.getEntity().level);
                    entity.setPos(event.getX(), event.getY(), event.getZ());
                    entity.getLevel().addFreshEntity(entity);
                    event.getEntity().checkDespawn();
                }

                catch (final Exception e) {

                    MobStagesReborn.LOGGER.trace("Failed to spawn replacement mob!", e);
                }
            }
        }

        event.setResult(Event.Result.DENY);
        return false;
    }
}
