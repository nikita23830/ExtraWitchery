package com.nikita23830.ewitchery.client.renderEntity;

import com.nikita23830.ewitchery.AdvanceWitchery;
import com.nikita23830.ewitchery.client.models.ModelEnt;
import com.nikita23830.ewitchery.client.models.ModelPhantom;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;

public class RenderEnt extends RenderLiving {
    public static final ResourceLocation loc = new ResourceLocation(AdvanceWitchery.MODID.toLowerCase(), "textures/entity/ent.png");

    public RenderEnt() {
        super(new ModelEnt(), 0.25F);
    }


    @Override
    protected ResourceLocation getEntityTexture(Entity e) {
        return loc;
    }
}
