package com.nikita23830.ewitchery.common.events;

import com.nikita23830.ewitchery.common.entity.EntityElly;
import cpw.mods.fml.common.eventhandler.Cancelable;
import cpw.mods.fml.common.eventhandler.Event;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;

@Cancelable
public abstract class EllyPutItemsEvent extends Event {
    private final EntityElly elly;
    private final TileEntity te;
    private ItemStack put;
    private boolean cancel;

    public EllyPutItemsEvent(EntityElly elly, TileEntity te, ItemStack put) {
        this.elly = elly;
        this.te = te;
        this.put = put;
    }

    public EntityElly getElly() {
        return elly;
    }

    public TileEntity getInventory() {
        return te;
    }

    public ItemStack getItemPut() {
        return put;
    }

    public void setItemPut(ItemStack put) {
        this.put = put;
    }

    @Override
    public boolean isCanceled() {
        return cancel;
    }

    @Override
    public void setCanceled(boolean cancel) {
        super.setCanceled(cancel);
    }

    public static class Pre extends EllyPutItemsEvent {

        public Pre(EntityElly elly, TileEntity te, ItemStack put) {
            super(elly, te, put);
        }
    }

    public static class Post extends EllyPutItemsEvent {

        public Post(EntityElly elly, TileEntity te, ItemStack put) {
            super(elly, te, put);
        }
    }
}
