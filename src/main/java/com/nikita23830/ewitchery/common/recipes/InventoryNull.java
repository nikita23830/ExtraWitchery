package com.nikita23830.ewitchery.common.recipes;

import net.minecraft.inventory.Container;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;

public class InventoryNull extends InventoryCrafting {
    public final RecipeSize size;

    public InventoryNull(RecipeSize size) {
        super(new ContainerNull(), size.size, 1);
        this.size = size;
    }

    public InventoryNull(int size) {
        super(new ContainerNull(), size, 1);
        this.size = null;
    }

    public void setContents(ItemStack[] contents) {
        if (contents == null)
            return;
        if (contents.length != this.getSizeInventory())
            return;
        for (int i = 0; i < contents.length; ++i) {
            setInventorySlotContents(i, contents[i]);
        }
    }

    public boolean isEquals(RecipeBlackRitual rbr) {
        if (rbr.getInput().length != getSizeInventory())
            return false;
        ItemStack[] clone = new ItemStack[rbr.getInput().length];
        for (int i = 0; i < rbr.getInput().length; ++i) {
            if (rbr.getInput()[i] == null)
                continue;
            clone[i] = rbr.getInput()[i].copy();
        }
        for (ItemStack a : clone) {
            if (a == null)
                continue;
            if (!contains(a))
                return false;
        }
        return getNonNullItems(clone) == getNonNullItems(getContents());
    }

    private int getNonNullItems(ItemStack[] arr) {
        int i = 0;
        for (ItemStack a : arr) {
            if (a != null)
                ++i;
        }
        return i;
    }

    private boolean contains(ItemStack stack) {
        for (int i = 0; i < getSizeInventory(); ++i) {
            ItemStack a = getStackInSlot(i);
            if (a == null)
                continue;
            if (a.getItem() == stack.getItem() && a.getItemDamage() == stack.getItemDamage() && a.stackSize >= stack.stackSize && ItemStack.areItemStackTagsEqual(a, stack))
                return true;
        }
        return false;
    }

    public ItemStack[] getContents() {
        ItemStack[] a = new ItemStack[getSizeInventory()];
        for (int i = 0; i < getSizeInventory(); ++i) {
            a[i] = getStackInSlot(i);
        }
        return a;
    }

    public ItemStack[] getContentsClone() {
        ItemStack[] a = new ItemStack[getSizeInventory()];
        for (int i = 0; i < getSizeInventory(); ++i) {
            if (getStackInSlot(i) == null)
                continue;
            a[i] = getStackInSlot(i).copy();
        }
        return a;
    }
}
