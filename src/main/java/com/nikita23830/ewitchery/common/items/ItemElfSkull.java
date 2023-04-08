package com.nikita23830.ewitchery.common.items;

import com.nikita23830.ewitchery.AdvanceWitchery;

public class ItemElfSkull extends ModItem {
    public ItemElfSkull() {
        super("elfSkull");
        setMaxStackSize(1);
        setTextureName(AdvanceWitchery.MODID.toLowerCase() + ":skull");
    }
}
