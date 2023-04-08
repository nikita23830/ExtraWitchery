package com.nikita23830.ewitchery.common.recipes;

import com.nikita23830.ewitchery.common.items.armor.ModArmor;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;

public class InventoryCraftingUtil {

    public static boolean hasModArmor(InventoryCrafting inv, int type) {
        for (int i = 0; i < inv.getSizeInventory(); ++i) {
            ItemStack a = inv.getStackInSlot(i);
            if (a == null)
                continue;
            if (a.getItem() instanceof ModArmor && ((ModArmor)a.getItem()).armorType == type)
                return true;
        }
        return false;
    }

    public static boolean hasArmor(InventoryCrafting inv, int type) {
        for (int i = 0; i < inv.getSizeInventory(); ++i) {
            ItemStack a = inv.getStackInSlot(i);
            if (a == null)
                continue;
            if (a.getItem() instanceof ItemArmor && ((ItemArmor)a.getItem()).armorType == type)
                return true;
        }
        return false;
    }

    public static boolean correctCountItems(InventoryCrafting inv) {
        int b = 0;
        for (int i = 0; i < inv.getSizeInventory(); ++i) {
            ItemStack a = inv.getStackInSlot(i);
            if (a == null)
                continue;
            ++b;
        }
        return b == 2;
    }

    public static ItemStack getArmor(InventoryCrafting inv, int type) {
        for (int i = 0; i < inv.getSizeInventory(); ++i) {
            ItemStack a = inv.getStackInSlot(i);
            if (a == null)
                continue;
            if (a.getItem() instanceof ItemArmor && ((ItemArmor)a.getItem()).armorType == type)
                return a;
        }
        return null;
    }

    public static void clear(InventoryCrafting inv) {
        for (int i = 0; i < inv.getSizeInventory(); ++i) {
            inv.setInventorySlotContents(i, null);
        }
    }

    public static boolean hasStack(ItemStack[] stacks, ItemStack stack) {
        for (ItemStack itemStack : stacks) {
            if (itemStack == null)
                continue;
            if (itemStack.getItem() == stack.getItem() && itemStack.getItemDamage() == stack.getItemDamage())
                return true;
        }
        return false;
    }
}
