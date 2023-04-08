package com.nikita23830.ewitchery.client.models;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.MathHelper;

@SideOnly(Side.CLIENT)
public class ModelBehemoth extends ModelBipedCustom {
    public int heldItemLeft;
    public int heldItemRight;
    ModelRenderer mouth;
    ModelRenderer lefthorn01;
    ModelRenderer lefthorn02;
    ModelRenderer lefthorn03;
    ModelRenderer righthorn01;
    ModelRenderer righthorn02;
    ModelRenderer righthorn03;
    ModelRenderer leftshoulder;
    ModelRenderer rightshoulder;
    ModelRenderer leftlowerarm;
    ModelRenderer rightlowerarm;
    ModelRenderer leftlowerleg;
    ModelRenderer rightlowerleg;
    ModelRenderer leftfoot;
    ModelRenderer rightfoot;

    public ModelBehemoth() {
        this(1.0F);
    }

    public ModelBehemoth(float shadowSize) {
        this.heldItemLeft = 0;
        this.heldItemRight = 0;
        this.textureWidth = 128;
        this.textureHeight = 128;
        this.head = new ModelRenderer(this, 0, 0);
        this.head.addBox(-4.0F, -10.0F, -4.0F, 8, 10, 8);
        this.head.setRotationPoint(0.0F, -21.0F, 0.0F);
        this.head.setTextureSize(128, 128);
        this.setRotation(this.head, 0.0F, 0.0F, 0.0F);
        this.mouth = new ModelRenderer(this, 32, 0);
        this.head.addChild(this.mouth);
        this.mouth.addBox(-2.0F, -2.0F, -4.5F, 4, 3, 2);
        this.mouth.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.mouth.setTextureSize(128, 128);
        this.setRotation(this.mouth, 0.2094395F, 0.0F, 0.0F);
        this.lefthorn01 = new ModelRenderer(this, 32, 5);
        this.head.addChild(this.lefthorn01);
        this.lefthorn01.addBox(3.0F, -5.0F, -0.5F, 5, 5, 6);
        this.lefthorn01.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.lefthorn01.setTextureSize(128, 128);
        this.setRotation(this.lefthorn01, 0.1745329F, 0.3141593F, -0.4363323F);
        this.lefthorn02 = new ModelRenderer(this, 50, 0);
        this.head.addChild(this.lefthorn02);
        this.lefthorn02.addBox(-3.0F, -10.0F, 6.0F, 3, 7, 4);
        this.lefthorn02.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.lefthorn02.setTextureSize(128, 128);
        this.setRotation(this.lefthorn02, 1.204277F, -0.1396263F, 1.012291F);
        this.lefthorn03 = new ModelRenderer(this, 53, 0);
        this.head.addChild(this.lefthorn03);
        this.lefthorn03.addBox(3.5F, -7.5F, 11.0F, 2, 7, 2);
        this.lefthorn03.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.lefthorn03.setTextureSize(128, 128);
        this.setRotation(this.lefthorn03, 1.989675F, 0.0F, 0.5235988F);
        this.righthorn01 = new ModelRenderer(this, 32, 5);
        this.head.addChild(this.righthorn01);
        this.righthorn01.mirror = true;
        this.righthorn01.addBox(-8.0F, -5.0F, -0.5F, 5, 5, 6);
        this.righthorn01.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.righthorn01.setTextureSize(128, 128);
        this.setRotation(this.righthorn01, 0.1745329F, -0.3141593F, 0.4363323F);
        this.righthorn01.mirror = false;
        this.righthorn02 = new ModelRenderer(this, 50, 0);
        this.head.addChild(this.righthorn02);
        this.righthorn02.mirror = true;
        this.righthorn02.addBox(0.0F, -10.0F, 6.0F, 3, 7, 4);
        this.righthorn02.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.righthorn02.setTextureSize(128, 128);
        this.setRotation(this.righthorn02, 1.204277F, 0.1396263F, -1.012291F);
        this.righthorn02.mirror = false;
        this.righthorn03 = new ModelRenderer(this, 53, 0);
        this.head.addChild(this.righthorn03);
        this.righthorn03.mirror = true;
        this.righthorn03.addBox(-5.5F, -7.5F, 11.0F, 2, 7, 2);
        this.righthorn03.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.righthorn03.setTextureSize(128, 128);
        this.setRotation(this.righthorn03, 1.989675F, 0.0F, -0.5235988F);
        this.righthorn03.mirror = false;
        this.body = new ModelRenderer(this, 0, 18);
        this.body.addBox(-8.0F, 0.0F, -2.0F, 16, 24, 7);
        this.body.setRotationPoint(0.0F, -21.0F, 0.0F);
        this.body.setTextureSize(128, 128);
        this.setRotation(this.body, 0.0F, 0.0F, 0.0F);
        this.leftshoulder = new ModelRenderer(this, 46, 16);
        this.body.addChild(this.leftshoulder);
        this.leftshoulder.addBox(4.0F, -3.0F, -3.0F, 8, 8, 9);
        this.leftshoulder.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.leftshoulder.setTextureSize(128, 128);
        this.setRotation(this.leftshoulder, 0.0F, 0.0F, 0.2443461F);
        this.rightshoulder = new ModelRenderer(this, 46, 16);
        this.body.addChild(this.rightshoulder);
        this.rightshoulder.mirror = true;
        this.rightshoulder.addBox(-12.0F, -3.0F, -3.0F, 8, 8, 9);
        this.rightshoulder.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.rightshoulder.setTextureSize(128, 128);
        this.setRotation(this.rightshoulder, 0.0F, 0.0F, -0.2443461F);
        this.rightshoulder.mirror = false;
        this.leftarm = new ModelRenderer(this, 80, 0);
        this.leftarm.addBox(0.0F, -2.0F, -3.0F, 5, 13, 6);
        this.leftarm.setRotationPoint(8.0F, -16.0F, 1.0F);
        this.leftarm.setTextureSize(128, 128);
        this.setRotation(this.leftarm, (float)Math.toRadians(15.0), 0.0F, (float)Math.toRadians(-10.0));
        this.leftlowerarm = new ModelRenderer(this, 80, 19);
        this.leftarm.addChild(this.leftlowerarm);
        this.leftlowerarm.addBox(3.0F, 8.0F, 1.0F, 5, 14, 6);
        this.leftlowerarm.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.leftlowerarm.setTextureSize(128, 128);
        this.setRotation(this.leftlowerarm, (float)Math.toRadians(-30.0), 0.0F, (float)Math.toRadians(18.0));
        this.rightarm = new ModelRenderer(this, 80, 0);
        this.rightarm.mirror = true;
        this.rightarm.addBox(-5.0F, -2.0F, -3.0F, 5, 13, 6);
        this.rightarm.setRotationPoint(-8.0F, -16.0F, 1.0F);
        this.rightarm.setTextureSize(128, 128);
        this.setRotation(this.rightarm, (float)Math.toRadians(15.0), 0.0F, (float)Math.toRadians(10.0));
        this.rightarm.mirror = false;
        this.rightlowerarm = new ModelRenderer(this, 80, 19);
        this.rightarm.addChild(this.rightlowerarm);
        this.rightlowerarm.mirror = true;
        this.rightlowerarm.addBox(-8.0F, 8.0F, 1.0F, 5, 14, 6);
        this.rightlowerarm.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.rightlowerarm.setTextureSize(128, 128);
        this.setRotation(this.rightlowerarm, (float)Math.toRadians(-30.0), 0.0F, (float)Math.toRadians(-18.0));
        this.rightlowerarm.mirror = false;
        this.leftleg = new ModelRenderer(this, 0, 49);
        this.leftleg.addBox(-4.0F, -1.0F, -2.0F, 8, 13, 7);
        this.leftleg.setRotationPoint(4.0F, 3.0F, 1.0F);
        this.leftleg.setTextureSize(128, 128);
        this.setRotation(this.leftleg, (float)Math.toRadians(28.0), 0.0F, 0.0F);
        this.leftlowerleg = new ModelRenderer(this, 0, 69);
        this.leftleg.addChild(this.leftlowerleg);
        this.leftlowerleg.addBox(-3.9F, 3.0F, 5.5F, 8, 14, 7);
        this.leftlowerleg.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.leftlowerleg.setTextureSize(128, 128);
        this.setRotation(this.leftlowerleg, (float)Math.toRadians(-56.0), 0.0F, 0.0F);
        this.leftfoot = new ModelRenderer(this, 0, 90);
        this.leftleg.addChild(this.leftfoot);
        this.leftfoot.addBox(-4.0F, 14.0F, -3.0F, 9, 7, 10);
        this.leftfoot.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.leftfoot.setTextureSize(128, 128);
        this.setRotation(this.leftfoot, (float)Math.toRadians(-28.0), 0.0F, 0.0F);
        this.rightleg = new ModelRenderer(this, 0, 49);
        this.rightleg.mirror = true;
        this.rightleg.addBox(-4.0F, -1.0F, -2.0F, 8, 13, 7);
        this.rightleg.setRotationPoint(-4.0F, 3.0F, 1.0F);
        this.rightleg.setTextureSize(128, 128);
        this.setRotation(this.rightleg, (float)Math.toRadians(28.0), 0.0F, 0.0F);
        this.rightleg.mirror = false;
        this.rightlowerleg = new ModelRenderer(this, 0, 69);
        this.rightleg.addChild(this.rightlowerleg);
        this.rightlowerleg.mirror = true;
        this.rightlowerleg.addBox(-4.1F, 3.0F, 5.5F, 8, 14, 7);
        this.rightlowerleg.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.rightlowerleg.setTextureSize(128, 128);
        this.setRotation(this.rightlowerleg, (float)Math.toRadians(-56.0), 0.0F, 0.0F);
        this.rightlowerleg.mirror = false;
        this.rightfoot = new ModelRenderer(this, 0, 90);
        this.rightleg.addChild(this.rightfoot);
        this.rightfoot.mirror = true;
        this.rightfoot.addBox(-5.0F, 14.0F, -3.0F, 9, 7, 10);
        this.rightfoot.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.rightfoot.setTextureSize(128, 128);
        this.setRotation(this.rightfoot, (float)Math.toRadians(-28.0), 0.0F, 0.0F);
        this.rightfoot.mirror = false;
    }

    public void render(Entity entity, float time, float distance, float loop, float lookY, float lookX, float scale) {
        super.render(entity, time, distance, loop, lookY, lookX, scale);
    }

    public void setAngles(EntityLiving entity, float time, float distance, float loop, float lookY, float lookX, float scale) {
        super.setAngles(entity, time, distance, loop, lookY, lookX, scale);
    }

    public void animate(EntityLiving entity, float time, float distance, float loop, float lookY, float lookX, float scale) {
        super.animate(entity, time, distance, loop, lookY, lookX, scale);
        float pi = 3.1415927F;
        if (entity instanceof EntityLivingBase) {
            if (((EntityLivingBase)entity).getAITarget() != null) {
                float offsetArmHead = 0.0F;
                ModelRenderer var10000 = this.rightarm;
                var10000.rotateAngleY += -(0.1F - offsetArmHead * 0.6F) + this.head.rotateAngleY;
                var10000 = this.rightarm;
                var10000.rotateAngleX += -1.5707964F + this.head.rotateAngleX;
                var10000 = this.rightarm;
                var10000.rotateAngleZ += MathHelper.cos(loop * 0.09F) * 0.05F + 0.05F;
                var10000 = this.rightarm;
                var10000.rotateAngleX += MathHelper.sin(loop * 0.067F) * 0.05F;
            }

        }
    }
}
