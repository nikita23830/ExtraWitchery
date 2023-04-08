package com.nikita23830.ewitchery.common.recipes;

import com.nikita23830.ewitchery.common.stats.AchievementMod;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.stats.Achievement;
import net.minecraft.stats.AchievementList;
import net.minecraft.village.MerchantRecipe;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import java.util.ArrayList;

public class RecipeTradeElle implements IRecipe {
    private final ItemStack in;
    private final ItemStack out;
    public final MerchantRecipe recipe;

    public RecipeTradeElle(ItemStack in, ItemStack out) {
        this.in = in;
        this.out = out;
        this.recipe = new MerchantRecipe(out, in);
    }

    @Override
    public boolean matches(InventoryCrafting inv, World w) {
        return false;
    }

    public boolean matches(ItemStack stack) {
        return in.getItem() == stack.getItem() && in.getItemDamage() == stack.getItemDamage() && in.stackSize <= stack.stackSize && ItemStack.areItemStackTagsEqual(in, stack);
    }

    public boolean matchesOutput(ItemStack stack) {
        return out.getItem() == stack.getItem() && out.getItemDamage() == stack.getItemDamage() && out.stackSize <= stack.stackSize && ItemStack.areItemStackTagsEqual(out, stack);
    }

    public int countIn() {
        return this.in.stackSize;
    }

    @Override
    public ItemStack getCraftingResult(InventoryCrafting p_77572_1_) {
        return out.copy();
    }

    @Override
    public int getRecipeSize() {
        return 0;
    }

    @Override
    public ItemStack getRecipeOutput() {
        return out.copy();
    }
}
