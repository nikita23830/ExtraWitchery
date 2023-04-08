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
public class ModelEnt extends ModelCustomObj {
    float maxLeg;
    private static IModelCustom modelObj = AdvancedModelLoader.loadModel(new ResourceLocation(AdvanceWitchery.MODID.toLowerCase(), "textures/entity/ent.obj"));


    public ModelEnt() {
        this(1.0F);
    }

    public ModelEnt(float shadowSize) {
        this.maxLeg = 0.0F;
        this.model = (WavefrontObject)modelObj;
        this.parts = this.model.groupObjects;
        this.setPartCenter("head", 0.0F, 1.2F, 0.3F);
        this.setPartCenter("body", 0.0F, 1.2F, 0.3F);
        this.setPartCenter("leftarm", 0.3F, 1.1F, 0.0F);
        this.setPartCenter("rightarm", -0.3F, 1.1F, 0.0F);
        this.setPartCenter("frontmiddleleg", 0.0F, 0.3F, 0.3F);
        this.setPartCenter("frontleftleg", 0.3F, 0.3F, 0.15F);
        this.setPartCenter("frontrightleg", -0.3F, 0.3F, 0.15F);
        this.setPartCenter("backmiddleleg", 0.0F, 0.3F, -0.3F);
        this.setPartCenter("backleftleg", 0.3F, 0.3F, -0.15F);
        this.setPartCenter("backrightleg", -0.3F, 0.3F, -0.15F);
        this.lockHeadX = true;
        this.lockHeadY = true;
        this.trophyScale = 1.0F;
        this.trophyOffset = new float[]{0.0F, 0.0F, -0.3F};
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

        if (partName.equals("frontleftleg") || partName.equals("backleftleg") || partName.equals("frontrightleg") || partName.equals("backrightleg") || partName.equals("frontmiddleleg") || partName.equals("backmiddleleg")) {
            angleY = 1.0F;
        }

        float walkSwing = 0.6F;
        if (partName.equals("frontrightleg") || partName.equals("frontleftleg") || partName.equals("backleftleg") || partName.equals("backrightleg")) {
            rotation = (float)((double)rotation + Math.toDegrees((double)(MathHelper.cos(time * 0.6662F + 3.1415927F) * walkSwing * distance)));
        }

        if (partName.equals("frontmiddleleg") || partName.equals("backmiddleleg")) {
            rotation = (float)((double)rotation + Math.toDegrees((double)(MathHelper.cos(time * 0.6662F) * walkSwing * distance)));
        }

        if (entity instanceof EntityLivingBase && ((EntityLivingBase)entity).getAITarget() != null) {
            if (partName.equals("leftarm")) {
                this.rotate(0.0F, -25.0F, 0.0F);
            }

            if (partName.equals("rightarm")) {
                this.rotate(0.0F, 25.0F, 0.0F);
            }
        }

        this.rotate(rotation, angleX, angleY, angleZ);
        this.rotate(rotX, rotY, rotZ);
        this.translate(posX, posY, posZ);
    }
}

