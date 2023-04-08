package com.nikita23830.ewitchery.client.renderTiles;

import com.nikita23830.ewitchery.AdvanceWitchery;
import com.nikita23830.ewitchery.common.tiles.TilePedestalElfArrow;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.AdvancedModelLoader;
import net.minecraftforge.client.model.IModelCustom;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

public class RenderTilePedestalElfArrow extends TileEntitySpecialRenderer {

    private static final ResourceLocation textures = new ResourceLocation(AdvanceWitchery.MODID.toLowerCase(), "textures/blocks/pedestalElfArrow.png"); // pedestalElfArrow
    private static final ResourceLocation textures0 = new ResourceLocation(AdvanceWitchery.MODID.toLowerCase(), "textures/blocks/pedestalElfArrow0.png");
    private static final IModelCustom model = AdvancedModelLoader.loadModel(new ResourceLocation(AdvanceWitchery.MODID.toLowerCase(), "textures/blocks/pedestalElfArrow.obj"));

    @Override
    public void renderTileEntityAt(TileEntity tileentity, double d0, double d1, double d2, float pticks) {
        ItemStack _stack = ((TilePedestalElfArrow)tileentity).getStackInSlot(0);
        if (_stack != null)
            renderItem(_stack.copy(), d0, d1, d2, pticks, tileentity, 4);
        GL11.glPushMatrix();
        GL11.glEnable(GL12.GL_RESCALE_NORMAL);
        GL11.glColor4f(1F, 1F, 1F, 1F);
        if (((TilePedestalElfArrow) tileentity).isBlood())
            Minecraft.getMinecraft().renderEngine.bindTexture(textures0);
        else
            Minecraft.getMinecraft().renderEngine.bindTexture(textures);
        GL11.glTranslated(d0 + 0.5, d1, d2 + 0.5);
        //GL11.glScalef(1F, -1F, -1F);
        //GL11.glRotatef(180F, 1F, 0F, 0F);
        model.renderAll();
        GL11.glScalef(1F, -1F, -1F);
        //GL11.glEnable(GL12.GL_RESCALE_NORMAL);
        GL11.glPopMatrix();
    }

    private void renderItem(ItemStack stack, double d0, double d1, double d2, float pticks, TileEntity tileEntity, int index) {
        EntityItem entityitem = null;
        float ticks = (float) Minecraft.getMinecraft().renderViewEntity.ticksExisted + pticks;
        GL11.glPushMatrix();
        float h = MathHelper.sin(ticks % 32767.0F / 16.0F) * 0.05F;

        switch (index) {
            case 0: {
                GL11.glTranslatef((float)d0 + 0.5F, (float)d1 + 1.20F + h, (float)d2 - 0.25F);
                break;
            }
            case 1: {
                GL11.glTranslatef((float)d0 + 0.5F, (float)d1 + 1.20F + h, (float)d2 + 1.25F);
                break;
            }
            case 2: {
                GL11.glTranslatef((float)d0 - 0.25F, (float)d1 + 1.20F + h, (float)d2 + 0.5F);
                break;
            }
            case 3: {
                GL11.glTranslatef((float)d0 + 1.25F, (float)d1 + 1.20F + h, (float)d2 + 0.5F);
                break;
            }
            case 4: {
                GL11.glTranslatef((float)d0 + 0.5F, (float)d1 + 0.25F + h, (float)d2 + 0.5F);
                break;
            }
        }

        GL11.glRotatef(ticks % 360.0F, 0.0F, 1.0F, 0.0F);
        float size = (stack.getItem() instanceof ItemBlock) ? (index != 4 ? 1.0F : 2.0F) : (index != 4 ? 0.5F : 1.0F);
        GL11.glScalef(size, size, size);
        stack.stackSize = 1;
        entityitem = new EntityItem(tileEntity.getWorldObj(), 0.0D, 0.0D, 0.0D, stack);
        entityitem.hoverStart = 0.0F;
        RenderManager.instance.renderEntityWithPosYaw(entityitem, 0.0D, 0.0D, 0.0D, 0.0F, 0.0F);
        if (!Minecraft.isFancyGraphicsEnabled()) {
            GL11.glRotatef(180.0F, 0.0F, 1.0F, 0.0F);
            RenderManager.instance.renderEntityWithPosYaw(entityitem, 0.0D, 0.0D, 0.0D, 0.0F, 0.0F);
        }
        GL11.glPopMatrix();
    }
}
