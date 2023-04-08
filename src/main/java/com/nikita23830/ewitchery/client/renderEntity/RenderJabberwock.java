package com.nikita23830.ewitchery.client.renderEntity;

import com.nikita23830.ewitchery.AdvanceWitchery;
import com.nikita23830.ewitchery.client.models.ModelJabberwock;
import com.nikita23830.ewitchery.client.models.ModelPhantom;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;

public class RenderJabberwock extends RenderLiving {
    public static final ResourceLocation loc = new ResourceLocation(AdvanceWitchery.MODID.toLowerCase(), "textures/entity/jabberwock.png");

    public RenderJabberwock() {
        super(new ModelJabberwock(), 0.25F);
    }


    @Override
    protected ResourceLocation getEntityTexture(Entity e) {
        return loc;
    }
}
