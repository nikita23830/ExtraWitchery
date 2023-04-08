package com.nikita23830.ewitchery.client.gui;

import com.emoniph.witchery.entity.EntityFollower;
import com.nikita23830.ewitchery.AdvanceWitchery;
import com.nikita23830.ewitchery.common.network.NetworkHandler;
import com.nikita23830.ewitchery.common.network.server.PacketDialogElle;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.util.ArrayList;
import java.util.StringJoiner;

public class GuiDialog extends GuiScreen {
    private static final ResourceLocation location = new ResourceLocation(AdvanceWitchery.MODID.toLowerCase(), "textures/dialog.png");
    private ArrayList<String> message = new ArrayList<>();
    private final ArrayList<GuiButton> buttons = new ArrayList<>();
    private int guiLeft = 0;
    private int guiTop = 0;
    private int xSize = 0;
    private int ySize = 0;
    private int id;
    private EntityLivingBase living;

    public GuiDialog(int id) {
        this.id = id;
    }

    @Override
    public void initGui() {
        this.living = new EntityFollower(mc.thePlayer.worldObj);
        xSize = 249;
        ySize = 154;
        guiLeft = (width / 2) - (xSize / 2);
        guiTop = (height / 2) - (ySize / 2);
        int k = 0;
        while (true) {
            String splash = I18n.format("awitchery.text.dialog." + id + "." + k, new Object[0]);
            if (splash.equals("awitchery.text.dialog." + id + "." + k)) {
                break;
            }
            message.add(splash);
            ++k;
        }
        k = 0;
        while (true) {
            String splash = I18n.format("awitchery.text.dialog." + id + ".btn." + k, new Object[0]);
            if (splash.equals("awitchery.text.dialog." + id + ".btn." + k)) {
                break;
            }
            buttons.add(new GuiButton(k, guiLeft + 8, guiTop + (90 + (18 * k)),233, 17, splash));
            ++k;
            if (k > 3)
                break;
        }

        super.initGui();
        for (GuiButton gb : buttons) {
            this.buttonList.add(gb);
        }
        ArrayList<String> converted = new ArrayList<>();
        FontRenderer f = mc.fontRenderer;
        for (String s : message) {
            if (f.getStringWidth(s) <= 233)
                converted.add(s);
            else {
                String[] text = s.split(" ");
                StringJoiner sj = new StringJoiner(" ");
                for (String t : text) {
                    if (f.getStringWidth(sj.toString() + " " + t) > 233) {
                        converted.add(sj.toString());
                        sj = new StringJoiner(" ");
                        sj.add(t);
                    } else
                        sj.add(t);
                }
                if (!sj.toString().isEmpty())
                    converted.add(sj.toString());
            }
        }
        this.message = converted;
    }

    @Override
    public void drawScreen(int x, int y, float t) {
        this.drawWorldBackground(0);
        mc.getTextureManager().bindTexture(location);
        drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);
        drawCenteredString(mc.fontRenderer, "Elle", (guiLeft + (xSize / 2)), (guiTop + 4), Color.WHITE.getRGB());
        int i = 0;
        for (String s : message) {
            drawCenteredString(mc.fontRenderer, s, (guiLeft + (xSize / 2)), (guiTop + 24 + (i * 14)), Color.WHITE.getRGB());
            ++i;
        }
        super.drawScreen(x, y, t);
        GL11.glPushMatrix();
        GL11.glTranslatef(0, 0, 100);
        GuiInventory.func_147046_a((guiLeft - 50), (guiTop + 150), 70, ((guiLeft + (xSize / 2F) - x)), ((guiTop + (ySize / 2F)) - y), this.living);
        GL11.glPopMatrix();
    }

    @Override
    protected void actionPerformed(GuiButton gb) {
        super.actionPerformed(gb);
        if (gb.id == 0 && this.id == 0) {
            Minecraft.getMinecraft().displayGuiScreen(null);
        }
        if (gb.id == 1) {
            mc.displayGuiScreen(null);
            NetworkHandler.INSTANCE.sendToServer(new PacketDialogElle(this.id, 1));
        }
    }
}
