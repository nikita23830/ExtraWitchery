package com.nikita23830.ewitchery.common.items;

import com.nikita23830.ewitchery.common.ModBlocks;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

import java.util.List;

public class ItemChalk extends ModItem {
    IIcon[] icons = new IIcon[2];

    public ItemChalk() {
        super("chalk");
        hasSubtypes = true;
        setMaxDamage(100);
        setMaxStackSize(1);
    }

    @Override
    public void registerIcons(IIconRegister ir) {
        icons[0] = ir.registerIcon("AdvanceWitchery:blackChalk");
        icons[1] = ir.registerIcon("AdvanceWitchery:bloodChalk");
    }

    @Override
    public IIcon getIcon(ItemStack stack, int pass) {
        return icons[0];
    }

    @Override
    public IIcon getIconFromDamage(int m) {
        return icons[0];
    }

    @Override
    public boolean onItemUseFirst(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ) {
        if (world.isRemote)
            return false;
        if (side == 1 && y <= 250) {
            world.setBlock(x, (y + 1), z, ModBlocks.blockCircle);
            if (stack.getItemDamage() >= 100)
                --stack.stackSize;
            else
                stack.damageItem(1, player);
            return true;
        }
        return false;
    }
}
