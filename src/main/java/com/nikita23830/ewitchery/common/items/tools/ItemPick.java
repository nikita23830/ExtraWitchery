package com.nikita23830.ewitchery.common.items.tools;

import com.nikita23830.ewitchery.AdvanceWitchery;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.ChatComponentText;
import net.minecraft.world.World;
import net.minecraftforge.common.util.EnumHelper;

public class ItemPick extends ItemPickaxe {
    public static Item.ToolMaterial material = EnumHelper.addToolMaterial("AWITCHERY", 50, -1, 9.0F, 36.0F, 45);
    private String[] types = new String[]{"1x1", "3x3", "5x5", "7x7", "9x9", "11x11", "13x13", "15x15"};

    public ItemPick() {
        super(material);
        this.setHarvestLevel("pickaxe", 50);
        GameRegistry.registerItem(this, "awitchery.pick");
        setUnlocalizedName(AdvanceWitchery.modid() + ".pick");
        setTextureName(AdvanceWitchery.modid() + ":pickaxe");
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
    public ItemStack onItemRightClick(ItemStack stack, World w, EntityPlayer ep) {
        if (w.isRemote)
            return stack;
        if (ep.isSneaking()) {
            if (isSilk(stack)) {
                setLucky(stack);
                ep.addChatComponentMessage(new ChatComponentText("§eРежим кирки изменен на удачу"));
            } else {
                setSilk(stack);
                ep.addChatComponentMessage(new ChatComponentText("§eРежим кирки изменен на шелковое косание"));
            }
        } else {
            int current = getTypePick(stack);
            if (current < 7)
                setTypePick(stack, (current + 1));
            else
                setTypePick(stack, 0);
            ep.addChatComponentMessage(new ChatComponentText("§eОбласть копания изменена на " + (types[getTypePick(stack)])));
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
        if (CoreTool.checkMaterial(player.worldObj.getBlock(X,Y,Z),2))
            CoreTool.breakAOE(itemstack, X, Y, Z, player, type, 2, player);
        return super.onBlockStartBreak(itemstack, X, Y, Z, player);
    }
}
