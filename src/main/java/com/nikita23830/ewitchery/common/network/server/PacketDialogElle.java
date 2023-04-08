package com.nikita23830.ewitchery.common.network.server;

import com.nikita23830.ewitchery.common.stats.StatManager;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import cpw.mods.fml.relauncher.Side;
import io.netty.buffer.ByteBuf;
import net.minecraft.util.ChatComponentText;

public class PacketDialogElle implements IMessage, IMessageHandler<PacketDialogElle, IMessage> {
    private int type;
    private int idBtn;

    public PacketDialogElle() {}

    public PacketDialogElle(int type, int idBtn) {
        this.type = type;
        this.idBtn = idBtn;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        this.type = buf.readInt();
        this.idBtn = buf.readInt();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(this.type);
        buf.writeInt(this.idBtn);
    }

    @Override
    public IMessage onMessage(PacketDialogElle message, MessageContext ctx) {
        if (ctx.side == Side.CLIENT)
            return null;
        switch (message.type) {
            case 0: {
                if (!StatManager.hasStat(StatManager.firstContract, ctx.getServerHandler().playerEntity)) {
                    ctx.getServerHandler().playerEntity.addChatComponentMessage(new ChatComponentText("§eElle§f> Принеси демонический контракт"));
                    ctx.getServerHandler().playerEntity.triggerAchievement(StatManager.firstContract);
                }
                return null;
            }
        }
        return null;
    }
}
