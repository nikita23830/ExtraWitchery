package com.nikita23830.ewitchery.common.entity;

import net.minecraft.world.World;

public class EntityBlaze extends net.minecraft.entity.monster.EntityBlaze {

    public EntityBlaze(World world) {
        super(world);
    }

    public static void freeze(net.minecraft.entity.monster.EntityBlaze blaze) {
        if (blaze.worldObj.isRemote)
            return;
        EntityBlaze bl = new EntityBlaze(blaze.worldObj);
        bl.setHealth(blaze.getHealth());
        if (blaze.getEntityToAttack() != null)
            bl.setTarget(blaze.getEntityToAttack());
        if (blaze.getAttackTarget() != null)
            bl.setAttackTarget(blaze.getAttackTarget());
        if (blaze.getAITarget() != null)
            bl.setRevengeTarget(blaze.getAITarget());
        bl.setPositionAndRotation(blaze.posX, blaze.posY, blaze.posZ, blaze.rotationYaw, blaze.rotationPitch);
        blaze.worldObj.spawnEntityInWorld(bl);
        blaze.setDead();
    }

    public void onLivingUpdate() {
        super.onLivingUpdate();
        motionX = motionY = motionZ = 0F;
    }
}
