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
public class ModelBehemoth2 extends ModelCustomObj {
    float maxLeg;
    private static IModelCustom modelObj = AdvancedModelLoader.loadModel(new ResourceLocation(AdvanceWitchery.MODID.toLowerCase(), "textures/entity/behemoth2.obj"));

    public ModelBehemoth2() {
        this(1.0F);
    }

    public ModelBehemoth2(float shadowSize) {
        this.maxLeg = 0.0F;
        this.model = (WavefrontObject) modelObj;
        this.parts = this.model.groupObjects;
        this.setPartCenter("head", 0.0F, 46.55F, 0.0F);
        this.setPartCenter("body", 0.0F, 22.05F, 0.0F);
        this.setPartCenter("armleft", 11.2F, 39.55F, 0.0F);
        this.setPartCenter("armright", -11.2F, 39.55F, 0.0F);
        this.setPartCenter("legleft", 4.9F, 22.4F, 0.0F);
        this.setPartCenter("legright", -4.9F, 22.4F, 0.0F);
        this.setPartCenter("tail", 0.0F, 37.8F, -9.45F);
        this.lockHeadX = false;
        this.lockHeadY = false;
        this.trophyScale = 0.1F;
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
        if (partName.equals("armleft")) {
            rotZ = (float)((double)rotZ - Math.toDegrees((double)(MathHelper.cos(loop * 0.09F) * 0.05F + 0.05F)));
            rotX = (float)((double)rotX - Math.toDegrees((double)(MathHelper.sin(loop * 0.067F) * 0.05F)));
        }

        if (partName.equals("armright")) {
            rotZ = (float)((double)rotZ + Math.toDegrees((double)(MathHelper.cos(loop * 0.09F) * 0.05F + 0.05F)));
            rotX = (float)((double)rotX + Math.toDegrees((double)(MathHelper.sin(loop * 0.067F) * 0.05F)));
        }

        if (partName.equals("tail")) {
            rotX = (float)(-Math.toDegrees((double)(MathHelper.cos(loop * 0.1F) * 0.05F - 0.05F)));
            rotY = (float)(-Math.toDegrees((double)(MathHelper.cos(loop * 0.09F) * 0.05F - 0.05F)));
        }

        float walkSwing = 0.05F;
        if (partName.equals("armleft")) {
            rotX = (float)((double)rotX + Math.toDegrees((double)(MathHelper.cos(time * walkSwing) * distance * 0.5F)));
        }

        if (partName.equals("armright")) {
            rotX = (float)((double)rotX + Math.toDegrees((double)(MathHelper.cos(time * walkSwing + 3.1415927F) * distance * 0.5F)));
        }

        if (partName.equals("legleft")) {
            rotX = (float)((double)rotX + Math.toDegrees((double)(MathHelper.cos(time * walkSwing + 3.1415927F) * distance)));
        }

        if (partName.equals("legright")) {
            rotX = (float)((double)rotX + Math.toDegrees((double)(MathHelper.cos(time * walkSwing) * distance)));
        }

        if (entity instanceof EntityLivingBase && ((EntityLivingBase)entity).getAITarget() != null) {
            if (partName.equals("armleft")) {
                this.rotate(-40.0F, 0.0F, 0.0F);
            }

            if (partName.equals("armright")) {
                this.rotate(-40.0F, 0.0F, 0.0F);
            }
        }

        this.rotate(rotation, angleX, angleY, angleZ);
        this.rotate(rotX, rotY, rotZ);
        this.translate(posX, posY, posZ);
    }
}
