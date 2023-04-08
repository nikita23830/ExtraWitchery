package com.nikita23830.ewitchery.common.network;

import com.nikita23830.ewitchery.AdvanceWitchery;
import com.nikita23830.ewitchery.common.network.client.PacketOpenDialogElle;
import com.nikita23830.ewitchery.common.network.server.PacketDialogElle;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import cpw.mods.fml.relauncher.Side;

public class NetworkHandler {
    public static final SimpleNetworkWrapper INSTANCE;

    public NetworkHandler() {
    }

    public static void init(){
        int idDescruminator = 0;
        // start server -> client
        INSTANCE.registerMessage(PacketOpenDialogElle.class, PacketOpenDialogElle.class, ++idDescruminator, Side.CLIENT);

        // start client -> server
        INSTANCE.registerMessage(PacketDialogElle.class, PacketDialogElle.class, ++idDescruminator, Side.SERVER);
    }

    static {
        INSTANCE = NetworkRegistry.INSTANCE.newSimpleChannel(AdvanceWitchery.MODID.toLowerCase());
    }
}
