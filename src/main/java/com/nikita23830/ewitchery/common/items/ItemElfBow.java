package com.nikita23830.ewitchery.common.items;

import com.nikita23830.ewitchery.AdvanceWitchery;
import com.nikita23830.ewitchery.common.ModItems;
import com.nikita23830.ewitchery.common.entity.projectile.EntityModArrow;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.init.Items;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.ArrowLooseEvent;
import net.minecraftforge.event.entity.player.ArrowNockEvent;

public class ItemElfBow extends ItemBow {
    private IIcon[] iconArray;

    public ItemElfBow() {
        super();
        GameRegistry.registerItem(this, getClass().getSimpleName());
        setTextureName(AdvanceWitchery.MODID.toLowerCase() + ":bow_standby");
    }

    private int getSlotWithArrow(EntityPlayer player) {
        for (int i = 0; i < player.inventory.mainInventory.length; ++i) {
            if (player.inventory.mainInventory[i] == null)
                continue;
            if (player.inventory.mainInventory[i].getItem() instanceof IArrow)
                return i;
        }
        return -1;
    }

    public void onPlayerStoppedUsing(ItemStack stack, World world, EntityPlayer player, int time) {
        int j = this.getMaxItemUseDuration(stack) - time;

        ArrowLooseEvent event = new ArrowLooseEvent(player, stack, j);
        MinecraftForge.EVENT_BUS.post(event);
        if (event.isCanceled()) {
            return;
        }
        j = event.charge;

        boolean flag = player.capabilities.isCreativeMode || EnchantmentHelper.getEnchantmentLevel(Enchantment.infinity.effectId, stack) > 0;

        int slotWithArrow = getSlotWithArrow(player);
        if (flag || slotWithArrow != -1) {
            float f = (float)j / 20.0F;
            f = (f * f + f * 2.0F) / 3.0F;

            if ((double)f < 0.1D) {
                return;
            }

            if (f > 1.0F)
            {
                f = 1.0F;
            }

            EntityModArrow entityarrow = ((IArrow)player.inventory.mainInventory[slotWithArrow].getItem()).createEntity(player, f * 2.0F);

            if (f == 1.0F) {
                entityarrow.setIsCritical(true);
            }

            int k = EnchantmentHelper.getEnchantmentLevel(Enchantment.power.effectId, stack);

            if (k > 0) {
                entityarrow.setDamage(entityarrow.getDamage() + (double)k * 0.5D + 0.5D);
            }

            int l = EnchantmentHelper.getEnchantmentLevel(Enchantment.punch.effectId, stack);

            if (l > 0) {
                entityarrow.setKnockbackStrength(l);
            }

            if (EnchantmentHelper.getEnchantmentLevel(Enchantment.flame.effectId, stack) > 0) {
                entityarrow.setFire(100);
            }

            stack.damageItem(1, player);
            world.playSoundAtEntity(player, "random.bow", 1.0F, 1.0F / (itemRand.nextFloat() * 0.4F + 1.2F) + f * 0.5F);

            if (flag) {
                entityarrow.canBePickedUp = 2;
            } else {
                --player.inventory.mainInventory[slotWithArrow].stackSize;
            }

            if (!world.isRemote) {
                world.spawnEntityInWorld(entityarrow);
            }
        }
    }

    public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player) {
        ArrowNockEvent event = new ArrowNockEvent(player, stack);
        MinecraftForge.EVENT_BUS.post(event);
        if (event.isCanceled()) {
            return event.result;
        }

        if (player.capabilities.isCreativeMode || (player.inventory.hasItem(ModItems.bloodArrow) || player.inventory.hasItem(ModItems.sunArrow) || player.inventory.hasItem(ModItems.elfArrow))) {
            player.setItemInUse(stack, this.getMaxItemUseDuration(stack));
        }

        return stack;
    }

    @Override
    protected String getIconString() {
        return AdvanceWitchery.MODID.toLowerCase() + ":bow";
    }

    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister ir) {
        this.itemIcon = ir.registerIcon(this.getIconString() + "_standby");
        this.iconArray = new IIcon[bowPullIconNameArray.length];

        for (int i = 0; i < this.iconArray.length; ++i) {
            this.iconArray[i] = ir.registerIcon(this.getIconString() + "_" + bowPullIconNameArray[i]);
        }
    }

    @Override
    public IIcon getIcon(ItemStack stack, int renderPass, EntityPlayer player, ItemStack usingItem, int useRemaining) {
        int c = stack.getMaxItemUseDuration() - useRemaining;
        if (player.isUsingItem())
            return this.iconArray[c >= 18 ? 2 : c >= 13 ? 1 : 0];
        return super.getIcon(stack, renderPass, player, usingItem, useRemaining);
    }

    @SideOnly(Side.CLIENT)
    public IIcon getItemIconForUseDuration(int time) {
        return this.iconArray[time];
    }
}
