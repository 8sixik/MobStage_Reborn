package net.sixik.mobstagesreborn.compact.crt;

import com.blamejared.crafttweaker.api.CraftTweakerAPI;
import com.blamejared.crafttweaker.api.annotation.ZenRegister;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import org.openzen.zencode.java.ZenCodeType;

@ZenRegister
@ZenCodeType.Name("mods.MobStagesSixik")
public class MobStagesCrT {
    @ZenCodeType.Method
    public static void addStage (String stage, String entityId) {
        CraftTweakerAPI.apply(new ActionAddEntityStage(stage, entityId));
    }

    @ZenCodeType.Method
    public static void addStage (String stage, String entityId, ResourceLocation dimension) {

        CraftTweakerAPI.apply(new ActionAddEntityStage(stage, entityId, dimension));
    }

    @ZenCodeType.Method
    public static void addReplacement (String entityId, String replacementId) {
        CraftTweakerAPI.apply(new ActionAddSpawnReplacement(entityId, replacementId));
    }

    @ZenCodeType.Method
    public static void addReplacement (String entityId, String replacementId, ResourceLocation dimension) {

        CraftTweakerAPI.apply(new ActionAddSpawnReplacement(entityId, replacementId, dimension));
    }

    @ZenCodeType.Method
    public static void addRange (String entityId, int range) {

        CraftTweakerAPI.apply(new ActionAddSpawnRange(entityId, range));
    }

    @ZenCodeType.Method
    public static void addReplacement (String entityId, int range, ResourceLocation dimension) {

        CraftTweakerAPI.apply(new ActionAddSpawnRange(entityId, range, dimension));
    }

    @ZenCodeType.Method
    public static void toggleSpawner (String entityId, boolean allow) {

        CraftTweakerAPI.apply(new ActionOverlookSpawners(entityId, allow));
    }

    @ZenCodeType.Method
    public static void toggleSpawner (String entityId, boolean allow, ResourceLocation dimension) {

        CraftTweakerAPI.apply(new ActionOverlookSpawners(entityId, allow, dimension));
    }
}
