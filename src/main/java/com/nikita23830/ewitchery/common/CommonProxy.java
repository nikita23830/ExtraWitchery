package com.nikita23830.ewitchery.common;

import com.nikita23830.ewitchery.common.events.ModEvents;
import com.nikita23830.ewitchery.common.network.NetworkHandler;
import com.nikita23830.ewitchery.common.recipes.RecipeManager;
import com.nikita23830.ewitchery.common.stats.StatManager;
import cpw.mods.fml.common.event.FMLConstructionEvent;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

public class CommonProxy {

    public void cons(FMLConstructionEvent event) {
    }

    public void preInit(FMLPreInitializationEvent event) {

    }

    public void init(FMLInitializationEvent event) {
        ModBlocks.init();
        ModItems.init();
        ModEntity.init();
        ModEvents.init();
        StatManager.init();
        RecipeManager.init();
        NetworkHandler.init();
    }

    public void postInit(FMLPostInitializationEvent event) {
        RecipeManager.initArmor();
    }

    public static void increaseItem(ItemStack stack, EntityPlayer player) {
        for (ItemStack a : player.inventory.mainInventory) {
            if (a == null)
                continue;
            if (a.getItem() == stack.getItem() && a.getItemDamage() == stack.getItemDamage() && ItemStack.areItemStackTagsEqual(a, stack)) {
                --a.stackSize;
                break;
            }
        }
    }

    public static void addOrDropItem(ItemStack stack, EntityPlayer player) {
        if (!player.inventory.addItemStackToInventory(stack)) {
            EntityItem ei = new EntityItem(player.worldObj, player.posX, player.posY, player.posZ, stack);
            ei.delayBeforeCanPickup = 0;
            player.worldObj.spawnEntityInWorld(ei);
        }
    }

    public static boolean hasItem(ItemStack stack, EntityPlayer player) {
        for (ItemStack a : player.inventory.mainInventory) {
            if (a == null)
                continue;
            if (a.getItem() == stack.getItem() && a.getItemDamage() == stack.getItemDamage() && ItemStack.areItemStackTagsEqual(a, stack))
                return true;
        }
        return false;
    }

    public static boolean hasItem(ItemStack stack, EntityPlayer player, int count) {
        for (ItemStack a : player.inventory.mainInventory) {
            if (a == null)
                continue;
            if (a.getItem() == stack.getItem() && a.getItemDamage() == stack.getItemDamage() && a.stackSize >= count && ItemStack.areItemStackTagsEqual(a, stack))
                return true;
        }
        return false;
    }
}
