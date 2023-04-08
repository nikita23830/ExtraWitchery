package com.nikita23830.ewitchery.common.items;

import com.nikita23830.ewitchery.AdvanceWitchery;
import com.nikita23830.ewitchery.client.gui.GuiReadOlderBook;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

import java.util.List;

public class ItemOlderBook extends ModItem {
    IIcon[] icons = new IIcon[7];

    public ItemOlderBook() {
        super("orderBook");
        hasSubtypes = true;
        setMaxDamage(0);
        setMaxStackSize(1);
    }

    @Override
    public ItemStack onItemRightClick(ItemStack s, World w, EntityPlayer p) {
        if (!w.isRemote)
            return s;
        Minecraft.getMinecraft().displayGuiScreen(new GuiReadOlderBook(s));
        return s;
    }

    @Override
    public void registerIcons(IIconRegister ir) {
        for (int i = 0; i < 7; ++i) {
            icons[i] = ir.registerIcon(AdvanceWitchery.MODID.toLowerCase() + ":scroll" + i);
        }
    }

    @Override
    public IIcon getIconFromDamage(int m) {
        int index = m < 7 ? m : 0;
        return icons[index];
    }

    @Override
    public void getSubItems(Item p_150895_1_, CreativeTabs p_150895_2_, List l) {
        for (int i = 0; i < 7; ++i) {
            l.add(new ItemStack(this, 1, i));
        }
    }
}
