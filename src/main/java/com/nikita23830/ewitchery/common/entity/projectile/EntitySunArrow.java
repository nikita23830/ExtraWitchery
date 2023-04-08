package com.nikita23830.ewitchery.common.entity.projectile;

import com.nikita23830.ewitchery.common.ModItems;
import com.nikita23830.ewitchery.common.entity.IArrowConsumer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

public class EntitySunArrow extends EntityModArrow {
    private static final ResourceLocation arrowTextures = new ResourceLocation("textures/entity/arrow.png");

    public EntitySunArrow(World world) {
        super(world);
    }

    public EntitySunArrow(World world, double posX, double posY, double posZ) {
        super(world, posX, posY, posZ);
    }

    public EntitySunArrow(World world, EntityLivingBase summoner, EntityLivingBase target, float speed0, float speed1) {
        super(world, summoner, target, speed0, speed1);
    }

    public EntitySunArrow(World world, EntityLivingBase summoner, float speed) {
        super(world, summoner, speed);
    }

    @Override
    protected boolean collideBlock(int x, int y, int z) {
        if (!worldObj.isRemote) {
            canBePickedUp = 1;
        }
        return false;
    }

    @Override
    protected boolean collideEntity(Entity entity) {
        if (entity instanceof IArrowConsumer) {
            ((IArrowConsumer) entity).applySunAttack(getShootingPlayer(), (float) getDamage());
            return true;
        }
        return false;
    }

    @Override
    public ResourceLocation getLocationRender() {
        return arrowTextures;
    }

    @Override
    protected ItemStack getPickItemStack() {
        return new ItemStack(ModItems.elfArrow);
    }
}
