package com.nikita23830.ewitchery.common.items;

import com.nikita23830.ewitchery.AdvanceWitchery;

public class ItemFragmentNetherStar extends ModItem {
    public ItemFragmentNetherStar() {
        super("fragmentNetherStar");
        setMaxStackSize(8);
        setTextureName(AdvanceWitchery.MODID.toLowerCase() + ":fragNetherStar");
    }
}
