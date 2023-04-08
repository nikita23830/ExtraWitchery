package com.nikita23830.ewitchery.common.items;

import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import java.util.List;

public abstract class ModItem extends Item {

    public ModItem(String name) {
        GameRegistry.registerItem(this, getClass().getSimpleName());
        setUnlocalizedName(name);
    }

    @Override
    public void getSubItems(Item item, CreativeTabs tab, List list) {
        super.getSubItems(item, tab, list);
    }

    public ItemStack create() {
        return new ItemStack(this);
    }

    public ItemStack create(int damage) {
        return new ItemStack(this, 1, damage);
    }

    public ItemStack create(int count, int damage) {
        return new ItemStack(this, count, damage);
    }
}
