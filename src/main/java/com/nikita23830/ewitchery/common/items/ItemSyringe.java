package com.nikita23830.ewitchery.common.items;

import com.nikita23830.ewitchery.AdvanceWitchery;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

import java.util.List;

public class ItemSyringe extends ModItem{
    IIcon[] icons = new IIcon[5];

    public ItemSyringe() {
        super("syringe");
        hasSubtypes = true;
        setMaxDamage(0);
        setMaxStackSize(1);
    }

    @Override
    public void getSubItems(Item p_150895_1_, CreativeTabs p_150895_2_, List l) {
        for (int i = 0; i < 5; ++i) {
            l.add(new ItemStack(this, 1, i));
        }
    }

    @Override
    public void registerIcons(IIconRegister ir) {
        icons[0] = ir.registerIcon(AdvanceWitchery.MODID.toLowerCase() + ":syringe_empty");
        icons[1] = ir.registerIcon(AdvanceWitchery.MODID.toLowerCase() + ":syringe_blood");
        icons[2] = ir.registerIcon(AdvanceWitchery.MODID.toLowerCase() + ":syringe_black");
        icons[3] = ir.registerIcon(AdvanceWitchery.MODID.toLowerCase() + ":syringe_purple");
        icons[4] = ir.registerIcon(AdvanceWitchery.MODID.toLowerCase() + ":syringe_green");
    }

    @Override
    public IIcon getIconFromDamage(int m) {
       return icons[m < 5 ? m : 0];
    }
}
