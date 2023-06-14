package net.sixik.mobstagesreborn.compact.crt;

import com.blamejared.crafttweaker.api.action.base.IAction;
import net.minecraft.resources.ResourceLocation;
import net.sixik.mobstagesreborn.MobStageInfo;
import net.sixik.mobstagesreborn.MobStagesReborn;

public class ActionAddSpawnRange implements IAction {
    private final String entityId;
    private final int range;

    private final boolean isDimensional;
    private final ResourceLocation dimension;

    public ActionAddSpawnRange (String entity, int range) {

        this(entity, range, new ResourceLocation("minecraft:overworld"), false);
    }

    public ActionAddSpawnRange (String entity, int range, ResourceLocation dimension) {

        this(entity, range, dimension, true);
    }

    private ActionAddSpawnRange (String entity, int range, ResourceLocation dimension, boolean isDimensional) {

        this.entityId = entity;
        this.range = range;
        this.isDimensional = isDimensional;
        this.dimension = dimension;
    }

    @Override
    public void apply () {

        final MobStageInfo info = this.isDimensional ? MobStagesReborn.DIMENSIONAL_STAGE_INFO.get(this.entityId).get(this.dimension) : MobStagesReborn.GLOBAL_STAGE_INFO.get(this.entityId);
        info.setRange(this.range);
    }

    @Override
    public String describe () {

        return "Overriding spawn range of " + this.entityId + " to " + this.range + (this.isDimensional ? " Dimension: " + this.dimension : "");
    }
}
