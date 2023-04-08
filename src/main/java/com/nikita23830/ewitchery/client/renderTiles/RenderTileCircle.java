package com.nikita23830.ewitchery.client.renderTiles;

import com.nikita23830.ewitchery.common.tiles.TileCirle;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import org.lwjgl.opengl.GL11;

public class RenderTileCircle extends TileEntitySpecialRenderer {
    RenderItem renderer = new RenderItem();

    public RenderTileCircle() {
        this.renderer.setRenderManager(RenderManager.instance);
    }

    public void renderTileEntityAt(TileEntity tileentity, double x, double y, double z, float f) {
        TileCirle machine = (TileCirle) tileentity;
        if (machine != null && machine.getBlockType() != null) {
            GL11.glPushMatrix();
            GL11.glTranslatef((float)x + 0.6F, (float)y + 0.35F, (float)z + 0.6F);
            GL11.glEnable(32826);
            int A = 0;
            ItemStack st = machine.getStackInSlot(0);
            if (st != null) {
                EntityItem entity = new EntityItem((World)null, x, y, z, st);
                entity.hoverStart = 0.0F;
                if (Minecraft.getMinecraft() != null && Minecraft.getMinecraft().thePlayer != null) {
                    entity.age = Minecraft.getMinecraft().thePlayer.ticksExisted;
                }
                try {
                    this.renderer.doRender(entity, (-0.1D + (double)(A % 2) * 0.5D), 0.0D, -0.1D + (double) 0 * 0.5D, f, f);
                } catch (Exception e) {
                    // NO-OP
                }
            }

            GL11.glDisable(32826);
            GL11.glPopMatrix();
        }

    }
}
