package com.nikita23830.ewitchery.common.tiles;

import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class TileCirle extends TileEntityContainer implements ISidedInventory {

    public TileCirle() {
        super(1);
    }

    @Override
    public void updateEntityServer() {

    }

    @Override
    public void updateEntityClient() {

    }

    @Override
    public void writeToNBT(NBTTagCompound n) {
        super.writeToNBT(n);

    }

    @Override
    public void readFromNBT(NBTTagCompound n) {
        super.readFromNBT(n);

    }

    @Override
    public int[] getAccessibleSlotsFromSide(int p_94128_1_) {
        return new int[0];
    }

    @Override
    public boolean canInsertItem(int p_102007_1_, ItemStack p_102007_2_, int p_102007_3_) {
        return false;
    }

    @Override
    public boolean canExtractItem(int p_102008_1_, ItemStack p_102008_2_, int p_102008_3_) {
        return false;
    }
}
