package com.nikita23830.ewitchery.common.entity.ai;

import net.minecraft.entity.ai.EntityAIBase;

public class EntityAIFindHome extends EntityAIBase {
    private final IEntityHaveHomePoint entity;

    public EntityAIFindHome(IEntityHaveHomePoint entity) {
        this.entity = entity;
    }

    @Override
    public boolean shouldExecute() {
        return entity.getInstance().getDistanceSq(
                entity.getHome().posX + .5D,
                entity.getHome().posY + .5D,
                entity.getHome().posZ + .5D) >= 64D;
    }

    @Override
    public void startExecuting() {
        super.startExecuting();
        entity.getInstance().getNavigator().tryMoveToXYZ(
                entity.getHome().posX + .5D,
                entity.getHome().posY + .5D,
                entity.getHome().posZ + .5D,
                1D
        );
    }

    @Override
    public void resetTask() {
        super.resetTask();
        entity.getInstance().getNavigator().clearPathEntity();
    }
}
