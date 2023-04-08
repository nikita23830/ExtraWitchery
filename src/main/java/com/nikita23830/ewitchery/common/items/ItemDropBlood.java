package com.nikita23830.ewitchery.common.items;

import com.nikita23830.ewitchery.AdvanceWitchery;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

import java.util.List;

public class ItemDropBlood extends ModItem{
    IIcon[] icons = new IIcon[3];

    public ItemDropBlood() {
        super("dropBlood");
        hasSubtypes = true;
        setMaxDamage(0);
        setMaxStackSize(8);
    }

    @Override
    public void getSubItems(Item item, CreativeTabs tab, List list) {
        for (int i = 0; i < 3; ++i) {
            list.add(new ItemStack(this, 1, i));
        }
    }

    @Override
    public void registerIcons(IIconRegister ir) {
        for (int i = 0; i < 3; ++i) {
            icons[i] = ir.registerIcon(AdvanceWitchery.MODID.toLowerCase() + ":blood" + i);
        }
    }

    @Override
    public IIcon getIconFromDamage(int meta) {
        return meta < 3 ? icons[meta] : icons[0];
    }
}
