package com.nikita23830.ewitchery.client.renderEntity;

import com.nikita23830.ewitchery.AdvanceWitchery;
import com.nikita23830.ewitchery.client.models.ModelBehemoth;
import com.nikita23830.ewitchery.client.models.ModelBehemoth2;
import com.nikita23830.ewitchery.common.entity.EntityBehemoth;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;

public class RenderBehemoth extends RenderLiving {
    public static final ResourceLocation loc = new ResourceLocation(AdvanceWitchery.MODID.toLowerCase(), "textures/entity/behemoth.png");
    public static final ResourceLocation loc2 = new ResourceLocation(AdvanceWitchery.MODID.toLowerCase(), "textures/entity/behemoth2.png");
    private final ModelBehemoth model0;
    private final ModelBehemoth2 model1 = new ModelBehemoth2();

    public RenderBehemoth() {
        super(new ModelBehemoth(), 0.25F);
        model0 = (ModelBehemoth) mainModel;
    }


    @Override
    protected ResourceLocation getEntityTexture(Entity e) {
        if (e instanceof EntityBehemoth && ((EntityBehemoth) e).transform)
            return loc2;
        return loc;
    }

    @Override
    public void doRender(EntityLivingBase e, double px, double py, double pz, float p_76986_8_, float p_76986_9_) {
        if (e instanceof EntityBehemoth && ((EntityBehemoth) e).transform)
            this.mainModel = model1;
        else
            this.mainModel = model0;
        super.doRender(e, px, py, pz, p_76986_8_, p_76986_9_);
    }
}
