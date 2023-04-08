package com.nikita23830.ewitchery.common.items;

import com.nikita23830.ewitchery.common.entity.projectile.EntityModArrow;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

public interface IArrow {
    EntityModArrow createEntity(EntityPlayer player, float strength);
}
