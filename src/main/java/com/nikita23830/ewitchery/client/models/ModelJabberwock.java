package com.nikita23830.ewitchery.client.models;

import com.nikita23830.ewitchery.AdvanceWitchery;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.AdvancedModelLoader;
import net.minecraftforge.client.model.IModelCustom;
import net.minecraftforge.client.model.obj.WavefrontObject;

@SideOnly(Side.CLIENT)
public class ModelJabberwock extends ModelCustomObj {
    float maxLeg;
    private static IModelCustom modelObj = AdvancedModelLoader.loadModel(new ResourceLocation(AdvanceWitchery.MODID.toLowerCase(), "textures/entity/jabberwock.obj"));

    public ModelJabberwock() {
        this(1.0F);
    }

    public ModelJabberwock(float shadowSize) {
        this.maxLeg = 0.0F;
        this.model = (WavefrontObject)modelObj;
        this.parts = this.model.groupObjects;
        this.setPartCenter("head", 0.0F, 2.2F, 0.1F);
        this.setPartCenter("mouth", 0.0F, 2.25F, 0.8F);
        this.setPartCenter("body", 0.0F, 2.2F, 0.2F);
        this.setPartCenter("leftarm", 0.35F, 1.95F, 0.0F);
        this.setPartCenter("rightarm", -0.35F, 1.95F, 0.0F);
        this.setPartCenter("leftleg", 0.3F, 1.0F, -0.15F);
        this.setPartCenter("rightleg", -0.3F, 1.0F, -0.15F);
        this.trophyScale = 0.8F;
        this.trophyOffset = new float[]{0.0F, 0.0F, -0.4F};
    }

    public void animatePart(String partName, EntityLiving entity, float time, float distance, float loop, float lookY, float lookX, float scale) {
        super.animatePart(partName, entity, time, distance, loop, lookY, lookX, scale);
        float pi = 3.1415927F;
        float posX = 0.0F;
        float posY = 0.0F;
        float posZ = 0.0F;
        float angleX = 0.0F;
        float angleY = 0.0F;
        float angleZ = 0.0F;
        float rotation = 0.0F;
        float rotX = 0.0F;
        float rotY = 0.0F;
        float rotZ = 0.0F;
        if (partName.equals("mouth")) {
            this.centerPartToPart("mouth", "head");
            if (!this.lockHeadX) {
                this.rotate((float)Math.toDegrees((double)(lookX / 57.295776F)), 0.0F, 0.0F);
            }

            if (!this.lockHeadY) {
                this.rotate(0.0F, (float)Math.toDegrees((double)(lookY / 57.295776F)), 0.0F);
            }

            this.uncenterPartToPart("mouth", "head");
        }

        if (partName.equals("mouth")) {
            this.rotate((float)(-Math.toDegrees((double)(MathHelper.cos(loop * 0.09F) * 0.05F - 0.05F))), 0.0F, 0.0F);
        }

        if (partName.equals("leftarm")) {
            rotZ = (float)((double)rotZ - Math.toDegrees((double)(MathHelper.cos(loop * 0.09F) * 0.05F + 0.05F)));
            rotX = (float)((double)rotX - Math.toDegrees((double)(MathHelper.sin(loop * 0.067F) * 0.05F)));
        }

        if (partName.equals("rightarm")) {
            rotZ = (float)((double)rotZ + Math.toDegrees((double)(MathHelper.cos(loop * 0.09F) * 0.05F + 0.05F)));
            rotX = (float)((double)rotX + Math.toDegrees((double)(MathHelper.sin(loop * 0.067F) * 0.05F)));
        }

        if (partName.equals("tail")) {
            rotX = (float)(-Math.toDegrees((double)(MathHelper.cos(loop * 0.1F) * 0.05F - 0.05F)));
            rotY = (float)(-Math.toDegrees((double)(MathHelper.cos(loop * 0.09F) * 0.05F - 0.05F)));
        }

        if (entity == null || entity.onGround || entity.isInWater()) {
            float walkSwing = 0.6F;
            if (partName.equals("leftarm")) {
                rotX = (float)((double)rotX + Math.toDegrees((double)(MathHelper.cos(time * walkSwing) * 1.0F * distance * 0.5F)));
                rotZ = (float)((double)rotZ - Math.toDegrees((double)(MathHelper.cos(time * walkSwing) * 0.5F * distance * 0.5F)));
            }

            if (partName.equals("rightarm")) {
                rotX = (float)((double)rotX + Math.toDegrees((double)(MathHelper.cos(time * walkSwing + 3.1415927F) * 1.0F * distance * 0.5F)));
                rotZ = (float)((double)rotZ + Math.toDegrees((double)(MathHelper.cos(time * walkSwing + 3.1415927F) * 0.5F * distance * 0.5F)));
            }

            if (partName.equals("leftleg")) {
                rotX = (float)((double)rotX + Math.toDegrees((double)(MathHelper.cos(time * walkSwing + 3.1415927F) * 1.4F * distance)));
            }

            if (partName.equals("rightleg")) {
                rotX = (float)((double)rotX + Math.toDegrees((double)(MathHelper.cos(time * walkSwing) * 1.4F * distance)));
            }
        }

        if (entity instanceof EntityLivingBase && ((EntityLivingBase)entity).getAITarget() != null) {
            if (partName.equals("leftarm") || partName.equals("rightarm")) {
                rotX += 20.0F;
            }

            if (partName.equals("mouth")) {
                rotX += 20.0F;
            }
        }

        if (entity != null && !entity.onGround && !entity.isInWater()) {
            if (partName.equals("leftarm")) {
                rotZ -= 10.0F;
                rotX -= 50.0F;
            }

            if (partName.equals("rightarm")) {
                rotZ += 10.0F;
                rotX -= 50.0F;
            }

            if (partName.equals("leftleg")) {
                rotX += 50.0F;
            }

            if (partName.equals("rightleg")) {
                rotX += 50.0F;
            }
        }

        this.rotate(rotation, angleX, angleY, angleZ);
        this.rotate(rotX, rotY, rotZ);
        this.translate(posX, posY, posZ);
    }
}
