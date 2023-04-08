package com.nikita23830.ewitchery.common.items.armor;

import com.emoniph.witchery.item.ItemVampireClothes;
import com.meteor.extrabotany.common.item.relic.legendary.armor.killer.ItemKillerArmor;
import com.nikita23830.ewitchery.AdvanceWitchery;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraftforge.common.util.EnumHelper;

import java.awt.*;

public class ModArmor extends ItemVampireClothes {

    public ModArmor(int type, String name) {
        super(type, false, true);
        setUnlocalizedName(name);
    }

    public int getColor(ItemStack stack) {
        if (!this.hasColor(stack)) {
            return super.getColor(stack);
        } else {
            int color = super.getColor(stack);
            if (color == 10511680) {
                color = 13369344;
            }

            return color; //color
        }
    }

    public String getArmorTexture(ItemStack stack, Entity entity, int slot, String type) {
        if (stack != null && this.armorType == 2) {
            return AdvanceWitchery.MODID.toLowerCase() + ":textures/items/vampirearmor.png";
        } else if (stack != null) {
            return type == null ? AdvanceWitchery.MODID.toLowerCase() + ":textures/items/vampirearmor_over_first.png" : AdvanceWitchery.MODID.toLowerCase() + ":textures/items/vampirearmor_over.png";
        } else {
            return null;
        }
    }

    @Override
    public Item setUnlocalizedName(String name) {
        return super.setUnlocalizedName(name);
    }

    public static boolean isFullSet(EntityPlayer pl) {
        for (int i = 0; i < 4; ++i) {
            ItemStack a = pl.inventory.armorInventory[i];
            if (a == null)
                return false;
            if (!(a.getItem() instanceof ModArmor)) {
                if (a.hasTagCompound() && a.getTagCompound().hasKey("selfSunAdvWitchery")) {
                    continue;
                }
                return false;
            }
        }
        return true;
    }

    @Override
    public void damageArmor(EntityLivingBase entity, ItemStack stack, DamageSource source, int damage, int slot) {

    }
}
