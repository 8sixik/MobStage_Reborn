package net.sixik.mobstagesreborn.compact.crt;

import com.blamejared.crafttweaker.api.action.base.IAction;
import net.minecraft.resources.ResourceLocation;
import net.sixik.mobstagesreborn.MobStageInfo;
import net.sixik.mobstagesreborn.MobStagesReborn;

public class ActionAddSpawnReplacement implements IAction {
    private final String entityId;
    private final String replacementId;

    private final boolean isDimensional;
    private final ResourceLocation dimension;

    public ActionAddSpawnReplacement (String entity, String replacement) {

        this(entity, replacement, new ResourceLocation("minecraft:overworld"), false);
    }

    public ActionAddSpawnReplacement (String entity, String replacement, ResourceLocation dimension) {

        this(entity, replacement, dimension, true);
    }

    private ActionAddSpawnReplacement (String entity, String replacement, ResourceLocation dimension, boolean isDimensional) {

        this.entityId = entity;
        this.replacementId = replacement;
        this.isDimensional = isDimensional;
        this.dimension = dimension;
    }

    @Override
    public void apply () {

        final MobStageInfo info = this.isDimensional ? MobStagesReborn.DIMENSIONAL_STAGE_INFO.get(this.entityId).get(this.dimension) : MobStagesReborn.GLOBAL_STAGE_INFO.get(this.entityId);
        info.setReplacement(this.replacementId);
    }

    @Override
    public String describe () {

        return "Adding a replacement for " + this.entityId + " to " + this.replacementId + (this.isDimensional ? " Dimension: " + this.dimension : "");
    }
}
