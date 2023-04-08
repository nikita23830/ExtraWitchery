package com.nikita23830.ewitchery.common.block;

import com.emoniph.witchery.util.ParticleEffect;
import com.nikita23830.ewitchery.AdvanceWitchery;
import com.nikita23830.ewitchery.common.tiles.TileBloodCircle;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.IIcon;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;

import java.util.List;
import java.util.Random;

public class BlockHeartCircle extends Block implements ITileEntityProvider {
    private IIcon icons;
    public static Random random = new Random();

    public BlockHeartCircle() {
        super(Material.vine);
        this.setResistance(1000.0F);
        this.setHardness(2.0F);
        this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.015625F, 1.0F);
        GameRegistry.registerBlock(this, getClass().getSimpleName());
        GameRegistry.registerTileEntity(TileBloodCircle.class, AdvanceWitchery.class.getSimpleName() + ":" + TileBloodCircle.class.getSimpleName());
    }

    @Override
    public TileEntity createNewTileEntity(World p_149915_1_, int p_149915_2_) {
        return new TileBloodCircle();
    }

    @Override
    public void registerBlockIcons(IIconRegister p_149651_1_) {
        icons = p_149651_1_.registerIcon("AdvanceWitchery:heartGlyph");
    }

    @Override
    public IIcon getIcon(int p_149691_1_, int p_149691_2_) {
        return icons;
    }

    public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int x, int y, int z) {
        return null;
    }

    public boolean isOpaqueCube() {
        return false;
    }

    public boolean renderAsNormalBlock() {
        return false;
    }

    public int quantityDropped(Random rand) {
        return 0;
    }

    @Override
    public ItemStack getPickBlock(MovingObjectPosition target, World world, int x, int y, int z, EntityPlayer player) {
        return null;
    }

    @Override
    public Item getItemDropped(int p_149650_1_, Random p_149650_2_, int p_149650_3_) {
        return null;
    }

    @Override
    public boolean onBlockActivated(World w, int x, int y, int z, EntityPlayer ep, int p_149727_6_, float p_149727_7_, float p_149727_8_, float p_149727_9_) {
        TileEntity te = w.getTileEntity(x, y, z);
        if (te instanceof TileBloodCircle && !w.isRemote)
            ((TileBloodCircle) te).onClick(ep);
        return true;
    }
}
