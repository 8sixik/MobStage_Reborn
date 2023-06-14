package net.sixik.mobstagesreborn.compact.crt;

import com.blamejared.crafttweaker.api.action.base.IAction;
import net.minecraft.resources.ResourceLocation;
import net.sixik.mobstagesreborn.MobStageInfo;
import net.sixik.mobstagesreborn.MobStagesReborn;

public class ActionOverlookSpawners implements IAction {
    private final String entityId;
    private final boolean ignoreSpawner;

    private final boolean isDimensional;
    private final ResourceLocation dimension;

    public ActionOverlookSpawners (String entity, boolean ignoreSpawner) {

        this(entity, ignoreSpawner, new ResourceLocation("minecraft:overworld"), false);
    }

    public ActionOverlookSpawners (String entity, boolean ignoreSpawner, ResourceLocation dimension) {

        this(entity, ignoreSpawner, dimension, true);
    }

    private ActionOverlookSpawners (String entity, boolean ignoreSpawner, ResourceLocation dimension, boolean isDimensional) {

        this.entityId = entity;
        this.ignoreSpawner = ignoreSpawner;
        this.isDimensional = isDimensional;
        this.dimension = dimension;
    }

    @Override
    public void apply () {

        if (this.isDimensional && MobStagesReborn.DIMENSIONAL_STAGE_INFO.get(this.entityId) == null) {

            throw new IllegalArgumentException("You must stage " + this.entityId + " before spawner can be toggled!");
        }

        final MobStageInfo info = this.isDimensional ? MobStagesReborn.DIMENSIONAL_STAGE_INFO.get(this.entityId).get(this.dimension) : MobStagesReborn.GLOBAL_STAGE_INFO.get(this.entityId);
        info.setAllowSpawners(this.ignoreSpawner);
    }

    @Override
    public String describe () {

        return "Toggling spawners for " + this.entityId + " to " + this.ignoreSpawner + (this.isDimensional ? " Dimension: " + this.dimension : "");
    }
}
