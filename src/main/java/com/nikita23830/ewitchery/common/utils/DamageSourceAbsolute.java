package com.nikita23830.ewitchery.common.utils;

import net.minecraft.util.DamageSource;

public class DamageSourceAbsolute extends DamageSource {
    public static DamageSourceAbsolute source = (DamageSourceAbsolute) new DamageSourceAbsolute("advancewitchery.absolute").setDamageIsAbsolute().setDamageBypassesArmor().setDamageAllowedInCreativeMode();

    public DamageSourceAbsolute(String p_i1566_1_) {
        super(p_i1566_1_);
    }
}
