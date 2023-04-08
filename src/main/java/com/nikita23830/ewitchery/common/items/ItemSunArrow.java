package com.nikita23830.ewitchery.common.items;

import com.emoniph.witchery.client.particle.NaturePowerFX;
import com.emoniph.witchery.common.ExtendedPlayer;
import com.nikita23830.ewitchery.AdvanceWitchery;
import com.nikita23830.ewitchery.common.entity.IArrowConsumer;
import com.nikita23830.ewitchery.common.entity.projectile.EntityModArrow;
import com.nikita23830.ewitchery.common.stats.StatManager;
import com.nikita23830.ewitchery.common.utils.DamageSourceSun;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.Random;
import java.util.UUID;

public class ItemSunArrow extends ModItem implements IArrow {

    public IIcon icon = null;
    private int cd = 0;
    private static ArrayList<UUID> ignore = new ArrayList<>();

    public ItemSunArrow() {
        super("sunArrow");
        this.setMaxStackSize(64);
        this.setHasSubtypes(true);
        this.setMaxDamage(0);
    }

    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister ir) {
        this.icon = ir.registerIcon(AdvanceWitchery.MODID.toLowerCase() + ":sunArrow");
    }

    @SideOnly(Side.CLIENT)
    public IIcon getIconFromDamage(int par1) {
        return this.icon;
    }

    @Override
    public EntityModArrow createEntity(EntityPlayer player, float strength) {
        return null;
    }

    @Override
    public void onUpdate(ItemStack stack, World w, Entity e, int slot, boolean p_77663_5_) {
        super.onUpdate(stack, w, e, slot, p_77663_5_);
        if (e instanceof EntityPlayer && !w.isRemote) {
            if (ignore.contains(e.getUniqueID()))
                return;
            if (cd > 0) {
                --cd;
                return;
            }
            cd = 20;
            if (stack.stackSize < 32)
                return;
            if (!StatManager.hasStat(StatManager.makeSunArrow, (EntityPlayer) e)) {
                ((EntityPlayer) e).triggerAchievement(StatManager.makeSunArrow);
                ignore.add(e.getUniqueID());
            } else {
                ignore.add(e.getUniqueID());
            }
        }
    }
}
