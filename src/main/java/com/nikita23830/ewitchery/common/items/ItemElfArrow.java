package com.nikita23830.ewitchery.common.items;

import com.nikita23830.ewitchery.AdvanceWitchery;
import com.nikita23830.ewitchery.common.entity.projectile.EntityElfArrow;
import com.nikita23830.ewitchery.common.entity.projectile.EntityModArrow;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

public class ItemElfArrow extends ModItem implements IArrow{
    public IIcon icon = null;

    public ItemElfArrow() {
        super("elfArrow");
        this.setMaxStackSize(64);
        this.setHasSubtypes(true);
        this.setMaxDamage(0);
    }

    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister ir) {
        this.icon = ir.registerIcon(AdvanceWitchery.MODID.toLowerCase() + ":elf-arrow");
    }

    @SideOnly(Side.CLIENT)
    public IIcon getIconFromDamage(int par1) {
        return this.icon;
    }

    @Override
    public EntityModArrow createEntity(EntityPlayer player, float strength) {
        return new EntityElfArrow(player.worldObj, player, strength);
    }
}
