package com.nikita23830.ewitchery.common.network.client;

import com.nikita23830.ewitchery.client.gui.GuiDialog;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import cpw.mods.fml.relauncher.Side;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;

public class PacketOpenDialogElle implements IMessage, IMessageHandler<PacketOpenDialogElle, IMessage> {
    private int id = -1;

    public PacketOpenDialogElle() {}

    public PacketOpenDialogElle(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        this.id = buf.readInt();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(this.id);
    }

    @Override
    public IMessage onMessage(PacketOpenDialogElle message, MessageContext ctx) {
        if (ctx.side == Side.SERVER)
            return null;
        Minecraft.getMinecraft().displayGuiScreen(new GuiDialog(message.getId()));
        return null;
    }
}
