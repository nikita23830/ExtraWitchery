package com.nikita23830.ewitchery.client.renderEntity;

import com.emoniph.witchery.entity.EntityFollower;
import com.nikita23830.ewitchery.common.entity.EntityElly;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.entity.RenderBiped;
import net.minecraft.entity.EntityLiving;
import net.minecraft.util.ResourceLocation;

public class RenderElly extends RenderBiped {
    private final ModelBiped model;
    private static final ResourceLocation TEXTURE_ELLE = new ResourceLocation("witchery", "textures/entities/lilithfol1.png");

    public RenderElly(ModelBiped model) {
        this(model, 1.0F);
    }

    public RenderElly(ModelBiped model, float scale) {
        super(model, 0.5F, scale);
        this.model = model;
    }

    protected ResourceLocation getEntityTexture(EntityLiving entity) {
        return this.getEntityTexture((EntityElly)entity);
    }

    protected ResourceLocation getEntityTexture(EntityElly entity) {
        return TEXTURE_ELLE;
    }
}
