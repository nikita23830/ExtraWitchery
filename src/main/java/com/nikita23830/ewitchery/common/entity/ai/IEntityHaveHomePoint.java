package com.nikita23830.ewitchery.common.entity.ai;

import net.minecraft.entity.EntityCreature;
import net.minecraft.util.ChunkCoordinates;

public interface IEntityHaveHomePoint {
    ChunkCoordinates getHome();
    void setHome(ChunkCoordinates home);
    <T extends EntityCreature> T getInstance();
}
