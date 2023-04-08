package com.nikita23830.ewitchery.common.recipes;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;

public class ContainerNull extends Container {

    @Override
    public boolean canInteractWith(EntityPlayer p_75145_1_) {
        return false;
    }

    @Override
    public void onCraftMatrixChanged(IInventory p_75130_1_) {
        // NO-OP
    }
}
