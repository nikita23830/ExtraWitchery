package com.nikita23830.ewitchery.common.items;

import com.nikita23830.ewitchery.AdvanceWitchery;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

import java.util.List;

public class ItemBreakArmor extends ModItem{
    IIcon[] icons = new IIcon[4];

    public ItemBreakArmor() {
        super("breakArmor");
        hasSubtypes = true;
        setMaxDamage(0);
        setMaxStackSize(1);
    }

    @Override
    public void registerIcons(IIconRegister ir) {
        for (int i = 0; i < icons.length; ++i) {
            icons[i] = ir.registerIcon(AdvanceWitchery.MODID.toLowerCase() + ":breakArmor" + i);
        }
    }

    @Override
    public IIcon getIconFromDamage(int m) {
        return m < 4 ? icons[m] : icons[0];
    }

    @Override
    public void getSubItems(Item item, CreativeTabs tab, List list) {
        for (int i = 0; i < 4; ++i) {
            list.add(new ItemStack(this, 1, i));
        }
    }
}
