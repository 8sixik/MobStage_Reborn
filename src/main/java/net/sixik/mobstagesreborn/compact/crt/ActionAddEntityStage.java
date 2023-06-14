package net.sixik.mobstagesreborn.compact.crt;

import com.blamejared.crafttweaker.api.action.base.IAction;
import net.minecraft.resources.ResourceLocation;
import net.sixik.mobstagesreborn.MobStagesReborn;

public class ActionAddEntityStage implements IAction {
    private final String stage;
    private final String entityId;

    private final boolean isDimensional;
    private final ResourceLocation dimension;

    public ActionAddEntityStage (String stage, String entity) {

        this(stage, entity, new ResourceLocation("minecraft:overworld"), false);
    }

    public ActionAddEntityStage (String stage, String entity, ResourceLocation dimension) {

        this(stage, entity, dimension, true);
    }

    private ActionAddEntityStage (String stage, String entity, ResourceLocation dimension, boolean isDimensional) {

        this.stage = stage;
        this.entityId = entity;
        this.isDimensional = isDimensional;
        this.dimension = dimension;
    }

    @Override
    public void apply () {

        if (this.isDimensional) {
            MobStagesReborn.getOrCreateStageInfo(this.stage, this.entityId, this.dimension);
        }
        else {
            MobStagesReborn.getOrCreateStageInfo(this.stage, this.entityId);
        }
    }

    @Override
    public String describe () {

        return "Adding " + this.entityId + " to stage." + this.stage + (this.isDimensional ? " Dimension: " + this.dimension : "");
    }
}
