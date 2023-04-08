package com.nikita23830.ewitchery.client.renderEntity;

import alfheim.client.model.entity.ModelEntityElf;
import com.nikita23830.ewitchery.AdvanceWitchery;
import com.nikita23830.ewitchery.client.models.ModelDarkElf;
import com.nikita23830.ewitchery.common.entity.EntityDarkElf;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;

public class RenderDarkElf extends RenderLiving {
    public static final ResourceLocation loc = new ResourceLocation(AdvanceWitchery.MODID.toLowerCase(), "textures/entity/DarkElf.png");

    protected ResourceLocation getEntityTexture(Entity entity) {
        return loc;
    }

    public RenderDarkElf() {
        super(new ModelEntityElf(), 0.5F);
    }

    protected void rotateAsgardCorpse(EntityDarkElf entity, float par2, float par3, float par4) {
        super.rotateCorpse(entity, par2, par3, par4);
    }

    public void doRender(EntityLiving entity, double par2, double par4, double par6, float par8, float par9) {
        this.doRenderAsgard((EntityDarkElf) entity, par2, par4, par6, par8, par9);
    }

    protected void rotateCorpse(EntityLivingBase par1EntityLivingBase, float par2, float par3, float par4) {
        this.rotateAsgardCorpse((EntityDarkElf)par1EntityLivingBase, par2, par3, par4);
    }

    public void doRender(EntityLivingBase par1, double par2, double par4, double par6, float par8, float par9) {
        this.doRenderAsgard((EntityDarkElf)par1, par2, par4, par6, par8, par9);
    }

    public void doRender(Entity entity, double par2, double par4, double par6, float par8, float par9) {
        this.doRenderAsgard((EntityDarkElf)entity, par2, par4, par6, par8, par9);
    }

    public void doRenderAsgard(EntityDarkElf par1Entity, double par2, double par4, double par6, float par8, float par9) {
        super.doRender(par1Entity, par2, par4, par6, par8, par9);
    }
}
