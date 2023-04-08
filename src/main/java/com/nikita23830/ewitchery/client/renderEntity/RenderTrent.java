package com.nikita23830.ewitchery.client.renderEntity;

import com.nikita23830.ewitchery.AdvanceWitchery;
import com.nikita23830.ewitchery.client.models.ModelPhantom;
import com.nikita23830.ewitchery.client.models.ModelTrent;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;

public class RenderTrent extends RenderLiving {
    public static final ResourceLocation loc = new ResourceLocation(AdvanceWitchery.MODID.toLowerCase(), "textures/entity/trent.png");

    public RenderTrent() {
        super(new ModelTrent(), 0.25F);
    }


    @Override
    protected ResourceLocation getEntityTexture(Entity e) {
        return loc;
    }
}
