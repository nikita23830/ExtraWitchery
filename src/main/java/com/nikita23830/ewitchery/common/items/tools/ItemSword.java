package com.nikita23830.ewitchery.common.items.tools;

import com.emoniph.witchery.common.ExtendedPlayer;
import com.google.common.collect.Multimap;
import com.nikita23830.ewitchery.AdvanceWitchery;
import com.nikita23830.ewitchery.common.utils.DamageSourceSun;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

public class ItemSword extends net.minecraft.item.ItemSword {

    public ItemSword() {
        super(ItemPick.material);
        GameRegistry.registerItem(this, "awitchery.sword");
        setUnlocalizedName(AdvanceWitchery.modid() + ".sword");
        setTextureName(AdvanceWitchery.modid() + ":sword");
    }

    @Override
    public boolean hitEntity(ItemStack stack, EntityLivingBase target, EntityLivingBase attacker) {
        if (attacker instanceof EntityPlayer && !attacker.worldObj.isRemote) {
            if (target instanceof EntityPlayer && ExtendedPlayer.get((EntityPlayer) target).isVampire())
                ExtendedPlayer.get((EntityPlayer) target).setBloodPower(0);
            EntityPlayer player = (EntityPlayer) attacker;
            if (player.worldObj.rand.nextDouble() <= .2D)
                target.attackEntityFrom(DamageSourceSun.create(player), 10F);
        }
        return true;
    }
}
