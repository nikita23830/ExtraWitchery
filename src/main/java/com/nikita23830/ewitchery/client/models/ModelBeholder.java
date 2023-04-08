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
public class ModelBeholder extends ModelCustomObj {
    float maxLeg;
    private static IModelCustom modelObj = AdvancedModelLoader.loadModel(new ResourceLocation(AdvanceWitchery.MODID.toLowerCase(), "textures/entity/beholder.obj"));

    public ModelBeholder() {
        this(1.0F);
    }

    public ModelBeholder(float shadowSize) {
        this.maxLeg = 0.0F;
        this.model = (WavefrontObject)modelObj;
        this.parts = this.model.groupObjects;
        this.setPartCenter("head", 0.0F, 2.0F, 0.0F);
        this.setPartCenter("mouth", 0.0F, 1.0F, -0.5F);
        this.setPartCenter("eye", 0.0F, 2.6F, 1.3F);
        this.setPartCenter("tentacleleft01", 1.6F, 2.7F, -0.3F);
        this.setPartCenter("tentacleleft02", 0.6F, 3.6F, -0.4F);
        this.setPartCenter("tentacleleft03", 1.2F, 3.1F, -0.5F);
        this.setPartCenter("tentacleleft04", 1.4F, 2.6F, -0.6F);
        this.setPartCenter("tentacleleft05", 0.9F, 3.0F, -0.9F);
        this.setPartCenter("tentacleright01", -1.6F, 2.7F, -0.3F);
        this.setPartCenter("tentacleright02", -0.6F, 3.6F, -0.4F);
        this.setPartCenter("tentacleright03", -1.2F, 3.1F, -0.5F);
        this.setPartCenter("tentacleright04", -1.4F, 2.6F, -0.6F);
        this.setPartCenter("tentacleright05", -0.9F, 3.0F, -0.9F);
        this.lockHeadX = true;
        this.lockHeadY = true;
        this.trophyScale = 0.2F;
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
        this.centerPartToPart(partName, "head");
        rotZ = (float)((double)rotZ - Math.toDegrees((double)(MathHelper.cos(loop * 0.09F) * 0.015F + 0.015F)));
        this.uncenterPartToPart(partName, "head");
        if (partName.equals("eye")) {
            rotX = (float)((double)rotX + Math.toDegrees((double)(lookX / 57.295776F)));
            rotY = (float)((double)rotY + Math.toDegrees((double)(lookY / 57.295776F)));
        }

        if (partName.equals("mouth")) {
            rotX = MathHelper.cos(loop * 0.09F) * 0.05F - 0.05F;
        }

        float bob = -MathHelper.sin(loop * 0.05F) * 0.3F;
        posY += bob;
        float animationScaleZ = 0.09F;
        float animationScaleY = 0.07F;
        float animationScaleX = 0.05F;
        float animationDistanceZ = 0.25F;
        float animationDistanceY = 0.2F;
        float animationDistanceX = 0.15F;
        if (partName.equals("tentacleleft01")) {
            rotZ = (float)((double)rotZ - Math.toDegrees((double)(MathHelper.cos(loop * animationScaleZ) * animationDistanceZ + animationDistanceZ)));
            rotY = (float)((double)rotY + Math.toDegrees((double)(MathHelper.cos(loop * animationScaleY) * animationDistanceY + animationDistanceY)));
            rotX = (float)((double)rotX - Math.toDegrees((double)(MathHelper.sin(loop * animationScaleX) * animationDistanceX)));
        }

        if (partName.equals("tentacleleft02")) {
            rotZ = (float)((double)rotZ - Math.toDegrees((double)(MathHelper.cos(loop * animationScaleZ) * animationDistanceZ + animationDistanceZ)));
            rotY = (float)((double)rotY + Math.toDegrees((double)(MathHelper.cos(loop * animationScaleY) * animationDistanceY + animationDistanceY)));
            rotX = (float)((double)rotX + Math.toDegrees((double)(MathHelper.sin(loop * animationScaleX) * animationDistanceX)));
        }

        if (partName.equals("tentacleleft03")) {
            rotZ = (float)((double)rotZ + Math.toDegrees((double)(MathHelper.cos(loop * animationScaleZ) * animationDistanceZ + animationDistanceZ)));
            rotY = (float)((double)rotY + Math.toDegrees((double)(MathHelper.cos(loop * animationScaleY) * animationDistanceY + animationDistanceY)));
            rotX = (float)((double)rotX - Math.toDegrees((double)(MathHelper.sin(loop * animationScaleX) * animationDistanceX)));
        }

        if (partName.equals("tentacleleft04")) {
            rotZ = (float)((double)rotZ + Math.toDegrees((double)(MathHelper.cos(loop * animationScaleZ) * animationDistanceZ + animationDistanceZ)));
            rotY = (float)((double)rotY - Math.toDegrees((double)(MathHelper.cos(loop * animationScaleY) * animationDistanceY + animationDistanceY)));
            rotX = (float)((double)rotX + Math.toDegrees((double)(MathHelper.sin(loop * animationScaleX) * animationDistanceX)));
        }

        if (partName.equals("tentacleleft05")) {
            rotZ = (float)((double)rotZ - Math.toDegrees((double)(MathHelper.cos(loop * animationScaleZ) * animationDistanceZ + animationDistanceZ)));
            rotY = (float)((double)rotY - Math.toDegrees((double)(MathHelper.cos(loop * animationScaleY) * animationDistanceY + animationDistanceY)));
            rotX = (float)((double)rotX - Math.toDegrees((double)(MathHelper.sin(loop * animationScaleX) * animationDistanceX)));
        }

        if (partName.equals("tentacleright01")) {
            rotZ = (float)((double)rotZ - Math.toDegrees((double)(MathHelper.cos(loop * animationScaleZ) * animationDistanceZ + animationDistanceZ)));
            rotY = (float)((double)rotY + Math.toDegrees((double)(MathHelper.cos(loop * animationScaleY) * animationDistanceY + animationDistanceY)));
            rotX = (float)((double)rotX + Math.toDegrees((double)(MathHelper.sin(loop * animationScaleX) * animationDistanceX)));
        }

        if (partName.equals("tentacleright02")) {
            rotZ = (float)((double)rotZ + Math.toDegrees((double)(MathHelper.cos(loop * animationScaleZ) * animationDistanceZ + animationDistanceZ)));
            rotY = (float)((double)rotY + Math.toDegrees((double)(MathHelper.cos(loop * animationScaleY) * animationDistanceY + animationDistanceY)));
            rotX = (float)((double)rotX + Math.toDegrees((double)(MathHelper.sin(loop * animationScaleX) * animationDistanceX)));
        }

        if (partName.equals("tentacleright03")) {
            rotZ = (float)((double)rotZ - Math.toDegrees((double)(MathHelper.cos(loop * animationScaleZ) * animationDistanceZ + animationDistanceZ)));
            rotY = (float)((double)rotY + Math.toDegrees((double)(MathHelper.cos(loop * animationScaleY) * animationDistanceY + animationDistanceY)));
            rotX = (float)((double)rotX - Math.toDegrees((double)(MathHelper.sin(loop * animationScaleX) * animationDistanceX)));
        }

        if (partName.equals("tentacleright04")) {
            rotZ = (float)((double)rotZ + Math.toDegrees((double)(MathHelper.cos(loop * animationScaleZ) * animationDistanceZ + animationDistanceZ)));
            rotY = (float)((double)rotY - Math.toDegrees((double)(MathHelper.cos(loop * animationScaleY) * animationDistanceY + animationDistanceY)));
            rotX = (float)((double)rotX - Math.toDegrees((double)(MathHelper.sin(loop * animationScaleX) * animationDistanceX)));
        }

        if (partName.equals("tentacleright05")) {
            rotZ = (float)((double)rotZ - Math.toDegrees((double)(MathHelper.cos(loop * animationScaleZ) * animationDistanceZ + animationDistanceZ)));
            rotY = (float)((double)rotY - Math.toDegrees((double)(MathHelper.cos(loop * animationScaleY) * animationDistanceY + animationDistanceY)));
            rotX = (float)((double)rotX - Math.toDegrees((double)(MathHelper.sin(loop * animationScaleX) * animationDistanceX)));
        }

        if (entity instanceof EntityLivingBase && ((EntityLivingBase)entity).getAITarget() != null) {
            if (partName.equals("mouth")) {
                rotX += 20.0F;
            }

            if (partName.contains("tentacleleft")) {
                rotZ -= 25.0F;
            }

            if (partName.contains("tentacleright")) {
                rotZ += 25.0F;
            }
        }

        this.rotate(rotation, angleX, angleY, angleZ);
        this.rotate(rotX, rotY, rotZ);
        this.translate(posX, posY, posZ);
    }
}
