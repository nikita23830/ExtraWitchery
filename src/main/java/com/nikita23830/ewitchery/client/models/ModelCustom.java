package com.nikita23830.ewitchery.client.models;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class ModelCustom extends ModelBase {
    public Map<ModelRenderer, float[]> initRotations;

    public ModelCustom() {
        this(1.0F);
    }

    public ModelCustom(float shadowSize) {
        this.initRotations = new HashMap();
        this.textureWidth = 128;
        this.textureHeight = 128;
    }

    public void setRotation(ModelRenderer model, float x, float y, float z) {
        model.rotateAngleX = x;
        model.rotateAngleY = y;
        model.rotateAngleZ = z;
        if (!this.initRotations.containsKey(model)) {
            this.initRotations.put(model, new float[]{x, y, z});
        }

    }

    public void render(Entity entity, float time, float distance, float loop, float lookY, float lookX, float scale) {
        float sizeScale = 1.0F;

        GL11.glScalef(sizeScale, sizeScale, sizeScale);
        GL11.glTranslatef(0.0F, -(sizeScale - 1.0F) * 2.0F, 0.0F);
        this.setAngles((EntityLiving)entity, time, distance, loop, lookY, lookX, scale);
        this.animate((EntityLiving)entity, time, distance, loop, lookY, lookX, scale);
    }

    public void setAngles(EntityLiving entity, float time, float distance, float loop, float lookY, float lookX, float scale) {
        Iterator var8 = this.initRotations.entrySet().iterator();

        while(var8.hasNext()) {
            Map.Entry<ModelRenderer, float[]> initRotation = (Map.Entry)var8.next();
            float[] rotations = (float[])initRotation.getValue();
            this.setRotation((ModelRenderer)initRotation.getKey(), rotations[0], rotations[1], rotations[2]);
        }

    }

    public void animate(EntityLiving entity, float time, float distance, float loop, float lookY, float lookX, float scale) {
    }
}
