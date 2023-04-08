package com.nikita23830.ewitchery.common.block;

import com.emoniph.witchery.util.ParticleEffect;
import com.nikita23830.ewitchery.common.tiles.TileCirle;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;

import java.util.List;
import java.util.Random;

public class BlockCircle extends Block implements ITileEntityProvider {
    private IIcon[] icons = new IIcon[12];
    public static Random random = new Random();

    public BlockCircle() {
        super(Material.vine);
        this.setResistance(1000.0F);
        this.setHardness(2.0F);
        this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.015625F, 1.0F);
        GameRegistry.registerBlock(this, getClass().getSimpleName());
        GameRegistry.registerTileEntity(TileCirle.class, TileCirle.class.getSimpleName());
    }

    @Override
    public void registerBlockIcons(IIconRegister p_149651_1_) {
        for (int i = 0; i < 12; ++i) {
            icons[i] = p_149651_1_.registerIcon("AdvanceWitchery:circleglyph3." + (i + 1));
        }
    }

    @Override
    public IIcon getIcon(int p_149691_1_, int p_149691_2_) {
        return icons[random.nextInt(icons.length)];
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

    @SideOnly(Side.CLIENT)
    public void randomDisplayTick(World world, int x, int y, int z, Random rand) {
        double d0;
        double d1;
        double d2;
        d0 = (double)((float)x + 0.4F + rand.nextFloat() * 0.2F);
        d1 = (double)((float)y + 0.4F + rand.nextFloat() * 0.3F);
        d2 = (double)((float)z + 0.4F + rand.nextFloat() * 0.2F);
        world.spawnParticle(ParticleEffect.ENCHANTMENT_TABLE.toString(), d0, d1, d2, 0.0D, 0.0D, 0.0D);

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
    public TileEntity createNewTileEntity(World p_149915_1_, int p_149915_2_) {
        return new TileCirle();
    }

    @Override
    public boolean onBlockActivated(World w, int x, int y, int z, EntityPlayer ep, int p_149727_6_, float p_149727_7_, float p_149727_8_, float p_149727_9_) {
        if (w.isRemote)
            return true;
        TileEntity te = w.getTileEntity(x, y, z);
        if (te instanceof TileCirle && ((TileCirle) te).getStackInSlot(0) == null && ep.inventory.getCurrentItem() != null && ep instanceof EntityPlayerMP) {
            ItemStack stack = ep.inventory.getCurrentItem().copy();
            stack.stackSize = 1;
            --ep.inventory.getCurrentItem().stackSize;
            ((TileCirle) te).setInventorySlotContents(0, stack);
            ((EntityPlayerMP)ep).func_147097_b(te);
            return true;
        }
        return false;
    }

    @Override
    public void onBlockClicked(World w, int x, int y, int z, EntityPlayer ep) {
        if (w.isRemote) {
            super.onBlockClicked(w, x, y, z, ep);
            return;
        }
        TileEntity te = w.getTileEntity(x, y, z);
        if (te instanceof TileCirle && ((TileCirle) te).getStackInSlot(0) != null) {
            EntityItem ei = new EntityItem(w, ep.posX, ep.posY, ep.posZ, ((TileCirle) te).getStackInSlot(0));
            w.spawnEntityInWorld(ei);
            ((TileCirle) te).setInventorySlotContents(0, null);
            if (ep instanceof EntityPlayerMP)
                ((EntityPlayerMP)ep).func_147097_b(te);
        }
        super.onBlockClicked(w, x, y, z, ep);
    }
}
