package com.nikita23830.ewitchery.common.entity;

import com.emoniph.witchery.Witchery;
import com.nikita23830.ewitchery.common.ModItems;
import com.nikita23830.ewitchery.common.utils.DamageSourceAbsolute;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.*;
import net.minecraft.entity.boss.IBossDisplayData;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.DamageSource;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import thaumcraft.common.entities.EntitySpecialItem;
import thaumcraft.common.lib.utils.EntityUtils;

import java.util.UUID;

public class EntityBehemoth extends EntityMob implements IBossDisplayData, IArrowConsumer {

    private int attackTimer = 0;
    public boolean transform = false;
    private int countArrow;
    private UUID summoner;

    public EntityBehemoth(World world, UUID summoner) {
        super(world);
        this.setSize(0.6F, 1.8F);
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
        this.getAttributeMap().getAttributeInstance(SharedMonsterAttributes.maxHealth).setBaseValue(20000.0F);
        super.setHealth(20000.0F);
        this.summoner = summoner;
        this.countArrow = 0;
    }

    protected void entityInit() {
        super.entityInit();
        super.dataWatcher.addObject(16, (byte) 0);
        super.dataWatcher.addObject(17, 0);
        super.dataWatcher.addObject(20, 0);
        super.dataWatcher.addObject(21, summoner.toString());
        super.dataWatcher.addObject(22, countArrow);
    }

    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(20090.0D);
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
        return StatCollector.translateToLocal("entity.AdvanceWitchery.masterVampire.name");
    }

    public void setRevengeTarget(EntityLivingBase entity) {
        if (entity instanceof EntityPlayer)
            super.setRevengeTarget(entity);
    }

    public int getInvulTime() {
        return super.dataWatcher.getWatchableObjectInt(20);
    }

    public void setInvulTime(int par1) {
        super.dataWatcher.updateObject(20, par1);
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
        if (worldObj.isRemote) {
            setArrowCountInEntity(countArrow);
        }
        if (!worldObj.isRemote && ticksExisted % 20 == 0)
            heal(10F);
    }

    public boolean attackEntityAsMob(Entity par1Entity) {
        this.attackTimer = 10;
        super.worldObj.setEntityState(this, (byte)4);
        boolean rareAttack = !worldObj.isRemote && worldObj.rand.nextDouble() <= .2F;
        boolean flag = par1Entity.attackEntityFrom(rareAttack ? DamageSourceAbsolute.source  : DamageSource.causeMobDamage(this), (float)(rareAttack ? 6 : 30));

        this.playSound("mob.irongolem.throw", 1.0F, 1.0F);
        return flag;
    }

    @Override
    public boolean attackEntityFrom(DamageSource source, float dmg) {
        if (source.getEntity() != null && source.getEntity() instanceof EntityPlayer) {
            if (source.getSourceOfDamage() instanceof EntityPlayer) {
                ((EntityPlayer)source.getSourceOfDamage()).addChatComponentMessage(new ChatComponentText("§eMaster Vampire§f> §7Что за жалкая атака"));
            }
            return false;
        }
        return false;
    }

    public void writeEntityToNBT(NBTTagCompound compound) {
        super.writeEntityToNBT(compound);
        compound.setInteger("Invul", this.getInvulTime());
        compound.setString("summoner", this.summoner.toString());
        compound.setInteger("arrow", this.countArrow);
    }

    public void readEntityFromNBT(NBTTagCompound compound) {
        super.readEntityFromNBT(compound);
        this.setInvulTime(compound.getInteger("Invul"));
        this.setSummoner(UUID.fromString(compound.getString("summoner")));
        this.setCountArrow(compound.getInteger("arrow"));
    }

    public void setSummoner(UUID summoner) {
        this.summoner = summoner;
        dataWatcher.updateObject(21, summoner.toString());
    }

    public void setCountArrow(int countArrow) {
        this.countArrow = countArrow;
        dataWatcher.updateObject(22, countArrow);
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
        this.entityDropItem(ModItems.dropBlood.create(), 0);
        EntityUtils.entityDropSpecialItem(this, ModItems.itemHeart.create(3), (this.height / 2.F));
        this.entityDropItem(ModItems.itemHeart.create(3), 0);
        int count = worldObj.rand.nextInt(12);
        do {
            --count;
            ItemStack drop = new ItemStack(Witchery.Items.GENERIC, 1, worldObj.rand.nextInt(Witchery.Items.GENERIC.subItems.size()));
            this.entityDropItem(drop, 0);
        } while (count >= 0);
    }

    public void onDeath(DamageSource source) {
        super.onDeath(source);
    }

    @Override
    public boolean getCanSpawnHere() {
        return true;
    }

    @Override
    public int getMaxSpawnedInChunk() {
        return 10;
    }

    @Override
    public void applySunAttack(Entity attacker, float dmg) {
        if (!(attacker instanceof EntityPlayer))
            return;
        if (worldObj.isRemote)
            return;
        UUID u = attacker.getUniqueID();
        if (!u.equals(summoner))
            return;
        if (countArrow >= 31) {
            onDeath(DamageSource.causePlayerDamage((EntityPlayer) attacker));
            if (!dead || !isDead)
                setDead();
        } else {
            setCountArrow(countArrow + 1);
            setHealth(getHealth() - (getMaxHealth() / 32));
        }
    }

    @Override
    public void applyElfAttack(Entity attacker, float dmg) {

    }

    @Override
    public void applyBloodAttack(Entity attacker, float dmg) {

    }
}
