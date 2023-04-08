package com.nikita23830.ewitchery.common.recipes;

import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

public class RecipeArmorSplit implements IRecipe {
    public int type;

    public RecipeArmorSplit(int type) {
        this.type = type;
    }

    @Override
    public boolean matches(InventoryCrafting i, World world) {
        return i != null && InventoryCraftingUtil.hasModArmor(i, this.type) && InventoryCraftingUtil.hasArmor(i, this.type) && InventoryCraftingUtil.correctCountItems(i);
    }

    public static ItemStack getCraftingResult(ItemStack stack) {
        ItemStack pre = stack.copy();
        NBTTagCompound n = pre.hasTagCompound() ? pre.getTagCompound() : new NBTTagCompound();
        n.setBoolean("selfSunAdvWitchery", true);
        pre.setTagCompound(n);
        return pre;
    }

    @Override
    public ItemStack getCraftingResult(InventoryCrafting inventory) {
        if (!matches(inventory, null))
            return null;
        ItemStack pre = InventoryCraftingUtil.getArmor(inventory, this.type);
        if (pre == null)
            return null;
        pre = pre.copy();
        NBTTagCompound n = pre.hasTagCompound() ? pre.getTagCompound() : new NBTTagCompound();
        n.setBoolean("selfSunAdvWitchery", true);
        pre.setTagCompound(n);
        //InventoryCraftingUtil.clear(inventory);
        return pre;
    }

    @Override
    public int getRecipeSize() {
        return 0;
    }

    @Override
    public ItemStack getRecipeOutput() {
        return null;
    }
}
