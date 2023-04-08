package com.nikita23830.ewitchery;

import com.nikita23830.ewitchery.common.CommonProxy;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLConstructionEvent;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;

@Mod(
        modid = AdvanceWitchery.MODID,
        version = AdvanceWitchery.VERSION,
        name = "Advance Witchery",
        dependencies = "after:NotEnoughItems"
)
public class AdvanceWitchery {
    @Mod.Instance("AdvanceWitchery")
    public static AdvanceWitchery instance;
    public static final String MODID = "AdvanceWitchery";
    public static final String VERSION = "1.0";
    @SidedProxy(serverSide = "com.nikita23830.ewitchery.common.CommonProxy", clientSide = "com.nikita23830.ewitchery.client.ClientProxy")
    public static CommonProxy proxy;

    @Mod.EventHandler
    public void cons(FMLConstructionEvent event) {
        proxy.cons(event);
    }

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        proxy.preInit(event);
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        proxy.init(event);
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        proxy.postInit(event);
    }

    public static String modid() {
        return MODID.toLowerCase();
    }
}
