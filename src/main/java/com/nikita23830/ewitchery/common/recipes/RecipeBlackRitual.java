package com.nikita23830.ewitchery.common.recipes;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.world.World;

import java.util.HashMap;

public class RecipeBlackRitual implements IRecipe {
    private final ItemStack out;
    private final ItemStack[] in;

    private String entity = null;
    private int needWorld = Integer.MAX_VALUE;
    private final String nameRitual;
    private boolean closed = false;

    public RecipeBlackRitual(String name, ItemStack output, ItemStack[] input) {
        this.out = output.copy();
        this.in = new ItemStack[input.length <= RecipeSize.MINI.size ? RecipeSize.MINI.size : RecipeSize.NORMAL.size];
        for (int i = 0; i < input.length; ++i) {
            if (input[i] == null)
                continue;
            in[i] = input[i].copy();
        }
        this.nameRitual = name;
    }

    public RecipeBlackRitual(String name, ItemStack output, ItemStack[] input, String entity, int needWorld) {
        this(name, output, input);
        this.entity = entity;
        this.needWorld = needWorld;
    }

    public RecipeBlackRitual(String name, ItemStack output, ItemStack[] input, String entity) {
        this(name, output, input);
        this.entity = entity;
    }

    public RecipeBlackRitual(String name, ItemStack output, ItemStack[] input, int needWorld) {
        this(name, output, input);
        this.entity = null;
        this.needWorld = needWorld;
    }

    public RecipeBlackRitual setClosed() {
        closed = true;
        return this;
    }

    public boolean isClosed() {
        return closed;
    }

    public String getNameRitual() {
        return nameRitual;
    }

    public boolean isTrueWorld(World world) {
        return needWorld == Integer.MAX_VALUE || world.provider.dimensionId == needWorld;
    }

    public boolean hasEntity() {
        return entity != null;
    }

    public boolean isTrueEntity(Entity entity) {
        return entity.getClass().getSimpleName().equalsIgnoreCase(this.entity);
    }

    public ItemStack[] getInput() {
        return in;
    }

    @Override
    public boolean matches(InventoryCrafting inv, World w) {
        if (!(inv instanceof InventoryNull))
            return false;
        return ((InventoryNull) inv).isEquals(this);
    }

    @Override
    public ItemStack getCraftingResult(InventoryCrafting inv) {
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

    public RecipeSize size() {
        for (RecipeSize rs : RecipeSize.values()) {
            if (rs.size == in.length)
                return rs;
        }
        return null;
    }
}
