package com.nikita23830.ewitchery.common.items;

import com.emoniph.witchery.common.ExtendedPlayer;
import com.nikita23830.ewitchery.AdvanceWitchery;
import cpw.mods.fml.relauncher.ReflectionHelper;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import vazkii.botania.api.mana.ManaItemHandler;
import vazkii.botania.common.lib.LibObfuscation;

import java.util.List;

public class ItemHeart extends ModItem {
    IIcon[] icons = new IIcon[4];

    public ItemHeart() {
        super("heart");
        hasSubtypes = true;
        setMaxDamage(0);
        setMaxStackSize(1);
    }

    @Override
    public void getSubItems(Item item, CreativeTabs tab, List list) {
        for (int i = 0; i < 4; ++i) {
            list.add(new ItemStack(this, 1, i));
        }
    }

    @Override
    public void registerIcons(IIconRegister iu) {
        for (int i = 0; i < 4; ++i) {
            icons[i] = iu.registerIcon(AdvanceWitchery.MODID.toLowerCase() + ":heart" + i);
        }
    }

    @Override
    public IIcon getIconFromDamage(int m) {
        return m < 4 ? icons[m] : icons[0];
    }

    @Override
    public int getMaxItemUseDuration(ItemStack stack) {
        if (stack.getItemDamage() == 3)
            return 32;
        return super.getMaxItemUseDuration(stack);
    }

    @Override
    public EnumAction getItemUseAction(ItemStack stack) {
        if (stack.getItemDamage() == 3)
            return EnumAction.eat;
        return super.getItemUseAction(stack);
    }

    public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player) {
        if (stack.getItemDamage() == 3) {
            if (ExtendedPlayer.get(player).isVampire() && ExtendedPlayer.get(player).getBloodPower() < ExtendedPlayer.get(player).getMaxBloodPower()) {
                player.setItemInUse(stack, this.getMaxItemUseDuration(stack));
            }

            return stack;
        }
        return super.onItemRightClick(stack, world, player);
    }

    public void onUsingTick(ItemStack stack, EntityPlayer player, int count) {
        super.onUsingTick(stack, player, count);
        if (stack.getItemDamage() != 3)
            return;
        if (count % 5 == 0 && ExtendedPlayer.get(player).isVampire() && ExtendedPlayer.get(player).getBloodPower() < ExtendedPlayer.get(player).getMaxBloodPower()) {
            ExtendedPlayer.get(player).setBloodPower(ExtendedPlayer.get(player).getBloodPower() + 100);
        }
    }
}
