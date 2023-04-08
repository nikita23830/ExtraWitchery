package com.nikita23830.ewitchery.client;

import com.nikita23830.ewitchery.common.tiles.IHud;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.profiler.Profiler;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MovingObjectPosition;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import org.lwjgl.opengl.GL11;

public class HUDHandler {

    @SubscribeEvent
    public void onTooltip(ItemTooltipEvent event) {
        if (event.itemStack == null)
            return;
        if (event.toolTip == null)
            return;
        if (event.itemStack.getItem() instanceof ItemArmor) {
            if (event.itemStack.hasTagCompound() && event.itemStack.getTagCompound().hasKey("selfSunAdvWitchery")) {
                event.toolTip.add("");
                event.toolTip.add("§eУлучшен. Полная защита от солнца, огня и лавы");
            }
        }
    }

    @SubscribeEvent
    public void onDrawScreenPost(RenderGameOverlayEvent.Post event) {
        Minecraft mc = Minecraft.getMinecraft();
        Profiler profiler = mc.mcProfiler;
        if (event.type == RenderGameOverlayEvent.ElementType.ALL) {
            profiler.startSection("ewitchery-hud");
            RenderHelper.enableGUIStandardItemLighting();
            GL11.glEnable(32826);

            try {
                MovingObjectPosition pos = mc.objectMouseOver;
                TileEntity $tile = mc.theWorld.getTileEntity(pos.blockX, pos.blockY, pos.blockZ);
                if ($tile instanceof IHud) {
                    ((IHud)$tile).renderHUD(mc, event.resolution);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            GL11.glEnable(2929);
            profiler.endSection();
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        }
    }
}