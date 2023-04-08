package com.nikita23830.ewitchery.common.tiles;

import com.emoniph.witchery.Witchery;
import com.emoniph.witchery.entity.EntityCovenWitch;
import com.google.common.collect.BiMap;
import com.mojang.authlib.GameProfile;
import com.nikita23830.ewitchery.common.CommonProxy;
import com.nikita23830.ewitchery.common.ModBlocks;
import com.nikita23830.ewitchery.common.entity.EntityBehemoth;
import com.nikita23830.ewitchery.common.entity.EntityBeholder;
import com.nikita23830.ewitchery.common.entity.EntityDarkElf;
import com.nikita23830.ewitchery.common.entity.EntityGuardian;
import com.nikita23830.ewitchery.common.recipes.InventoryNull;
import com.nikita23830.ewitchery.common.recipes.RecipeBlackRitual;
import com.nikita23830.ewitchery.common.recipes.RecipeManager;
import com.nikita23830.ewitchery.common.recipes.RecipeSize;
import cpw.mods.fml.common.registry.EntityRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.ReflectionHelper;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.event.HoverEvent;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.ChatStyle;
import net.minecraft.world.WorldServer;
import net.minecraft.world.WorldSettings;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.event.world.BlockEvent;

import java.lang.reflect.Constructor;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class TileBloodCircle extends TileEntity {
    private boolean start = false;
    private int cd = 0;
    private int stage = 0;
    private RecipeBlackRitual recipe = null;
    private FakePlayer started;
    private RecipeSize size;
    private ItemStack[] pre = null;
    private ItemStack[] post = null;
    private GameProfile toFake = null;

    @Override
    public void writeToNBT(NBTTagCompound n) {
        super.writeToNBT(n);
        n.setBoolean("start", start);
        if (recipe != null)
            n.setTag("recipe", recipe.getRecipeOutput().writeToNBT(new NBTTagCompound()));
        if (started != null) {
            NBTTagCompound nb = new NBTTagCompound();
            nb.setString("name", started.getGameProfile().getName());
            nb.setString("uuid", started.getGameProfile().getId().toString());
            n.setTag("owner", nb);
        }
        if (pre != null) {
            NBTTagList l = new NBTTagList();
            byte slot = -1;
            for (ItemStack a : pre) {
                ++slot;
                if (a == null)
                    continue;
                NBTTagCompound nb = a.writeToNBT(new NBTTagCompound());
                nb.setByte("slot", (byte) slot);
                l.appendTag(nb);
            }
            n.setTag("pre", l);
            n.setInteger("preCount", pre.length);
        }
        if (post != null) {
            NBTTagList l = new NBTTagList();
            byte slot = -1;
            for (ItemStack a : post) {
                ++slot;
                if (a == null)
                    continue;
                NBTTagCompound nb = a.writeToNBT(new NBTTagCompound());
                nb.setByte("slot", (byte) slot);
                l.appendTag(nb);
            }
            n.setTag("post", l);
            n.setInteger("postCount", post.length);
        }
    }

    @Override
    public void readFromNBT(NBTTagCompound n) {
        super.readFromNBT(n);
        start = n.getBoolean("start");
        if (n.hasKey("recipe")) {
            this.recipe = RecipeManager.findRecipe(ItemStack.loadItemStackFromNBT(n.getCompoundTag("recipe")));
        }
        if (n.hasKey("owner")) {
            NBTTagCompound nb = n.getCompoundTag("owner");
            this.toFake = new GameProfile(UUID.fromString(nb.getString("uuid")), nb.getString("name"));
        }
        if (n.hasKey("pre")) {
            NBTTagList l = n.getTagList("pre", 10);
            this.pre = new ItemStack[n.getInteger("preCount")];
            for (int i = 0; i < l.tagCount(); ++i) {
                NBTTagCompound nb = l.getCompoundTagAt(i);
                this.pre[nb.getByte("slot")] = ItemStack.loadItemStackFromNBT(nb);
            }
        }
        if (n.hasKey("post")) {
            NBTTagList l = n.getTagList("post", 10);
            this.post = new ItemStack[n.getInteger("postCount")];
            for (int i = 0; i < l.tagCount(); ++i) {
                NBTTagCompound nb = l.getCompoundTagAt(i);
                this.post[nb.getByte("slot")] = ItemStack.loadItemStackFromNBT(nb);
            }
        }
    }

    public void onClick(EntityPlayer player) {
        if (this.start && this.recipe != null) {
            return;
        }
        this.started = new FakePlayer((WorldServer) worldObj, player.getGameProfile());
        InventoryNull in = getInv();
        if (in == null)
            return;
        this.recipe = RecipeManager.findRecipe(in, worldObj);
        if (this.recipe != null) {
            this.size = in.size;
            if (this.recipe.hasEntity()) {
                List<EntityLivingBase> l = worldObj.getEntitiesWithinAABB(
                        EntityLivingBase.class,
                        AxisAlignedBB.getBoundingBox(
                                (xCoord + this.size.minMaxAxis[0]), (yCoord - 1), (zCoord + this.size.minMaxAxis[1]),
                                (xCoord + this.size.minMaxAxis[2]), (yCoord + 3), (zCoord + this.size.minMaxAxis[3])
                        ));
                if (l == null || l.size() == 0) {
                    this.size = null;
                    this.recipe = null;
                    return;
                }
                EntityLivingBase elb = l.stream().filter(e -> !(e instanceof EntityPlayer)).filter(e -> this.recipe.isTrueEntity(e)).findFirst().orElse(null);
                if (elb == null) {
                    this.size = null;
                    this.recipe = null;
                    return;
                }
                elb.setDead();
            }
            if (this.recipe.getNameRitual().equals("recipeSpawnVampireMaster")) {
                long dayTime = worldObj.getWorldTime() % 24000;
                if (dayTime <= 15000 || dayTime >= 20000) {
                    player.addChatComponentMessage(new ChatComponentText("§cДанный риутал возможен только ночью"));
                    this.size = null;
                    this.recipe = null;
                    return;
                }
                ItemStack current = player.inventory.getCurrentItem();
                if (current == null || !Witchery.Items.GENERIC.itemSeerStone.isMatch(current)) {
                    ChatComponentText tx = new ChatComponentText("§cИспользуйте: ");
                    ChatComponentTranslation translation = new ChatComponentTranslation(Witchery.Items.GENERIC.itemSeerStone.createStack().getUnlocalizedName());
                    ChatStyle style = translation.getChatStyle();
                    style.setChatHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_ITEM, new ChatComponentText(Witchery.Items.GENERIC.itemSeerStone.createStack().writeToNBT(new NBTTagCompound()).toString())));
                    translation.setChatStyle(style);
                    tx.appendSibling(translation);
                    player.addChatComponentMessage(tx);
                    this.size = null;
                    this.recipe = null;
                    return;
                }
                if (EntityCovenWitch.getCovenSize(player) < 6) {
                    player.addChatComponentMessage(new ChatComponentText("§cВнимание! Шабаш не полный, провести ритуал невозможно"));
                    this.size = null;
                    this.recipe = null;
                    return;
                }
            }
            reset();
            this.start = true;
            this.pre = in.getContentsClone();
            this.post = new ItemStack[pre.length];
        }
    }

    @Override
    public void updateEntity() {
        super.updateEntity();
        if (worldObj.isRemote)
            return;
        if (toFake != null) {
            this.started = new FakePlayer((WorldServer) worldObj, toFake);
            toFake = null;
        }
        if (start && recipe == null) {
            reset();
            return;
        }
        if (!start)
            return;
        if (size == null)
            size = recipe.size();
        if (size == null) {
            recipe = null;
            reset();
        }
        if (cd > 0) {
            --cd;
            return;
        }
        cd = 20;
        if (stage >= size.offset.length) {
            finish();
            reset();
            recipe = null;
            size = null;
            return;
        }
        TileEntity te = worldObj.getTileEntity((xCoord + size.offset[stage][0]), yCoord, (zCoord + size.offset[stage][1]));
        if (te instanceof TileCirle) {
            post[stage] = ((TileCirle) te).getStackInSlot(0);
        }
        worldObj.setBlock((xCoord + size.offset[stage][0]), yCoord, (zCoord + size.offset[stage][1]), size == RecipeSize.MINI ? ModBlocks.bloodFire : ModBlocks.blackFire);
        ++stage;
    }

    private void finish() {
        for (int[] c : size.offset) {
            worldObj.setBlockToAir((xCoord + c[0]), yCoord, (zCoord + c[1]));
        }
        boolean incorrect = false;
        for (int i = 0; i < pre.length; ++i) {
            if (ItemStack.areItemStacksEqual(pre[i], post[i]) && ItemStack.areItemStackTagsEqual(pre[i], post[i])) {
                continue;
            } else {
                incorrect = true;
                break;
            }
        }
        if (incorrect) {
            for (ItemStack a : post) {
                if (a == null)
                    continue;
                EntityItem ei = new EntityItem(worldObj, (xCoord), (yCoord + 0.7D), (zCoord), a.copy());
                worldObj.spawnEntityInWorld(ei);
            }
            pre = null;
            post = null;
            return;
        }

        if (this.recipe.getNameRitual().equals("recipeSpawnGuardian")) {
            EntityLivingBase toSpawn = null;
            if (worldObj.rand.nextDouble() >= 0.5D)
                toSpawn = new EntityBeholder(worldObj);
            else
                toSpawn = new EntityGuardian(worldObj);
            toSpawn.setPositionAndUpdate((xCoord + 0.5D), (yCoord + 0.5D), (zCoord + 0.5D));
            worldObj.spawnEntityInWorld(toSpawn);
            return;
        } else if (this.recipe.getNameRitual().equals("recipeSpawnVampireMaster")) {
            EntityBehemoth eb = new EntityBehemoth(worldObj, this.started.getUniqueID());
            eb.setPositionAndUpdate((xCoord + 0.5D), (yCoord + 0.5D), (zCoord + 0.5D));
            worldObj.spawnEntityInWorld(eb);
            return;
        }
        EntityItem ei = new EntityItem(worldObj, (xCoord), (yCoord + 0.7D), (zCoord), recipe.getRecipeOutput());
        worldObj.spawnEntityInWorld(ei);
    }

    private void reset() {
        this.start = false;
        this.stage = 0;
        this.cd = 0;
    }

    private InventoryNull getInv() {
        RecipeSize rs = checkSize();
        if (rs == null)
            return null;
        InventoryNull in = new InventoryNull(rs);
        for (int i = 0; i < rs.offset.length; ++i) {
            in.setInventorySlotContents(i, getStack(rs.offset[i][0],rs.offset[i][1]));
        }
        return in;
    }

    private ItemStack getStack(int ox, int oz) {
        TileEntity te = worldObj.getTileEntity(xCoord + ox, yCoord, zCoord + oz);
        if (te instanceof TileCirle)
            return ((TileCirle) te).getStackInSlot(0);
        return null;
    }

    private RecipeSize checkSize() {
        for (RecipeSize rs : RecipeSize.values()) {
            boolean find = true;
            for (int[] c : rs.offset) {
                if (!isTrueBlock(c[0], c[1])) {
                    find = false;
                    break;
                }
            }
            if (find)
                return rs;
        }
        return null;
    }

    private boolean isTrueBlock(int offsetX, int offsetZ) {
        Block b = worldObj.getBlock(xCoord + offsetX, yCoord, zCoord + offsetZ);
        if (b == ModBlocks.blockCircle) {
            if (started == null)
                return false;
            BlockEvent.BreakEvent ev = ForgeHooks.onBlockBreakEvent(worldObj, WorldSettings.GameType.SURVIVAL, started, xCoord + offsetX, yCoord, zCoord + offsetZ);
            return !ev.isCanceled();
        }
        return false;
    }
}
