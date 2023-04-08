package com.nikita23830.ewitchery.client.gui;

import com.nikita23830.ewitchery.AdvanceWitchery;
import com.nikita23830.ewitchery.common.stats.StatManager;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class GuiReadOlderBook extends GuiScreen {
    private final ItemStack stack;
    private final List<String> text;
    private static ResourceLocation loc = new ResourceLocation(AdvanceWitchery.MODID.toLowerCase(), "textures/hud-old-scroll.png");
    private int guiLeft = 0;
    private int guiTop = 0;

    public GuiReadOlderBook(ItemStack stack) {
        this.stack = stack;
        this.text = new ArrayList<String>();
        int k = 0;
        while (true) {
            String splash = I18n.format("awitchery.text.order.book." + stack.getItemDamage() + "." + k, new Object[0]);
            if (splash.equals("awitchery.text.order.book." + stack.getItemDamage() + "." + k)) {
                break;
            }
            text.add(splash);
            ++k;
        }
    }

    @Override
    public void initGui() {
        super.initGui();
    }

    @Override
    public void drawScreen(int x, int y, float t) {
        super.drawScreen(x, y, t);
        super.drawWorldBackground(0);
        guiLeft = (width / 2) - 128;
        guiTop = (height / 2) - 128;
        mc.getTextureManager().bindTexture(loc);
        boolean show = false;
        long dayTime = mc.thePlayer.worldObj.getWorldTime() % 24000;
        switch (stack.getItemDamage()) {
            case 0:
                show = true;
                break;
            case 1:
                show = (dayTime > 15000 && dayTime < 20000 && mc.thePlayer.worldObj.getMoonPhase() == 0);
                break;
            case 2:
                show = (dayTime > 15000 && dayTime < 20000 && mc.thePlayer.worldObj.getMoonPhase() == 4);
                break;
            case 3:
                show = (dayTime >= 2000 && dayTime <= 10000);
                break;
            case 4:
                show = mc.thePlayer.worldObj.provider.dimensionId == -1;
                break;
            case 5:
                show = !mc.thePlayer.worldObj.getWorldInfo().isRaining() && !mc.thePlayer.worldObj.getWorldInfo().isThundering();
                if (show)
                    show = (dayTime >= 1000 && dayTime <= 12000);
                break;
            case 6:
                show = true;
                break;
        }
        String prefix = show ? "" : "§k";
        drawTexturedModalRect(guiLeft, guiTop, 0, 0, 256, 256);
        for (int i = 0; i < text.size(); ++i) {
            int w = mc.fontRenderer.getStringWidth(prefix + text.get(i));
            mc.fontRenderer.drawString((prefix + text.get(i)), guiLeft + 128 - (w / 2), (guiTop + 35 + (i * 15)), Color.BLACK.getRGB(), false);
        }
        GL11.glColor4f(1, 1, 1, 1);
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }
}
