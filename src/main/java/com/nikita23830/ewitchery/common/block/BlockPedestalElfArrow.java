package com.nikita23830.ewitchery.common.block;

import com.nikita23830.ewitchery.AdvanceWitchery;
import com.nikita23830.ewitchery.client.ClientProxy;
import com.nikita23830.ewitchery.common.items.ItemElfArrow;
import com.nikita23830.ewitchery.common.tiles.TilePedestalElfArrow;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;

import java.util.ArrayList;

public class BlockPedestalElfArrow extends BlockContainer {

    public BlockPedestalElfArrow() {
        super(Material.rock);
        GameRegistry.registerBlock(this, getClass().getSimpleName());
        GameRegistry.registerTileEntity(TilePedestalElfArrow.class, AdvanceWitchery.MODID.toLowerCase() + "." + TilePedestalElfArrow.class.getSimpleName());
    }

    @Override
    public TileEntity createNewTileEntity(World p_149915_1_, int p_149915_2_) {
        return new TilePedestalElfArrow();
    }

    public boolean isOpaqueCube() {
        return false;
    }

    public boolean renderAsNormalBlock() {
        return false;
    }

    @Override
    public int getRenderType() {
        return ClientProxy.renderPedestalElfArrow;
    }

    @Override
    public boolean onBlockActivated(World w, int x, int y, int z, EntityPlayer player, int p_149727_6_, float p_149727_7_, float p_149727_8_, float p_149727_9_) {
        if (w.isRemote)
            return true;
        TileEntity te = w.getTileEntity(x, y, z);
        if (!(te instanceof TilePedestalElfArrow))
            return true;
        ItemStack hand = player.inventory.getCurrentItem();
        int count = ((TilePedestalElfArrow) te).onPlace(hand, player);
        if (count == -1)
            return true;
        if (count > 0) {
            player.inventory.getCurrentItem().stackSize -= count;
        }
        return true;
    }

    @Override
    public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase entity, ItemStack stack) {
        if (world.isRemote)
            return;
        Chunk chunk = world.getChunkFromChunkCoords(x >> 4, y >> 4);
        int count = ((Long)new ArrayList<TileEntity>(chunk.chunkTileEntityMap.values()).stream().filter(e -> e instanceof TilePedestalElfArrow).count()).intValue();
        if (count >= 9) {
            EntityItem ei = new EntityItem(world, (x + 0.5D), (y + 0.5D), (z + 0.5D), new ItemStack(this));
            world.spawnEntityInWorld(ei);
            world.setBlockToAir(x, y, z);
        }
    }
}
