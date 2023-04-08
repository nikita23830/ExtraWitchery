package com.nikita23830.ewitchery.common.events;

import alfheim.common.core.handler.AlfheimConfigHandler;
import alfheim.common.entity.EntityElf;
import com.emoniph.witchery.common.ExtendedPlayer;
import com.emoniph.witchery.entity.EntityEnt;
import com.emoniph.witchery.entity.EntityFollower;
import com.emoniph.witchery.entity.EntityHornedHuntsman;
import com.meteor.extrabotany.common.entity.EntityAsgard;
import com.nikita23830.ewitchery.common.CommonProxy;
import com.nikita23830.ewitchery.common.ModItems;
import com.nikita23830.ewitchery.common.entity.EntityCursedElf;
import com.nikita23830.ewitchery.common.entity.EntityElly;
import com.nikita23830.ewitchery.common.entity.EntityJabberwock;
import com.nikita23830.ewitchery.common.entity.EntityTrent;
import com.nikita23830.ewitchery.common.items.ItemSyringe;
import com.nikita23830.ewitchery.common.items.armor.ModArmor;
import com.nikita23830.ewitchery.common.network.NetworkHandler;
import com.nikita23830.ewitchery.common.network.client.PacketOpenDialogElle;
import com.nikita23830.ewitchery.common.stats.StatManager;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent;
import net.minecraft.entity.monster.EntityBlaze;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.DamageSource;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.player.EntityInteractEvent;
import org.bukkit.Bukkit;

import java.util.HashMap;
import java.util.UUID;

public class ModEvents {
    public static ModEvents events = new ModEvents();
    private static int[][] ritualA = new int[][]{{0,2},{1,2},{2,1},{2,0},{2,-1},{1,-2},{0,-2},{-1,-2},{-2,-1},{-2,0},{-2,1},{-1,2}};

    private static boolean checkedBukkit = false;
    private static boolean hasBukkit = false;

    private static HashMap<UUID, Integer> cdTickServer = new HashMap<>();

    public static void init() {
        FMLCommonHandler.instance().bus().register(events);
        MinecraftForge.EVENT_BUS.register(events);
    }

    @SubscribeEvent
    public void onSunAttack(CheckSunEvent event) {
        if (event.getEntity() instanceof EntityPlayer) {
            if (ModArmor.isFullSet((EntityPlayer) event.getEntity())) {
                event.setResult(false);
            }
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void onFireDamage(LivingAttackEvent event) {
        if (event.entity instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) event.entity;
            if (event.source == DamageSource.inFire || event.source == DamageSource.onFire || event.source == DamageSource.lava) {
                event.setCanceled(ModArmor.isFullSet(player));
            }
        }
    }

    @SubscribeEvent
    public void onPlayerJoin(PlayerEvent.PlayerLoggedInEvent event) {
        if (event.player.worldObj.isRemote)
            return;
        try {
            if (!checkedBukkit) {
                checkedBukkit = true;
                hasBukkit = hasBukkit();
            }
            if (hasBukkit) {
                if (Bukkit.getServer() != null && Bukkit.getPlayer(event.player.getUniqueID()) != null && !Bukkit.getPlayer(event.player.getUniqueID()).hasPlayedBefore()) {
                    CommonProxy.addOrDropItem(ModItems.orderBook.create(0), event.player);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean hasBukkit() {
        try {
            Class.forName("org.bukkit.Bukkit");
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @SubscribeEvent
    public void onClickEntity(EntityInteractEvent event) {
        if (event.entity.worldObj.isRemote)
            return;
        if (event.target instanceof EntityFollower && event.entity instanceof EntityPlayer) {
            if (!ExtendedPlayer.get((EntityPlayer) event.entity).isVampire()) {
                ((EntityPlayer)event.entity).addChatComponentMessage(new ChatComponentText("§eElle§f> Ты не вампир! Ты мне не интересен"));
                return;
            }
            event.setCanceled(true);
            int idDialog = StatManager.getIdDialog((EntityPlayer) event.entity);
            if (idDialog == 7) {
                EntityElly elly = new EntityElly(event.target.worldObj, (EntityPlayer) event.entity);
                elly.setHome(new ChunkCoordinates(event.entity.serverPosX, event.entity.serverPosY, event.entity.serverPosZ));
                elly.setPositionAndRotation(
                        event.target.posX,
                        event.target.posY,
                        event.target.posZ,
                        event.target.rotationYaw,
                        event.target.rotationPitch
                );
                event.target.worldObj.spawnEntityInWorld(elly);
                event.target.setDead();
            }
            if (idDialog >= 0)
                NetworkHandler.INSTANCE.sendTo(new PacketOpenDialogElle(idDialog), (EntityPlayerMP) event.entity);
        }

        boolean isAlfheim = (event.target != null && event.target.worldObj.provider.dimensionId == AlfheimConfigHandler.INSTANCE.getDimensionIDAlfheim()) || (event.entity instanceof EntityPlayer && ((EntityPlayer)event.entity).capabilities.isCreativeMode);

        if (event.target instanceof EntityElf && event.entity instanceof EntityPlayer) {
            event.setCanceled(true);
            ItemStack stack = ((EntityPlayer) event.entity).inventory.getCurrentItem();
            if (stack == null)
                return;
            if (stack.getItemDamage() != 2)
                return;
            if (stack.getItem() instanceof ItemSyringe && isAlfheim) {
                if (stack.getItemDamage() == 0) {
                    EntityAsgard asgard = new EntityAsgard(event.target.worldObj);
                    asgard.setPositionAndRotation(event.target.posX, event.target.posY, event.target.posZ, event.target.rotationYaw, event.target.rotationPitch);
                    ((EntityPlayer) event.entity).addChatComponentMessage(new ChatComponentText("§eElf§f> §7Как смеешь ты? Стража!"));
                    event.target.worldObj.spawnEntityInWorld(asgard);
                    --((EntityPlayer) event.entity).inventory.getCurrentItem().stackSize;
                    event.target.setDead();
                } else if (stack.getItemDamage() == 2) {
                    event.target.setDead();
                    if (event.target.worldObj.rand.nextDouble() >= .6D) {
                        ((EntityPlayer) event.entity).inventory.getCurrentItem().setItemDamage(1);
                    } else {
                        --((EntityPlayer) event.entity).inventory.getCurrentItem().stackSize;
                        EntityCursedElf elf = new EntityCursedElf(event.target.worldObj);
                        elf.setPositionAndRotation(event.target.posX, event.target.posY, event.target.posZ, event.target.rotationYaw, event.target.rotationPitch);
                        event.target.worldObj.spawnEntityInWorld(elf);
                    }
                }
            }
        }
        if (event.target instanceof EntityEnt && event.entity instanceof EntityPlayer && isAlfheim) {
            ItemStack stack = ((EntityPlayer) event.entity).inventory.getCurrentItem();
            if (stack == null)
                return;
            if (!(stack.getItem() instanceof ItemSyringe))
                return;
            if (stack.getItemDamage() != 2)
                return;
            --((EntityPlayer) event.entity).inventory.getCurrentItem().stackSize;
            if (event.target.worldObj.rand.nextDouble() <= .7D) {
                event.target.setDead();
            } else {
                EntityTrent trent = new EntityTrent(event.target.worldObj);
                trent.setPositionAndRotation(event.target.posX, event.target.posY, event.target.posZ, event.target.rotationYaw, event.target.rotationPitch);
                event.target.setDead();
                event.entity.worldObj.spawnEntityInWorld(trent);
            }
        }
        if (event.target instanceof EntityHornedHuntsman && event.entity instanceof EntityPlayer) {
            ItemStack stack = ((EntityPlayer) event.entity).inventory.getCurrentItem();
            if (stack == null)
                return;
            if (!(stack.getItem() instanceof ItemSyringe))
                return;
            if (stack.getItemDamage() != 2)
                return;
            --((EntityPlayer) event.entity).inventory.getCurrentItem().stackSize;
            if (event.target.worldObj.rand.nextDouble() >= .9D) {
                EntityJabberwock ej = new EntityJabberwock(event.target.worldObj);
                ej.setPositionAndUpdate(event.target.posX, event.target.posY, event.target.posZ);
                event.target.setDead();
                event.entity.worldObj.spawnEntityInWorld(ej);
            } else {
                CommonProxy.addOrDropItem(ModItems.syringe.create(3), (EntityPlayer) event.entity);
                ((EntityHornedHuntsman) event.target).setHealth(((EntityHornedHuntsman) event.target).getHealth() / 2F);
            }
        }
        if (event.target instanceof EntityBlaze && !(event.target instanceof com.nikita23830.ewitchery.common.entity.EntityBlaze) && event.entity instanceof EntityPlayer) {
            ItemStack stack = ((EntityPlayer) event.entity).inventory.getCurrentItem();
            if (stack == null)
                return;
            if (!(stack.getItem() instanceof ItemSyringe))
                return;
            if (stack.getItemDamage() != 3)
                return;
            --((EntityPlayer) event.entity).inventory.getCurrentItem().stackSize;
            com.nikita23830.ewitchery.common.entity.EntityBlaze.freeze((EntityBlaze) event.target);
        }
    }

}
