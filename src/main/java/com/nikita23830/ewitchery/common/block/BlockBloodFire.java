package com.nikita23830.ewitchery.common.block;

import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.BlockFire;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

import java.util.Random;

public class BlockBloodFire extends BlockFire {
    IIcon icon;

    public BlockBloodFire() {
        GameRegistry.registerBlock(this, getClass().getSimpleName());
    }

    @Override
    public void registerBlockIcons(IIconRegister ir) {
        icon = ir.registerIcon("AdvanceWitchery:fire_layer_0");
    }

    @Override
    public IIcon getIcon(int p_149691_1_, int p_149691_2_) {
        return icon;
    }

    public boolean isOpaqueCube()
    {
        return false;
    }

    public boolean renderAsNormalBlock()
    {
        return false;
    }

    public AxisAlignedBB getCollisionBoundingBoxFromPool(World p_149668_1_, int p_149668_2_, int p_149668_3_, int p_149668_4_)
    {
        return null;
    }

    public int quantityDropped(Random p_149745_1_)
    {
        return 0;
    }

    public boolean func_149698_L()
    {
        return false;
    }

    public boolean isCollidable()
    {
        return false;
    }

    public void updateTick(World p_149674_1_, int p_149674_2_, int p_149674_3_, int p_149674_4_, Random p_149674_5_) {}

    public boolean canPlaceBlockAt(World p_149742_1_, int p_149742_2_, int p_149742_3_, int p_149742_4_) {
        return false;
    }

    public void onNeighborBlockChange(World p_149695_1_, int p_149695_2_, int p_149695_3_, int p_149695_4_, Block p_149695_5_) {}

    public void onBlockAdded(World p_149726_1_, int p_149726_2_, int p_149726_3_, int p_149726_4_) {}

    @Override
    public IIcon getFireIcon(int p_149840_1_) {
        return icon;
    }
}
