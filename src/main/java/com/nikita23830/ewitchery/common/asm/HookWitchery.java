package com.nikita23830.ewitchery.common.asm;

import com.emoniph.witchery.util.Config;
import com.emoniph.witchery.util.CreatureUtil;
import com.nikita23830.ewitchery.common.events.CheckSunEvent;
import gloomyfolken.hooklib.asm.Hook;
import gloomyfolken.hooklib.asm.ReturnCondition;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraftforge.common.MinecraftForge;

public class HookWitchery {

    @Hook(returnCondition = ReturnCondition.ALWAYS, createMethod=false, isMandatory = true)
    public static boolean isInSunlight(CreatureUtil util, EntityLivingBase entity) {
        CheckSunEvent event = new CheckSunEvent(util, entity);
        MinecraftForge.EVENT_BUS.post(event);
        if (event.hasResult())
            return event.isResult();
        World world = entity.worldObj;
        if (world.provider.dimensionId != Config.instance().dimensionDreamID && world.provider.dimensionId != Config.instance().dimensionTormentID && !world.provider.hasNoSky && world.provider.isSurfaceWorld() && world.isDaytime()) {
            int x = MathHelper.floor_double(entity.posX);
            int y = MathHelper.floor_double(entity.posY);
            int z = MathHelper.floor_double(entity.posZ);
            BiomeGenBase biome = world.getBiomeGenForCoords(x, z);
            if (biome.biomeName.equals("Ominous Woods")) {
                return false;
            } else if (world.isRaining() && biome.canSpawnLightningBolt()) {
                return false;
            } else {
                return world.canBlockSeeTheSky(x, y + MathHelper.ceiling_double_int((double)entity.height), z);
            }
        } else {
            return false;
        }
    }
}
