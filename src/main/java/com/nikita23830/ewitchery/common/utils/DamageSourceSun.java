package com.nikita23830.ewitchery.common.utils;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EntityDamageSource;

public class DamageSourceSun extends EntityDamageSource {

    private DamageSourceSun(EntityPlayer player) {
        super("damage.advancewitchery.sun", player);
        setDamageIsAbsolute();
        setFireDamage();
        setDamageBypassesArmor();
        setDamageAllowedInCreativeMode();
    }

    public static DamageSourceSun create(EntityPlayer player) {
        return new DamageSourceSun(player);
    }
}
