package com.nikita23830.ewitchery.client.renderEntity;

import com.nikita23830.ewitchery.AdvanceWitchery;
import com.nikita23830.ewitchery.client.models.ModelBehemoth;
import com.nikita23830.ewitchery.client.models.ModelBehemoth2;
import com.nikita23830.ewitchery.client.models.ModelPhantom;
import com.nikita23830.ewitchery.common.entity.EntityBehemoth;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;

public class RenderPhantom extends RenderLiving {
    public static final ResourceLocation loc = new ResourceLocation(AdvanceWitchery.MODID.toLowerCase(), "textures/entity/phantom.png");

    public RenderPhantom() {
        super(new ModelPhantom(), 0.25F);
    }


    @Override
    protected ResourceLocation getEntityTexture(Entity e) {
        return loc;
    }
}
