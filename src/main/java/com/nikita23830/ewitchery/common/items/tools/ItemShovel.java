package com.nikita23830.ewitchery.common.items.tools;

import com.emoniph.witchery.common.ExtendedPlayer;
import com.nikita23830.ewitchery.AdvanceWitchery;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.BlockDirt;
import net.minecraft.block.IGrowable;
import net.minecraft.block.material.Material;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemSpade;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.ChatComponentText;
import net.minecraft.world.World;
import thaumcraft.common.items.equipment.ItemElementalShovel;

import static com.nikita23830.ewitchery.common.items.tools.ItemPick.material;

public class ItemShovel extends ItemSpade {
    private String[] types = new String[]{"1x1", "3x3", "5x5", "7x7", "9x9", "11x11", "13x13", "15x15"};

    public ItemShovel() {
        super(material);
        this.setHarvestLevel("shovel", 50);
        GameRegistry.registerItem(this, "awitchery.shovel");
        setUnlocalizedName(AdvanceWitchery.modid() + ".shovel");
        setTextureName(AdvanceWitchery.modid() + ":shovel");
    }

    private boolean isSilk(ItemStack stack) {
        return CoreTool.hasToolEffectFortune(stack) == 0;
    }

    private void setLucky(ItemStack stack) {
        NBTTagCompound n = stack.hasTagCompound() ? stack.getTagCompound() : new NBTTagCompound();
        NBTTagList ench = stack.getEnchantmentTagList();
        if (ench == null) {
            stack.addEnchantment(Enchantment.fortune, 10);
            return;
        }
        NBTTagList newEnch = new NBTTagList();
        for (byte i = 0; i < Math.min(ench.tagCount(), 127); ++i) {
            NBTTagCompound en = ench.getCompoundTagAt(i);
            if (en.hasKey("id") && en.getShort("id") == Enchantment.silkTouch.effectId && en.hasKey("lvl"))
                continue;
            else
                newEnch.appendTag(en);
        }
        n.setTag("ench", newEnch);
        stack.setTagCompound(n);
        stack.addEnchantment(Enchantment.fortune, 10);
    }

    private void setSilk(ItemStack stack) {
        NBTTagCompound n = stack.hasTagCompound() ? stack.getTagCompound() : new NBTTagCompound();
        NBTTagList ench = stack.getEnchantmentTagList();
        if (ench == null) {
            stack.addEnchantment(Enchantment.silkTouch, 1);
            return;
        }
        NBTTagList newEnch = new NBTTagList();
        for (byte i = 0; i < Math.min(ench.tagCount(), 127); ++i) {
            NBTTagCompound en = ench.getCompoundTagAt(i);
            if (en.hasKey("id") && en.getShort("id") == Enchantment.fortune.effectId && en.hasKey("lvl"))
                continue;
            else
                newEnch.appendTag(en);
        }
        n.setTag("ench", newEnch);
        stack.setTagCompound(n);
        stack.addEnchantment(Enchantment.silkTouch, 1);
    }

    private int getTypePick(ItemStack stack) {
        NBTTagCompound n = stack.hasTagCompound() ? stack.getTagCompound() : new NBTTagCompound();
        return n.hasKey("pick") ? n.getInteger("pick") : 0;
    }

    private void setTypePick(ItemStack stack, int type) {
        NBTTagCompound n = stack.hasTagCompound() ? stack.getTagCompound() : new NBTTagCompound();
        n.setInteger("pick", type);
        stack.setTagCompound(n);
    }

    @Override
    public boolean onItemUseFirst(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ) {
        if (world.isRemote)
            return super.onItemUseFirst(stack, player, world, x, y, z, side, hitX, hitY, hitZ);
        Block b = world.getBlock(x, y, z);
        if (b instanceof BlockDirt) {
            int type = getTypePick(stack);
            ExtendedPlayer ep = ExtendedPlayer.get(player);
            for (int xc = -type; xc <= type; ++xc) {
                for (int zc = -type; zc <= type; ++zc) {
                    if (world.isAirBlock((x + xc), y, (z + zc)))
                        continue;
                    Block bl = world.getBlock((x + xc), y, (z + zc));
                    if (ep.getBloodPower() <= 20)
                        return true;
                    if (bl.getMaterial() == Material.ground && !CoreTool.cantBreak(player, (x + xc), y, (z + zc))) {
                        world.setBlock((x + xc), y, (z + zc), Blocks.grass);
                        ep.setBloodPower(ep.getBloodPower() - 20);
                    }
                }
            }
            return true;
        }
        return super.onItemUseFirst(stack, player, world, x, y, z, side, hitX, hitY, hitZ);
    }

    @Override
    public ItemStack onItemRightClick(ItemStack stack, World w, EntityPlayer ep) {
        if (w.isRemote)
            return stack;
        if (ep.isSneaking()) {
            if (isSilk(stack)) {
                setLucky(stack);
                ep.addChatComponentMessage(new ChatComponentText("§eРежим лопаты изменен на удачу"));
            } else {
                setSilk(stack);
                ep.addChatComponentMessage(new ChatComponentText("§eРежим лопаты изменен на шелковое косание"));
            }
        } else {
            int current = getTypePick(stack);
            if (current < 7)
                setTypePick(stack, (current + 1));
            else
                setTypePick(stack, 0);
            ep.addChatComponentMessage(new ChatComponentText("§eОбласть вскопки изменена на " + (types[getTypePick(stack)])));
        }
        return stack;
    }

    @Override
    public boolean onBlockStartBreak(ItemStack itemstack, int X, int Y, int Z, EntityPlayer player) {
        if (player.worldObj.isRemote)
            return super.onBlockStartBreak(itemstack, X, Y, Z, player);
        int type = getTypePick(itemstack);
        if (type == 0)
            return super.onBlockStartBreak(itemstack, X, Y, Z, player);
        if (CoreTool.checkMaterial(player.worldObj.getBlock(X,Y,Z),1))
            CoreTool.breakAOE(itemstack, X, Y, Z, player, type, 1, player);
        return super.onBlockStartBreak(itemstack, X, Y, Z, player);
    }
}
