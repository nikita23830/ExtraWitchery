package com.nikita23830.ewitchery.client.renderBlock;

import com.meteor.extrabotany.common.block.tile.TileAutoPlate;
import com.nikita23830.ewitchery.client.ClientProxy;
import com.nikita23830.ewitchery.common.tiles.TilePedestalElfArrow;
import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.world.IBlockAccess;
import org.lwjgl.opengl.GL11;

public class RenderBlockPedestalElfArrow implements ISimpleBlockRenderingHandler {

    @Override
    public void renderInventoryBlock(Block block, int metadata, int modelId, RenderBlocks renderer) {
        GL11.glPushMatrix();
        GL11.glTranslatef(-0.5F, -0.6F, -0.5F);
        GL11.glScalef(0.9F, 0.9F, 0.9F);
        TileEntityRendererDispatcher.instance.renderTileEntityAt(new TilePedestalElfArrow(), 0.0D, 0.0D, 0.0D, 0.0F);
        GL11.glPopMatrix();
    }

    @Override
    public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks renderer) {
        return false;
    }

    @Override
    public boolean shouldRender3DInInventory(int modelId) {
        return true;
    }

    @Override
    public int getRenderId() {
        return ClientProxy.renderPedestalElfArrow;
    }
}
