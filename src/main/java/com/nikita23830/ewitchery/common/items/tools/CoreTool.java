package com.nikita23830.ewitchery.common.items.tools;

import com.emoniph.witchery.common.ExtendedPlayer;
import com.meteor.extrabotany.common.core.util.IConfigurableItem;
import com.meteor.extrabotany.common.core.util.ItemNBTHelper;
import com.meteor.extrabotany.common.core.util.ToolHandler;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialLiquid;
import net.minecraft.client.Minecraft;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.item.*;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.server.S18PacketEntityTeleport;
import net.minecraft.network.play.server.S23PacketBlockChange;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.event.world.BlockEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CoreTool {

    public static Material[] materialsPick = new Material[]{ Material.rock, Material.iron, Material.ice, Material.glass, Material.piston, Material.anvil };
    public static Material[] materialsShovel = new Material[]{ Material.grass, Material.ground, Material.sand, Material.snow, Material.craftedSnow, Material.clay };
    public static Material[] materialsAxe = new Material[]{ Material.coral, Material.leaves, Material.plants, Material.wood, Material.vine };

    public static boolean CoreBreak(EntityPlayer player, Map<Block, Integer> blockMap, Block block, int meta, World world, int x, int y, int z, boolean breakSound) {
        if (player.capabilities.isCreativeMode || (blockMap.containsKey(block) && blockMap.get(block) == meta)) {

            block.onBlockHarvested(world, x, y, z, meta, player);
            if (block.removedByPlayer(world, player, x, y, z, false))
                block.onBlockDestroyedByPlayer(world, x, y, z, meta);

            if (!world.isRemote) {
                ((EntityPlayerMP) player).playerNetServerHandler.sendPacket(new S23PacketBlockChange(x, y, z, world));
            }

            if (breakSound) world.playAuxSFX(2001, x, y, z, Block.getIdFromBlock(block) + (meta << 12));
            return true;
        }

        if (!world.isRemote) {

            block.onBlockHarvested(world, x, y, z, meta, player);

            if (block.removedByPlayer(world, player, x, y, z, true)) {
                block.onBlockDestroyedByPlayer(world, x, y, z, meta);
                block.harvestBlock(world, player, x, y, z, meta);
                player.addExhaustion(-0.025F);
                if (block.getExpDrop(world, meta, EnchantmentHelper.getFortuneModifier(player)) > 0)
                    player.addExperience(block.getExpDrop(world, meta, EnchantmentHelper.getFortuneModifier(player)));
            }

            EntityPlayerMP mpPlayer = (EntityPlayerMP) player;
            mpPlayer.playerNetServerHandler.sendPacket(new S23PacketBlockChange(x, y, z, world));
        } else {
            if (breakSound) world.playAuxSFX(2001, x, y, z, Block.getIdFromBlock(block) + (meta << 12));
            if (block.removedByPlayer(world, player, x, y, z, true)) {
                block.onBlockDestroyedByPlayer(world, x, y, z, meta);
            }

            Minecraft.getMinecraft().getNetHandler().addToSendQueue(new C07PacketPlayerDigging(2, x, y, z, Minecraft.getMinecraft().objectMouseOver.sideHit));
        }
        return true;
    }

    public static boolean breakAOEBlocks(ItemStack stack, int x, int y, int z, int breakRadius, int breakDepth, EntityPlayer player, int typeToop, Entity customMOP) {
        Map<Block, Integer> blockMap = IConfigurableItem.ProfileHelper.getBoolean(stack, "ToolVoidJunk", false) ? CoreTool.getObliterationList(stack) : new HashMap<Block, Integer>();
        Block block = player.worldObj.getBlock(x, y, z);
        int meta = player.worldObj.getBlockMetadata(x, y, z);

        float refStrength = ForgeHooks.blockStrength(block, player, player.worldObj, x, y, z);

        MovingObjectPosition mop = ToolHandler.raytraceFromEntity(player.worldObj, customMOP, 4.5d);
        if (mop == null) {
            ToolHandler.updateGhostBlocks(player, player.worldObj);
            return true;
        }
        int sideHit = mop.sideHit;

        int xMax = breakRadius;
        int xMin = breakRadius;
        int yMax = breakRadius;
        int yMin = breakRadius;
        int zMax = breakRadius;
        int zMin = breakRadius;
        int yOffset = 0;

        switch (sideHit) {
            case 0:
                yMax = breakDepth;
                yMin = 0;
                zMax = breakRadius;
                break;
            case 1:
                yMin = breakDepth;
                yMax = 0;
                zMax = breakRadius;
                break;
            case 2:
                xMax = breakRadius;
                zMin = 0;
                zMax = breakDepth;
                yOffset = breakRadius - 1;
                break;
            case 3:
                xMax = breakRadius;
                zMax = 0;
                zMin = breakDepth;
                yOffset = breakRadius - 1;
                break;
            case 4:
                xMax = breakDepth;
                xMin = 0;
                zMax = breakRadius;
                yOffset = breakRadius - 1;
                break;
            case 5:
                xMin = breakDepth;
                xMax = 0;
                zMax = breakRadius;
                yOffset = breakRadius - 1;
                break;
        }

        boolean isBreak = false;
        for (int xPos = x - xMin; xPos <= x + xMax; xPos++) {
            if (isBreak)
                break;
            for (int yPos = y + yOffset - yMin; yPos <= y + yOffset + yMax; yPos++) {
                if (isBreak)
                    break;
                for (int zPos = z - zMin; zPos <= z + zMax; zPos++) {
                    if (cantBreak(player, xPos, yPos, zPos)) {
                        System.out.println("Debug 0");
                        isBreak = true;
                        break;
                    }

                    if (!breakExtraBlock(stack, player.worldObj, xPos, yPos, zPos, 0, player, refStrength, Math.abs(x - xPos) <= 1 && Math.abs(y - yPos) <= 1 && Math.abs(z - zPos) <= 1, blockMap, typeToop))
                        return true;
                }
            }
        }

        @SuppressWarnings("unchecked") List<EntityItem> items = player.worldObj.getEntitiesWithinAABB(EntityItem.class, AxisAlignedBB.getBoundingBox(x - xMin, y + yOffset - yMin, z - zMin, x + xMax + 1, y + yOffset + yMax + 1, z + zMax + 1));
        for (EntityItem item : items) {
            if (!player.worldObj.isRemote) {
                item.setLocationAndAngles(player.posX, player.posY, player.posZ, 0, 0);
                ((EntityPlayerMP) player).playerNetServerHandler.sendPacket(new S18PacketEntityTeleport(item));
                item.delayBeforeCanPickup = 0;
            }
        }

        return true;
    }

    public static boolean cantBreak(EntityPlayer player, int x, int y, int z) {
        try {
            if (Bukkit.getServer() == null)
                return false;
            Player bPlayer = Bukkit.getPlayer(player.getUniqueID());
            BlockBreakEvent event = new BlockBreakEvent(bPlayer.getWorld().getBlockAt(x, y, z), bPlayer);
            Bukkit.getPluginManager().callEvent(event);
            return event.isCancelled();
        } catch (Throwable var6) {
            return true;
        }
    }

    protected static boolean breakExtraBlock(ItemStack stack, World world, int x, int y, int z, int totalSize, EntityPlayer player, float refStrength, boolean breakSound, Map<Block, Integer> blockMap, int typeTool) {
        if (world.isAirBlock(x, y, z)) return true;

        Block block = world.getBlock(x, y, z);

        if (typeTool != -1) {
            boolean breakBlock = false;
            Material[] materailsBlock = typeTool == 0 ? materialsAxe : typeTool == 1 ? materialsShovel : materialsPick;
            for (Material material : materailsBlock) {
                if (block.getMaterial() == material) {
                    breakBlock = true;
                    break;
                }
            }
            if (!breakBlock) {
                return true;
            }
        }

        if (block.getMaterial() instanceof MaterialLiquid || (block.getBlockHardness(world, x, y, x) <= 0 && !player.capabilities.isCreativeMode)) {
            return true;
        }
        
        if (ExtendedPlayer.get(player).getBloodPower() >= 10) {
            ExtendedPlayer.get(player).setBloodPower(ExtendedPlayer.get(player).getBloodPower() - 10);
        } else 
            return false;

        int meta = world.getBlockMetadata(x, y, z);

        float strength = ForgeHooks.blockStrength(block, player, world, x, y, z);

        if (!ForgeHooks.canHarvestBlock(block, player, meta) || strength <= 0.0F && !player.capabilities.isCreativeMode) {
            return true;
        }

        if (!world.isRemote) {
            BlockEvent.BreakEvent event = ForgeHooks.onBlockBreakEvent(world, world.getWorldInfo().getGameType(), (EntityPlayerMP) player, x, y, z);
            if (event.isCanceled()) {
                ((EntityPlayerMP) player).playerNetServerHandler.sendPacket(new S23PacketBlockChange(x, y, z, world));
                return true;
            }
        }

        return CoreTool.CoreBreak(player, blockMap, block, meta, world, x, y, z, breakSound);
    }

    public static Map<Block, Integer> getObliterationList(ItemStack stack) {
        Map<Block, Integer> blockMap = new HashMap<Block, Integer>();

        NBTTagCompound compound = ItemNBTHelper.getCompound(stack);

        if (compound.hasNoTags()) return blockMap;
        for (int i = 0; i < 9; i++) {
            NBTTagCompound tag = new NBTTagCompound();
            if (compound.hasKey("Item" + i)) tag = compound.getCompoundTag("Item" + i);

            if (tag.hasNoTags()) continue;

            ItemStack stack1 = ItemStack.loadItemStackFromNBT(tag);

            if (stack1 != null && stack1.getItem() instanceof ItemBlock)
                blockMap.put(Block.getBlockFromItem(stack1.getItem()), stack1.getItemDamage());
        }

        return blockMap;
    }

    public static void breakAOE(ItemStack st, int x, int y, int z, EntityPlayer pl, int type, int tool, Entity customMOP) {
        breakAOEBlocks(st, x, y, z, type, type, pl, tool, customMOP);
    }

    public static short hasToolEffectFortune(ItemStack stack) {
        NBTTagList ench = stack.getEnchantmentTagList();
        short fortune = 0;
        if (ench == null)
            return fortune;
        for (byte i = 0; i < Math.min(ench.tagCount(), 127); ++i) {
            NBTTagCompound en = ench.getCompoundTagAt(i);
            if (en.hasKey("id") && en.getShort("id") == Enchantment.fortune.effectId && en.hasKey("lvl"))
                fortune = en.getShort("lvl");
        }
        return fortune;
    }

    public static boolean checkMaterial(Block bl, ItemStack stack) {
        if (stack == null)
            return false;
        if (stack.getItem() instanceof ItemTool) {
            if (stack.getItem() instanceof ItemAxe)
                return checkMaterial(bl, 0);
            if (stack.getItem() instanceof ItemSpade)
                return checkMaterial(bl, 1);
            if (stack.getItem() instanceof ItemPickaxe)
                return checkMaterial(bl, 2);
        }
        return false;
    }

    public static boolean checkMaterial(Block bl, int tool) {
        if (bl == null || bl == Blocks.air)
            return false;
        switch (tool) {
            case 0: {// Axe
                for (Material m : materialsAxe) {
                    if (bl.getMaterial() == m)
                        return true;
                }
                break;
            }
            case 1: {// Shovel
                for (Material m : materialsShovel) {
                    if (bl.getMaterial() == m)
                        return true;
                }
                break;
            }
            case 2: {// Pick
                for (Material m : materialsPick) {
                    if (bl.getMaterial() == m)
                        return true;
                }
                break;
            }
        }
        return false;
    }

}
