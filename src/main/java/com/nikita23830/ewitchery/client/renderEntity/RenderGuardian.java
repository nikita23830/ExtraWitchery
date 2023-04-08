package com.nikita23830.ewitchery.client.renderEntity;

import com.nikita23830.ewitchery.AdvanceWitchery;
import com.nikita23830.ewitchery.client.models.ModelGuardian;
import com.nikita23830.ewitchery.common.entity.EntityGuardian;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;

public class RenderGuardian extends RenderLiving {
    public static final ResourceLocation loc = new ResourceLocation(AdvanceWitchery.MODID.toLowerCase(), "textures/entity/guardian.png");

    protected ResourceLocation getEntityTexture(Entity entity) {
        return loc;
    }

    public RenderGuardian() {
        super(new ModelGuardian(), 0.5F);
    }

    protected void rotateAsgardCorpse(EntityGuardian entity, float par2, float par3, float par4) {
        super.rotateCorpse(entity, par2, par3, par4);
    }

    public void doRender(EntityLiving entity, double par2, double par4, double par6, float par8, float par9) {
        this.doRenderAsgard((EntityGuardian) entity, par2, par4, par6, par8, par9);
    }

    protected void rotateCorpse(EntityLivingBase par1EntityLivingBase, float par2, float par3, float par4) {
        this.rotateAsgardCorpse((EntityGuardian)par1EntityLivingBase, par2, par3, par4);
    }

    public void doRender(EntityLivingBase par1, double par2, double par4, double par6, float par8, float par9) {
        this.doRenderAsgard((EntityGuardian)par1, par2, par4, par6, par8, par9);
    }

    public void doRender(Entity entity, double par2, double par4, double par6, float par8, float par9) {
        this.doRenderAsgard((EntityGuardian)entity, par2, par4, par6, par8, par9);
    }

    public void doRenderAsgard(EntityGuardian par1Entity, double par2, double par4, double par6, float par8, float par9) {

        super.doRender(par1Entity, par2, par4, par6, par8, par9);
    }
}
