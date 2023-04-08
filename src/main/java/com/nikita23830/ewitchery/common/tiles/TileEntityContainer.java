package com.nikita23830.ewitchery.common.tiles;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;

public abstract class TileEntityContainer extends TileEntity implements IInventory {
    private final int slotCount;
    private int playersUsing;

    public ItemStack[] inventory = new ItemStack[72];

    public TileEntityContainer(int slotCount) {
        this.slotCount = slotCount;
    }

    @Override
    public final void updateEntity() {
        super.updateEntity();
        if (worldObj.isRemote)
            updateEntityClient();
        else
            updateEntityServer();
    }

    public abstract void updateEntityServer();

    public abstract void updateEntityClient();

    @Override
    public void writeToNBT(NBTTagCompound nbt) {
        super.writeToNBT(nbt);
        NBTTagList nbttaglist = new NBTTagList();
        for(int i = 0; i < this.inventory.length; ++i) {
            if(this.inventory[i] != null) {
                NBTTagCompound nbttagcompound1 = new NBTTagCompound();
                nbttagcompound1.setByte("Slot", (byte)i);
                this.inventory[i].writeToNBT(nbttagcompound1);
                nbttaglist.appendTag(nbttagcompound1);
            }
        }

        nbt.setTag("Items", nbttaglist);
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        super.readFromNBT(nbt);
        NBTTagList nbttaglist = nbt.getTagList("Items", 10);
        this.inventory = new ItemStack[this.getSizeInventory()];
        for(int i = 0; i < nbttaglist.tagCount(); ++i) {
            NBTTagCompound nbttagcompound1 = nbttaglist.getCompoundTagAt(i);
            int j = nbttagcompound1.getByte("Slot") & 255;
            if(j >= 0 && j < this.inventory.length) {
                ItemStack v0 = ItemStack.loadItemStackFromNBT(nbttagcompound1);
                this.inventory[j] = v0;
            }
        }
    }

    @Override
    public int getSizeInventory() {
        return slotCount;
    }

    @Override
    public ItemStack getStackInSlot(int ind) {
        return inventory[ind];
    }

    @Override
    public ItemStack decrStackSize(int idSlot, int stackSize) {
        if(this.inventory[idSlot] != null) {
            ItemStack itemstack;
            if(this.inventory[idSlot].stackSize <= stackSize) {
                itemstack = this.inventory[idSlot];
                this.inventory[idSlot] = null;
                this.markDirty();
                return itemstack;
            } else {
                itemstack = this.inventory[idSlot].splitStack(stackSize);
                if(this.inventory[idSlot].stackSize == 0) {
                    this.inventory[idSlot] = null;
                }

                this.markDirty();
                return itemstack;
            }
        } else {
            return null;
        }
    }

    @Override
    public Packet getDescriptionPacket() {
        NBTTagCompound data = new NBTTagCompound();
        this.writeToNBT(data);
        return new S35PacketUpdateTileEntity(this.xCoord, this.yCoord, this.zCoord, 2, data);
    }

    @Override
    public void onDataPacket(NetworkManager netManager, S35PacketUpdateTileEntity packet) {
        readFromNBT(packet.func_148857_g());
    }

    @Override
    public ItemStack getStackInSlotOnClosing(int index) {
        if(this.inventory[index] != null) {
            ItemStack itemstack = this.inventory[index];
            this.inventory[index] = null;
            return itemstack;
        } else {
            return null;
        }
    }

    @Override
    public void setInventorySlotContents(int index, ItemStack item) {
        this.inventory[index] = item;
        if(item != null && item.stackSize > this.getInventoryStackLimit()) {
            item.stackSize = this.getInventoryStackLimit();
        }

        this.markDirty();
    }

    @Override
    public String getInventoryName() {
        return null;
    }

    @Override
    public boolean hasCustomInventoryName() {
        return false;
    }

    @Override
    public int getInventoryStackLimit() {
        return 64;
    }

    @Override
    public boolean isUseableByPlayer(EntityPlayer entityPlayer) {
        return super.worldObj.getTileEntity(super.xCoord, super.yCoord, super.zCoord) != this ? false : entityPlayer.getDistanceSq((double)super.xCoord + 0.5D, (double)super.yCoord + 0.5D, (double)super.zCoord + 0.5D) <= 64.0D;
    }

    @Override
    public void openInventory() {
        triggerPlayerUsageChange(1);
    }

    public void closeInventory() {
        triggerPlayerUsageChange(-1);
    }

    private void triggerPlayerUsageChange(int change) {
        if (worldObj != null) {
            playersUsing += change;
            worldObj.addBlockEvent(xCoord, yCoord, zCoord, super.getBlockType(), 1, playersUsing);
        }
    }

    public boolean canExecuteSlot(int id) {
        return getStackInSlot(id) == null || (getStackInSlot(id).stackSize < getStackInSlot(id).getMaxStackSize());
    }

    public boolean canTakeFromSlot(int id) {
        return getStackInSlot(id) != null;
    }

    public boolean canExecuteSlot(int id, int count) {
        if (getStackInSlot(id) == null)
            return true;
        return (getStackInSlot(id).copy().stackSize + count) <= getStackInSlot(id).getMaxStackSize();
    }

    public void onBlockBreak() {
        for (ItemStack stack : inventory) {
            if (stack != null) {
                EntityItem ei = new EntityItem(worldObj, (xCoord + 0.5D), (yCoord + 0.5D), (zCoord + 0.5D), stack);
                worldObj.spawnEntityInWorld(ei);
            }
        }
    }

    @Override
    public boolean isItemValidForSlot(int slot, ItemStack stack) {
        return true;
    }
}

