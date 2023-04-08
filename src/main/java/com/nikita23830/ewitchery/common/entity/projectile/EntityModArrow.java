package com.nikita23830.ewitchery.common.entity.projectile;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IProjectile;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.play.server.S2BPacketChangeGameState;
import net.minecraft.util.*;
import net.minecraft.world.World;

import java.util.List;

public abstract class EntityModArrow extends Entity implements IProjectile {
    public int field_145791_d = -1;
    public int field_145792_e = -1;
    public int field_145789_f = -1;
    public Block field_145790_g;
    public int inData;
    public boolean inGround;
    public int canBePickedUp;
    public int arrowShake;
    public Entity shootingEntity;
    public int ticksInGround;
    public int ticksInAir;
    public double damage = 2.0D;
    public int knockbackStrength;

    public EntityModArrow(World world) {
        super(world);
        this.renderDistanceWeight = 10.0D;
        this.setSize(0.5F, 0.5F);
    }

    public EntityModArrow(World world, double posX, double posY, double posZ) {
        super(world);
        this.renderDistanceWeight = 10.0D;
        this.setSize(0.5F, 0.5F);
        this.setPosition(posX, posY, posZ);
        this.yOffset = 0.0F;
    }

    public EntityModArrow(World world, EntityLivingBase summoner, EntityLivingBase target, float speed0, float speed1)  {
        super(world);
        this.renderDistanceWeight = 10.0D;
        this.shootingEntity = summoner;

        if (summoner instanceof EntityPlayer) {
            this.canBePickedUp = 1;
        }

        this.posY = summoner.posY + (double)summoner.getEyeHeight() - 0.10000000149011612D;
        double d0 = target.posX - summoner.posX;
        double d1 = target.boundingBox.minY + (double)(target.height / 3.0F) - this.posY;
        double d2 = target.posZ - summoner.posZ;
        double d3 = (double) MathHelper.sqrt_double(d0 * d0 + d2 * d2);

        if (d3 >= 1.0E-7D) {
            float f2 = (float)(Math.atan2(d2, d0) * 180.0D / Math.PI) - 90.0F;
            float f3 = (float)(-(Math.atan2(d1, d3) * 180.0D / Math.PI));
            double d4 = d0 / d3;
            double d5 = d2 / d3;
            this.setLocationAndAngles(summoner.posX + d4, this.posY, summoner.posZ + d5, f2, f3);
            this.yOffset = 0.0F;
            float f4 = (float)d3 * 0.2F;
            this.setThrowableHeading(d0, d1 + (double)f4, d2, speed0, speed1);
        }
    }

    public EntityModArrow(World world, EntityLivingBase summoner, float speed) {
        super(world);
        this.renderDistanceWeight = 10.0D;
        this.shootingEntity = summoner;

        if (summoner instanceof EntityPlayer)  {
            this.canBePickedUp = 1;
        }

        this.setSize(0.5F, 0.5F);
        this.setLocationAndAngles(summoner.posX, summoner.posY + (double)summoner.getEyeHeight(), summoner.posZ, summoner.rotationYaw, summoner.rotationPitch);
        this.posX -= (double)(MathHelper.cos(this.rotationYaw / 180.0F * (float)Math.PI) * 0.16F);
        this.posY -= 0.10000000149011612D;
        this.posZ -= (double)(MathHelper.sin(this.rotationYaw / 180.0F * (float)Math.PI) * 0.16F);
        this.setPosition(this.posX, this.posY, this.posZ);
        this.yOffset = 0.0F;
        this.motionX = (double)(-MathHelper.sin(this.rotationYaw / 180.0F * (float)Math.PI) * MathHelper.cos(this.rotationPitch / 180.0F * (float)Math.PI));
        this.motionZ = (double)(MathHelper.cos(this.rotationYaw / 180.0F * (float)Math.PI) * MathHelper.cos(this.rotationPitch / 180.0F * (float)Math.PI));
        this.motionY = (double)(-MathHelper.sin(this.rotationPitch / 180.0F * (float)Math.PI));
        this.setThrowableHeading(this.motionX, this.motionY, this.motionZ, speed * 1.5F, 1.0F);
    }

    public Entity getShootingEntity() {
        return shootingEntity;
    }

    public EntityPlayer getShootingPlayer() {
        return shootingEntity instanceof EntityPlayer ? (EntityPlayer) shootingEntity : null;
    }

    protected void entityInit()
    {
        this.dataWatcher.addObject(16, Byte.valueOf((byte)0));
    }

    public void setThrowableHeading(double motionX, double motionY, double motionZ, float speed, float size) {
        float f2 = MathHelper.sqrt_double(motionX * motionX + motionY * motionY + motionZ * motionZ);
        motionX /= (double)f2;
        motionY /= (double)f2;
        motionZ /= (double)f2;
        motionX += this.rand.nextGaussian() * (double)(this.rand.nextBoolean() ? -1 : 1) * 0.007499999832361937D * (double)size;
        motionY += this.rand.nextGaussian() * (double)(this.rand.nextBoolean() ? -1 : 1) * 0.007499999832361937D * (double)size;
        motionZ += this.rand.nextGaussian() * (double)(this.rand.nextBoolean() ? -1 : 1) * 0.007499999832361937D * (double)size;
        motionX *= (double)speed;
        motionY *= (double)speed;
        motionZ *= (double)speed;
        this.motionX = motionX;
        this.motionY = motionY;
        this.motionZ = motionZ;
        float f3 = MathHelper.sqrt_double(motionX * motionX + motionZ * motionZ);
        this.prevRotationYaw = this.rotationYaw = (float)(Math.atan2(motionY, motionZ) * 180.0D / Math.PI);
        this.prevRotationPitch = this.rotationPitch = (float)(Math.atan2(motionY, (double)f3) * 180.0D / Math.PI);
        this.ticksInGround = 0;
    }

    @SideOnly(Side.CLIENT)
    public void setPositionAndRotation2(double posX, double posY, double posZ, float yaw, float pitch, int p_70056_9_) {
        this.setPosition(posX, posY, posZ);
        this.setRotation(yaw, pitch);
    }

    @SideOnly(Side.CLIENT)
    public void setVelocity(double posX, double posY, double posZ) {
        this.motionX = posX;
        this.motionY = posY;
        this.motionZ = posZ;

        if (this.prevRotationPitch == 0.0F && this.prevRotationYaw == 0.0F) {
            float f = MathHelper.sqrt_double(posX * posX + posZ * posZ);
            this.rotationYaw = (float)(Math.atan2(posX, posZ) * 180.0D / Math.PI);
            this.rotationPitch = (float)(Math.atan2(posY, (double)f) * 180.0D / Math.PI);
            this.prevRotationPitch = this.rotationPitch;
            this.prevRotationYaw = this.rotationYaw;
            this.setLocationAndAngles(this.posX, this.posY, this.posZ, this.rotationYaw, this.rotationPitch);
            this.ticksInGround = 0;
        }
    }

    protected abstract boolean collideBlock(int x, int y, int z);

    protected abstract boolean collideEntity(Entity entity);

    @SideOnly(Side.CLIENT)
    public abstract ResourceLocation getLocationRender();

    public void onUpdate() {
        super.onUpdate();

        if (this.prevRotationPitch == 0.0F && this.prevRotationYaw == 0.0F) {
            float f = MathHelper.sqrt_double(this.motionX * this.motionX + this.motionZ * this.motionZ);
            this.prevRotationYaw = this.rotationYaw = (float)(Math.atan2(this.motionX, this.motionZ) * 180.0D / Math.PI);
            this.prevRotationPitch = this.rotationPitch = (float)(Math.atan2(this.motionY, (double)f) * 180.0D / Math.PI);
        }

        Block block = this.worldObj.getBlock(this.field_145791_d, this.field_145792_e, this.field_145789_f);

        if (block.getMaterial() != Material.air) {
            block.setBlockBoundsBasedOnState(this.worldObj, this.field_145791_d, this.field_145792_e, this.field_145789_f);
            AxisAlignedBB axis = block.getCollisionBoundingBoxFromPool(this.worldObj, this.field_145791_d, this.field_145792_e, this.field_145789_f);

            if (axis != null && axis.isVecInside(Vec3.createVectorHelper(this.posX, this.posY, this.posZ))) {
                if (!this.inGround && collideBlock(this.field_145791_d, this.field_145792_e, this.field_145789_f)) {
                    setDead();
                    return;
                }
                this.inGround = true;
            }
        }

        if (this.arrowShake > 0) {
            --this.arrowShake;
        }

        if (this.inGround) {
            int j = this.worldObj.getBlockMetadata(this.field_145791_d, this.field_145792_e, this.field_145789_f);

            if (block == this.field_145790_g && j == this.inData) {
                ++this.ticksInGround;

                if (this.ticksInGround == 1200) {
                    this.setDead();
                }
            } else {
                this.inGround = false;
                this.motionX *= (double)(this.rand.nextFloat() * 0.2F);
                this.motionY *= (double)(this.rand.nextFloat() * 0.2F);
                this.motionZ *= (double)(this.rand.nextFloat() * 0.2F);
                this.ticksInGround = 0;
                this.ticksInAir = 0;
            }
        } else {
            ++this.ticksInAir;
            Vec3 vec31 = Vec3.createVectorHelper(this.posX, this.posY, this.posZ);
            Vec3 vec3 = Vec3.createVectorHelper(this.posX + this.motionX, this.posY + this.motionY, this.posZ + this.motionZ);
            MovingObjectPosition movingobjectposition = this.worldObj.func_147447_a(vec31, vec3, false, true, false);
            vec31 = Vec3.createVectorHelper(this.posX, this.posY, this.posZ);
            vec3 = Vec3.createVectorHelper(this.posX + this.motionX, this.posY + this.motionY, this.posZ + this.motionZ);

            if (movingobjectposition != null) {
                vec3 = Vec3.createVectorHelper(movingobjectposition.hitVec.xCoord, movingobjectposition.hitVec.yCoord, movingobjectposition.hitVec.zCoord);
            }

            Entity entity = null;
            List list = this.worldObj.getEntitiesWithinAABBExcludingEntity(this, this.boundingBox.addCoord(this.motionX, this.motionY, this.motionZ).expand(1.0D, 1.0D, 1.0D));
            double d0 = 0.0D;
            int i;
            float f1;

            for (i = 0; i < list.size(); ++i) {
                Entity entity1 = (Entity)list.get(i);

                if (entity1.canBeCollidedWith() && (entity1 != this.shootingEntity || this.ticksInAir >= 5)) {
                    f1 = 0.3F;
                    AxisAlignedBB axisalignedbb1 = entity1.boundingBox.expand((double)f1, (double)f1, (double)f1);
                    MovingObjectPosition movingobjectposition1 = axisalignedbb1.calculateIntercept(vec31, vec3);

                    if (movingobjectposition1 != null) {
                        double d1 = vec31.distanceTo(movingobjectposition1.hitVec);

                        if (d1 < d0 || d0 == 0.0D) {
                            entity = entity1;
                            d0 = d1;
                        }
                    }
                }
            }

            if (entity != null) {
                movingobjectposition = new MovingObjectPosition(entity);
            }

            if (movingobjectposition != null && movingobjectposition.entityHit != null && movingobjectposition.entityHit instanceof EntityPlayer) {
                EntityPlayer entityplayer = (EntityPlayer)movingobjectposition.entityHit;

                if (entityplayer.capabilities.disableDamage || this.shootingEntity instanceof EntityPlayer && !((EntityPlayer)this.shootingEntity).canAttackPlayer(entityplayer)) {
                    movingobjectposition = null;
                }
            }

            float f2;
            float f4;

            if (movingobjectposition != null) {
                if (movingobjectposition.entityHit != null) {
                    if (collideEntity(movingobjectposition.entityHit)) {
                        setDead();
                        return;
                    }
                } else {
                    this.field_145791_d = movingobjectposition.blockX;
                    this.field_145792_e = movingobjectposition.blockY;
                    this.field_145789_f = movingobjectposition.blockZ;
                    this.field_145790_g = this.worldObj.getBlock(this.field_145791_d, this.field_145792_e, this.field_145789_f);
                    this.inData = this.worldObj.getBlockMetadata(this.field_145791_d, this.field_145792_e, this.field_145789_f);
                    this.motionX = (double) ((float) (movingobjectposition.hitVec.xCoord - this.posX));
                    this.motionY = (double) ((float) (movingobjectposition.hitVec.yCoord - this.posY));
                    this.motionZ = (double) ((float) (movingobjectposition.hitVec.zCoord - this.posZ));
                    f2 = MathHelper.sqrt_double(this.motionX * this.motionX + this.motionY * this.motionY + this.motionZ * this.motionZ);
                    this.posX -= this.motionX / (double) f2 * 0.05000000074505806D;
                    this.posY -= this.motionY / (double) f2 * 0.05000000074505806D;
                    this.posZ -= this.motionZ / (double) f2 * 0.05000000074505806D;
                    this.playSound("random.bowhit", 1.0F, 1.2F / (this.rand.nextFloat() * 0.2F + 0.9F));
                    if (!this.inGround && collideBlock(this.field_145791_d, this.field_145792_e, this.field_145789_f)) {
                        setDead();
                        return;
                    }
                    this.inGround = true;
                    this.arrowShake = 7;
                    this.setIsCritical(false);

                    if (this.field_145790_g.getMaterial() != Material.air) {
                        this.field_145790_g.onEntityCollidedWithBlock(this.worldObj, this.field_145791_d, this.field_145792_e, this.field_145789_f, this);
                    }
                }
            }

            if (this.getIsCritical()) {
                for (i = 0; i < 4; ++i) {
                    this.worldObj.spawnParticle("crit", this.posX + this.motionX * (double)i / 4.0D, this.posY + this.motionY * (double)i / 4.0D, this.posZ + this.motionZ * (double)i / 4.0D, -this.motionX, -this.motionY + 0.2D, -this.motionZ);
                }
            }

            this.posX += this.motionX;
            this.posY += this.motionY;
            this.posZ += this.motionZ;
            f2 = MathHelper.sqrt_double(this.motionX * this.motionX + this.motionZ * this.motionZ);
            this.rotationYaw = (float)(Math.atan2(this.motionX, this.motionZ) * 180.0D / Math.PI);

            for (this.rotationPitch = (float)(Math.atan2(this.motionY, (double)f2) * 180.0D / Math.PI); this.rotationPitch - this.prevRotationPitch < -180.0F; this.prevRotationPitch -= 360.0F) {
                // NO-OP
            }

            while (this.rotationPitch - this.prevRotationPitch >= 180.0F) {
                this.prevRotationPitch += 360.0F;
            }

            while (this.rotationYaw - this.prevRotationYaw < -180.0F) {
                this.prevRotationYaw -= 360.0F;
            }

            while (this.rotationYaw - this.prevRotationYaw >= 180.0F) {
                this.prevRotationYaw += 360.0F;
            }

            this.rotationPitch = this.prevRotationPitch + (this.rotationPitch - this.prevRotationPitch) * 0.2F;
            this.rotationYaw = this.prevRotationYaw + (this.rotationYaw - this.prevRotationYaw) * 0.2F;
            float f3 = 0.99F;
            f1 = 0.05F;

            if (this.isInWater()) {
                for (int l = 0; l < 4; ++l) {
                    f4 = 0.25F;
                    this.worldObj.spawnParticle("bubble", this.posX - this.motionX * (double)f4, this.posY - this.motionY * (double)f4, this.posZ - this.motionZ * (double)f4, this.motionX, this.motionY, this.motionZ);
                }

                f3 = 0.8F;
            }

            if (this.isWet()) {
                this.extinguish();
            }

            this.motionX *= (double)f3;
            this.motionY *= (double)f3;
            this.motionZ *= (double)f3;
            this.motionY -= (double)f1;
            this.setPosition(this.posX, this.posY, this.posZ);
            this.func_145775_I();
        }
    }

    public void writeEntityToNBT(NBTTagCompound compound) {
        compound.setShort("xTile", (short)this.field_145791_d);
        compound.setShort("yTile", (short)this.field_145792_e);
        compound.setShort("zTile", (short)this.field_145789_f);
        compound.setShort("life", (short)this.ticksInGround);
        compound.setByte("inTile", (byte)Block.getIdFromBlock(this.field_145790_g));
        compound.setByte("inData", (byte)this.inData);
        compound.setByte("shake", (byte)this.arrowShake);
        compound.setByte("inGround", (byte)(this.inGround ? 1 : 0));
        compound.setByte("pickup", (byte)this.canBePickedUp);
        compound.setDouble("damage", this.damage);
    }

    public void readEntityFromNBT(NBTTagCompound compound) {
        this.field_145791_d = compound.getShort("xTile");
        this.field_145792_e = compound.getShort("yTile");
        this.field_145789_f = compound.getShort("zTile");
        this.ticksInGround = compound.getShort("life");
        this.field_145790_g = Block.getBlockById(compound.getByte("inTile") & 255);
        this.inData = compound.getByte("inData") & 255;
        this.arrowShake = compound.getByte("shake") & 255;
        this.inGround = compound.getByte("inGround") == 1;

        if (compound.hasKey("damage", 99))
        {
            this.damage = compound.getDouble("damage");
        }

        if (compound.hasKey("pickup", 99))
        {
            this.canBePickedUp = compound.getByte("pickup");
        }
        else if (compound.hasKey("player", 99))
        {
            this.canBePickedUp = compound.getBoolean("player") ? 1 : 0;
        }
    }

    public void onCollideWithPlayer(EntityPlayer player) {
        if (!this.worldObj.isRemote && this.inGround && this.arrowShake <= 0) {
            boolean flag = this.canBePickedUp == 1 || this.canBePickedUp == 2 && player.capabilities.isCreativeMode;

            if (this.canBePickedUp == 1 && !player.inventory.addItemStackToInventory(getPickItemStack())) {
                flag = false;
            }

            if (flag) {
                this.playSound("random.pop", 0.2F, ((this.rand.nextFloat() - this.rand.nextFloat()) * 0.7F + 1.0F) * 2.0F);
                player.onItemPickup(this, 1);
                this.setDead();
            }
        }
    }

    protected abstract ItemStack getPickItemStack();

    protected boolean canTriggerWalking() {
        return false;
    }

    @SideOnly(Side.CLIENT)
    public float getShadowSize() {
        return 0.0F;
    }

    public void setDamage(double damage) {
        this.damage = damage;
    }

    public double getDamage() {
        return this.damage;
    }

    public void setKnockbackStrength(int knockbackStrength) {
        this.knockbackStrength = knockbackStrength;
    }

    public boolean canAttackWithItem() {
        return false;
    }

    public void setIsCritical(boolean isCritical) {
        byte b0 = this.dataWatcher.getWatchableObjectByte(16);

        if (isCritical) {
            this.dataWatcher.updateObject(16, Byte.valueOf((byte)(b0 | 1)));
        } else {
            this.dataWatcher.updateObject(16, Byte.valueOf((byte)(b0 & -2)));
        }
    }

    public boolean getIsCritical() {
        byte b0 = this.dataWatcher.getWatchableObjectByte(16);
        return (b0 & 1) != 0;
    }
}
