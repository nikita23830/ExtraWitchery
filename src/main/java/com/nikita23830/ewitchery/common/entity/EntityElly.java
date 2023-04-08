package com.nikita23830.ewitchery.common.entity;

import com.emoniph.witchery.Witchery;
import com.nikita23830.ewitchery.common.entity.ai.EntityAIFindHome;
import com.nikita23830.ewitchery.common.entity.ai.IEntityHaveHomePoint;
import com.nikita23830.ewitchery.common.events.EllyPutItemsEvent;
import com.nikita23830.ewitchery.common.recipes.RecipeManager;
import com.nikita23830.ewitchery.common.recipes.RecipeTradeElle;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentData;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.IMerchant;
import net.minecraft.entity.INpc;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.*;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Items;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.MathHelper;
import net.minecraft.village.MerchantRecipe;
import net.minecraft.village.MerchantRecipeList;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;

import javax.annotation.Nullable;
import java.util.*;

public class EntityElly extends EntityCreature implements IEntityHaveHomePoint, IMerchant, INpc {
    private EntityElly another = null;
    private UUID summoner;
    private ChunkCoordinates home;
    private EntityPlayer buyingPlayer;
    private MerchantRecipeList buyingList = null;

    public EntityElly(World world, EntityPlayer summoner) {
        this(world);
        this.summoner = summoner.getUniqueID();
    }

    public EntityElly(World world) {
        super(world);
        this.setSize(0.6F, 1.8F);
        super.isImmuneToFire = true;
        this.tasks.addTask(0, new EntityAISwimming(this));
        this.tasks.addTask(2, new EntityAIAttackOnCollide(this, EntityElly.class, 1.0D, false));
        this.tasks.addTask(5, new EntityAIMoveTowardsRestriction(this, 1.0D));
        this.tasks.addTask(7, new EntityAIWander(this, 1.0D));
        this.tasks.addTask(8, new EntityAIWatchClosest(this, EntityElly.class, 8.0F));
        this.tasks.addTask(8, new EntityAILookIdle(this));
        this.tasks.addTask(0, new EntityAIFindHome(this));
        this.targetTasks.addTask(1, new EntityAIHurtByTarget(this, true));
        this.targetTasks.addTask(2, new EntityAINearestAttackableTarget(this, EntityElly.class, 0, true));
        this.getAttributeMap().getAttributeInstance(SharedMonsterAttributes.maxHealth).setBaseValue((double) (600.0F));
        super.setHealth(600.0F);
    }

    public void setHome(ChunkCoordinates home) {
        this.home = home;
    }

    public ChunkCoordinates getHome() {
        return home;
    }

    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(600.0D);
    }

    @Override
    public void onEntityUpdate() {
        super.onEntityUpdate();
        if (worldObj.isRemote)
            return;
        if (ticksExisted % 3600 == 0 && worldObj.rand.nextDouble() <= .2D) {
            IInventory inv = getNearestInventory();
            if (inv == null)
                return;
            MerchantRecipeList rec = getRecipes(null);
            MerchantRecipe rand = (MerchantRecipe) rec.get(worldObj.rand.nextInt(rec.size()));
            EllyPutItemsEvent.Pre event = new EllyPutItemsEvent.Pre(this, (TileEntity) inv, rand.getItemToBuy());
            MinecraftForge.EVENT_BUS.post(event);
            if (event.isCanceled() || event.getItemPut() == null)
                return;
            ItemStack toPut = event.getItemPut().copy();
            putItemInInv(inv, toPut);
            EllyPutItemsEvent.Post event1 = new EllyPutItemsEvent.Post(this, (TileEntity) inv, rand.getItemToBuy());
            MinecraftForge.EVENT_BUS.post(event1);
        }
    }

    @Override
    protected boolean interact(EntityPlayer player) {
        ItemStack itemstack = player.inventory.getCurrentItem();
        boolean flag = itemstack != null && itemstack.getItem() == Items.spawn_egg;

        if (!flag && this.isEntityAlive() && getCustomer() == null && !this.isChild() && !player.isSneaking()) {
            if (!this.worldObj.isRemote)
            {
                this.setCustomer(player);
                player.displayGUIMerchant(this, "Elly");
            }

            return true;
        } else {
            return super.interact(player);
        }
    }

    private void putItemInInv(IInventory inventory, ItemStack stack) {
        for (int i = 0; i < inventory.getSizeInventory(); ++i) {
            ItemStack a = inventory.getStackInSlot(i);
            if (a == null) {
                inventory.setInventorySlotContents(i, stack);
                return;
            } else if (a.getItem() == stack.getItem() && a.getItemDamage() == stack.getItemDamage() && ItemStack.areItemStackTagsEqual(a, stack)) {
                if (stack.stackSize + a.stackSize <= stack.getMaxStackSize()) {
                    a.stackSize += stack.stackSize;
                    inventory.setInventorySlotContents(i, a);
                    return;
                } else {
                    int maxCan = Math.min((a.getMaxStackSize() - a.stackSize), stack.stackSize);
                    if (maxCan > 0) {
                        stack.stackSize -= maxCan;
                        a.stackSize += maxCan;
                        inventory.setInventorySlotContents(i, a);
                    }
                }
            }
        }
    }

    private IInventory getNearestInventory() {
        HashMap<Double, IInventory> map = new HashMap<>();
        for (int x = -5; x <= 5; ++x) {
            for (int y = -5; y <= 5; ++y) {
                for (int z = -5; z <= 5; ++z) {
                    TileEntity te = worldObj.getTileEntity(homeX() + x, homeY() + y, homeZ() + z);
                    if (te instanceof IInventory && !(te instanceof ISidedInventory))
                        map.put(distanceSqToIInventory(te), (IInventory) te);
                }
            }
        }
        double nearest = map.keySet().stream().min(Comparator.comparing(Double::floatValue)).orElse(0D);
        if (nearest == 0D)
            return null;
        return map.get(nearest);
    }

    private double distanceSqToIInventory(TileEntity te) {
        return ((homeX() - te.xCoord) * 2) + ((homeY() - te.yCoord) * 2) + ((homeZ() - te.zCoord) * 2);
    }

    private int homeX() {
        return getHome() == null ? 0 : getHome().posX;
    }

    private int homeY() {
        return getHome() == null ? 5 : getHome().posY;
    }

    private int homeZ() {
        return getHome() == null ? 0 : getHome().posZ;
    }

    public EntityElly getInstance() {
        return this;
    }

    @Override
    public void writeToNBT(NBTTagCompound n) {
        super.writeToNBT(n);
        if (summoner != null)
            n.setString("summoner", summoner.toString());
        if (home != null) {
            NBTTagCompound b = new NBTTagCompound();
            b.setInteger("x", homeX());
            b.setInteger("y", homeY());
            b.setInteger("z", homeZ());
            n.setTag("home", b);
        }
    }

    @Override
    public void readFromNBT(NBTTagCompound n) {
        super.readFromNBT(n);
        if (n.hasKey("summoner"))
            summoner = UUID.fromString(n.getString("summoner"));
        if (n.hasKey("home")) {
            NBTTagCompound b = n.getCompoundTag("home");
            if (b.hasKey("x") && b.hasKey("y") && b.hasKey("z")) {
                setHome(
                        new ChunkCoordinates(
                                b.getInteger("x"),
                                b.getInteger("y"),
                                b.getInteger("z")
                        )
                );
            }
        }
    }

    @Override
    public void setCustomer(EntityPlayer player) {
        this.buyingPlayer = player;
    }

    @Override
    public EntityPlayer getCustomer() {
        return this.buyingPlayer;
    }

    @Override
    public MerchantRecipeList getRecipes(@Nullable EntityPlayer player) {
        if (player == null || player.getUniqueID().equals(summoner)) {
            if (this.buyingList == null) {
                addDefaultEquipmentAndRecipies(this.rand.nextInt(4) + 6);
            }
            return this.buyingList;
        } else
            return new MerchantRecipeList();
    }

    @Override
    public void setRecipes(MerchantRecipeList lis) {

    }

    @Override
    public void useRecipe(MerchantRecipe recipe) {

    }

    @Override
    public void func_110297_a_(ItemStack stack) {

    }

    private void addDefaultEquipmentAndRecipies(int par1) {
        MerchantRecipeList merchantrecipelist = new MerchantRecipeList();

        int j1;
        for(int i = 0; i < par1; ++i) {
            Enchantment enchantment = Enchantment.enchantmentsBookList[this.rand.nextInt(Enchantment.enchantmentsBookList.length)];
            j1 = MathHelper.getRandomIntegerInRange(this.rand, enchantment.getMinLevel(), enchantment.getMaxLevel());
            ItemStack itemstack = Items.enchanted_book.getEnchantedItemStack(new EnchantmentData(enchantment, j1));
            int j = 2 + this.rand.nextInt(5 + j1 * 10) + 3 * j1;
            MerchantRecipe recipe = new MerchantRecipe(this.getPrice(j), itemstack);
            recipe.func_82783_a(-5);
            merchantrecipelist.add(recipe);
        }

        MerchantRecipe recipe;
        if (this.rand.nextDouble() < 0.25) {
            recipe = new MerchantRecipe(this.getPrice(this.rand.nextInt(3) + 8), Witchery.Items.GENERIC.itemSpectralDust.createStack(this.rand.nextInt(4) + 3));
            recipe.func_82783_a(-5);
            merchantrecipelist.add(recipe);
        }

        if (this.rand.nextDouble() < 0.25) {
            recipe = new MerchantRecipe(this.getPrice(this.rand.nextInt(3) + 8), Witchery.Items.GENERIC.itemDogTongue.createStack(this.rand.nextInt(4) + 4));
            recipe.func_82783_a(-5);
            merchantrecipelist.add(recipe);
        }

        if (this.rand.nextDouble() < 0.15) {
            recipe = new MerchantRecipe(this.getPrice(this.rand.nextInt(10) + 20), Witchery.Items.GENERIC.itemRedstoneSoup.createStack(1));
            recipe.func_82783_a(-5);
            merchantrecipelist.add(recipe);
        }

        if (this.rand.nextDouble() < 0.15) {
            recipe = new MerchantRecipe(new ItemStack(Items.diamond), new ItemStack(Items.ghast_tear, 2));
            recipe.func_82783_a(-5);
            merchantrecipelist.add(recipe);
        }

        if (this.rand.nextDouble() < 0.15) {
            recipe = new MerchantRecipe(new ItemStack(Items.diamond), new ItemStack(Items.ender_pearl, 2));
            recipe.func_82783_a(-5);
            merchantrecipelist.add(recipe);
        }

        Collections.shuffle(merchantrecipelist);
        Item currencyForHeart = this.getCurrency();
        MerchantRecipe heartRecipe = new MerchantRecipe(new ItemStack(currencyForHeart, currencyForHeart == Items.gold_ingot ? 30 : 3), Witchery.Items.GENERIC.itemDemonHeart.createStack(1));
        heartRecipe.func_82783_a(-5);
        merchantrecipelist.add(this.rand.nextInt(3), heartRecipe);
        if (this.buyingList == null) {
            this.buyingList = new MerchantRecipeList();
        }

        for(j1 = 0; j1 < par1 && j1 < merchantrecipelist.size(); ++j1) {
            this.buyingList.add(merchantrecipelist.get(j1));
        }


        for (IRecipe r : RecipeManager.getListRecipeElle()) {
            int position = worldObj.rand.nextInt(this.buyingList.size());
            this.buyingList.add(position, ((RecipeTradeElle)r).recipe);
        }
    }

    private ItemStack getPrice(int basePriceInEmeralds) {
        Item currency = this.getCurrency();
        int multiplier = currency == Items.gold_ingot ? 1 : (currency == Items.emerald ? 3 : (currency == Items.diamond ? 5 : 4));
        int quantity = Math.max(1, basePriceInEmeralds / multiplier);
        return new ItemStack(currency, quantity);
    }

    private Item getCurrency() {
        double chance = this.rand.nextDouble();
        if (chance < 0.2) {
            return Items.blaze_rod;
        } else if (chance < 0.4) {
            return Items.magma_cream;
        } else if (chance < 0.5) {
            return Items.diamond;
        } else {
            return chance < 0.75 ? Items.emerald : Items.gold_ingot;
        }
    }
}
