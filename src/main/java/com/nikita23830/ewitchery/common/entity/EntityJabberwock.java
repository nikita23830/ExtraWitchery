package com.nikita23830.ewitchery.common.entity;

import com.emoniph.witchery.Witchery;
import com.emoniph.witchery.entity.EntityHornedHuntsman;
import com.nikita23830.ewitchery.common.ModItems;
import com.nikita23830.ewitchery.common.stats.StatManager;
import com.nikita23830.ewitchery.common.utils.DamageSourceAbsolute;
import com.nikita23830.ewitchery.common.utils.DamageSourceSun;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentData;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.*;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;

public class EntityJabberwock extends EntityMob {
    private int attackTimer = 0;

    public EntityJabberwock(World world) {
        super(world);
        this.setSize(2.2F, 2.6F);
        super.isImmuneToFire = true;
        this.getNavigator().setAvoidsWater(true);
        this.getNavigator().setCanSwim(true);
        super.tasks.addTask(1, new EntityAISwimming(this));
        super.tasks.addTask(2, new EntityAIAttackOnCollide(this, 1.0D, true));
        super.tasks.addTask(4, new EntityAIMoveTowardsTarget(this, 1.0D, 48.0F));
        super.tasks.addTask(5, new EntityAIWander(this, 1.0D));
        super.tasks.addTask(6, new EntityAIWatchClosest(this, EntityPlayer.class, 6.0F));
        super.tasks.addTask(7, new EntityAILookIdle(this));
        super.targetTasks.addTask(1, new EntityAIHurtByTarget(this, false));
        super.targetTasks.addTask(1, new EntityAINearestAttackableTarget(this, EntityPlayer.class, 0, true));
        super.experienceValue = 1225;
        this.getAttributeMap().getAttributeInstance(SharedMonsterAttributes.maxHealth).setBaseValue((double) (300.0F));
        super.setHealth(300.0F);

    }

    protected void entityInit() {
        super.entityInit();
        super.dataWatcher.addObject(16, Byte.valueOf((byte)0));
        super.dataWatcher.addObject(17, Integer.valueOf(0));
        super.dataWatcher.addObject(20, new Integer(0));
    }

    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(300.0D);
        this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.35D);
        this.getEntityAttribute(SharedMonsterAttributes.followRange).setBaseValue(50.0D);
        this.getEntityAttribute(SharedMonsterAttributes.knockbackResistance).setBaseValue(1.0D);
    }

    public int getTotalArmorValue() {
        return 0;
    }

    protected void func_145780_a(int par1, int par2, int par3, Block par4) {
        this.playSound("mob.irongolem.walk", 1.0F, 1.0F);
    }

    public String getCommandSenderName() {
        return StatCollector.translateToLocal("entity.AdvanceWitchery.jabberwock.name");
    }

    public void setRevengeTarget(EntityLivingBase entity) {
        if (entity instanceof EntityPlayer)
            super.setRevengeTarget(entity);
    }

    public int getInvulTime() {
        return super.dataWatcher.getWatchableObjectInt(20);
    }

    public void setInvulTime(int par1) {
        super.dataWatcher.updateObject(20, Integer.valueOf(par1));
    }

    public void init() {
        this.setInvulTime(150);
        this.setHealth(this.getMaxHealth() / 4.0F);
    }

    public boolean isAIEnabled() {
        return true;
    }

    protected void updateAITick() {
        super.updateAITick();
    }

    protected void updateAITasks() {
        if(this.getInvulTime() > 0) {
            int i = this.getInvulTime() - 1;
            if(i <= 0) {
                super.worldObj.playBroadcastSound(1013, (int)super.posX, (int)super.posY, (int)super.posZ, 0);
            }

            this.setInvulTime(i);
        } else {
            super.updateAITasks();
        }

    }

    protected int decreaseAirSupply(int par1) {
        return par1;
    }

    protected void collideWithEntity(Entity par1Entity) {
        super.collideWithEntity(par1Entity);
    }

    public void onLivingUpdate() {
        super.onLivingUpdate();
        if(this.attackTimer > 0) {
            --this.attackTimer;
        }

    }

    public boolean attackEntityAsMob(Entity par1Entity) {
        this.attackTimer = 10;
        super.worldObj.setEntityState(this, (byte)4);
        boolean isServer = !worldObj.isRemote;
        boolean flag = par1Entity.attackEntityFrom(isServer && worldObj.rand.nextDouble() < .5D ? DamageSourceAbsolute.source : DamageSource.causeMobDamage(this), (float)(6));
        this.playSound("mob.irongolem.throw", 1.0F, 1.0F);
        return flag;
    }

    @Override
    public boolean attackEntityFrom(DamageSource source, float dmg) {
        if (source.getSourceOfDamage() instanceof EntityPlayer) {
            if (worldObj.rand.nextDouble() <= .2D && !worldObj.isRemote) {
                int count = 1 + worldObj.rand.nextInt(3);
                do {
                    --count;
                    EntityWolf ew = new EntityWolf(worldObj);
                    ew.setAngry(true);
                    ew.setAttackTarget((EntityLivingBase) source.getSourceOfDamage());
                    ew.setPositionAndRotation(posX, posY, posZ, rotationYaw, rotationPitch);
                    worldObj.spawnEntityInWorld(ew);
                } while (count >= 0);
            }
            return super.attackEntityFrom(source, Math.min(dmg, 15F));
        }
        return false;
    }

    public void writeEntityToNBT(NBTTagCompound par1NBTTagCompound) {
        super.writeEntityToNBT(par1NBTTagCompound);
        par1NBTTagCompound.setInteger("Invul", this.getInvulTime());
    }

    public void readEntityFromNBT(NBTTagCompound par1NBTTagCompound) {
        super.readEntityFromNBT(par1NBTTagCompound);
        this.setInvulTime(par1NBTTagCompound.getInteger("Invul"));
    }

    @SideOnly(Side.CLIENT)
    public void handleHealthUpdate(byte par1) {
        if(par1 == 4) {
            this.attackTimer = 10;
            this.playSound("mob.irongolem.throw", 1.0F, 1.0F);
        } else {
            super.handleHealthUpdate(par1);
        }

    }

    @SideOnly(Side.CLIENT)
    public int getAttackTimer() {
        return this.attackTimer;
    }

    protected String getDeathSound() {
        return "mob.irongolem.death";
    }

    public float getBrightness(float par1) {
        return 1.0F;
    }

    @Override
    protected void dropFewItems(boolean p_70628_1_, int p_70628_2_) {
        this.entityDropItem(ModItems.itemHeart.create(1), 0);
        this.entityDropItem(new ItemStack(Items.skull, this.worldObj.rand.nextInt(3) == 0 ? 3 : 2, 1), 0.0F);
        Enchantment enchantment = Enchantment.enchantmentsBookList[this.rand.nextInt(Enchantment.enchantmentsBookList.length)];
        int k = MathHelper.getRandomIntegerInRange(this.rand, Math.min(enchantment.getMinLevel() + 2, enchantment.getMaxLevel()), enchantment.getMaxLevel());
        ItemStack itemstack = Items.enchanted_book.getEnchantedItemStack(new EnchantmentData(enchantment, k));
        this.entityDropItem(itemstack, 0.0F);
        this.entityDropItem(Witchery.Items.GENERIC.itemInfernalBlood.createStack(), 0.0F);
        if (this.worldObj.rand.nextInt(4) == 0) {
            this.entityDropItem(new ItemStack(Witchery.Items.HUNTSMANS_SPEAR), 0.0F);
        }
    }

    public void onDeath(DamageSource source) {
        super.onDeath(source);
        if (source.getSourceOfDamage() instanceof EntityPlayer && !worldObj.isRemote && StatManager.hasStat(StatManager.readFourPart, (EntityPlayer) source.getSourceOfDamage())) {
            ((EntityPlayer)source.getSourceOfDamage()).triggerAchievement(StatManager.killJabberwock);
        }
    }

    @Override
    public boolean getCanSpawnHere() {
        return true;
    }

    @Override
    public int getMaxSpawnedInChunk() {
        return 10;
    }
}
