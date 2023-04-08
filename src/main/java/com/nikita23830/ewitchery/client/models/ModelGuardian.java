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
public class ModelGuardian extends ModelCustomObj {
    float maxLeg;
    private static IModelCustom modelObj = AdvancedModelLoader.loadModel(new ResourceLocation(AdvanceWitchery.MODID.toLowerCase(), "textures/entity/guardian.obj"));

    public ModelGuardian() {
        this(1.0F);
    }

    public ModelGuardian(float shadowSize) {
        this.maxLeg = 0.0F;
        this.model = (WavefrontObject)modelObj;
        this.parts = this.model.groupObjects;
        this.setPartCenter("lefthead", 0.35F, 3.1F, 0.0F);
        this.setPartCenter("righthead", -0.35F, 3.1F, 0.0F);
        this.setPartCenter("body", 0.0F, 3.1F, 0.0F);
        this.setPartCenter("leftarm", 0.8F, 2.75F, 0.0F);
        this.setPartCenter("rightarm", -0.8F, 2.75F, 0.0F);
        this.setPartCenter("leftleg", 0.35F, 1.25F, 0.0F);
        this.setPartCenter("rightleg", -0.35F, 1.25F, 0.0F);
        this.trophyScale = 1.0F;
        this.trophyOffset = new float[]{0.0F, -0.2F, -0.2F};
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
        if (partName.equals("leftarm")) {
            rotZ = (float)((double)rotZ - Math.toDegrees((double)(MathHelper.cos(loop * 0.09F) * 0.05F + 0.05F)));
            rotX = (float)((double)rotX - Math.toDegrees((double)(MathHelper.sin(loop * 0.067F) * 0.05F)));
        }

        if (partName.equals("rightarm")) {
            rotZ = (float)((double)rotZ + Math.toDegrees((double)(MathHelper.cos(loop * 0.09F) * 0.05F + 0.05F)));
            rotX = (float)((double)rotX + Math.toDegrees((double)(MathHelper.sin(loop * 0.067F) * 0.05F)));
        }

        float walkSwing = 0.6F;
        if (partName.equals("leftarm")) {
            rotX = (float)((double)rotX + Math.toDegrees((double)(MathHelper.cos(time * walkSwing) * 2.0F * distance * 0.5F)));
        }

        if (partName.equals("rightarm")) {
            rotX = (float)((double)rotX + Math.toDegrees((double)(MathHelper.cos(time * walkSwing + 3.1415927F) * 2.0F * distance * 0.5F)));
        }

        if (partName.equals("leftleg")) {
            rotX = (float)((double)rotX + Math.toDegrees((double)(MathHelper.cos(time * walkSwing + 3.1415927F) * 1.4F * distance)));
        }

        if (partName.equals("rightleg")) {
            rotX = (float)((double)rotX + Math.toDegrees((double)(MathHelper.cos(time * walkSwing) * 1.4F * distance)));
        }

        if (entity instanceof EntityLivingBase && ((EntityLivingBase)entity).getAITarget() != null) {
            if (partName.equals("leftarm")) {
                if (((EntityLivingBase)entity).attackTime >= 5) {
                    rotX += 30.0F;
                } else {
                    rotX -= 30.0F;
                }
            }

            if (partName.equals("rightarm")) {
                if (((EntityLivingBase)entity).attackTime >= 5) {
                    rotX -= 30.0F;
                } else {
                    rotX += 30.0F;
                }
            }
        }

        this.rotate(rotation, angleX, angleY, angleZ);
        this.rotate(rotX, rotY, rotZ);
        this.translate(posX, posY, posZ);
    }
}

