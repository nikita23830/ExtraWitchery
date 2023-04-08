package com.nikita23830.ewitchery.client.nei;

import codechicken.nei.api.API;
import codechicken.nei.api.IConfigureNEI;
import com.emoniph.witchery.Witchery;
import com.nikita23830.ewitchery.AdvanceWitchery;
import com.nikita23830.ewitchery.client.nei.handlers.RecipeArmorSplitHandler;
import com.nikita23830.ewitchery.client.nei.handlers.RecipeBlackRitualHandler;
import com.nikita23830.ewitchery.client.nei.handlers.RecipeTradeElleHandler;
import cpw.mods.fml.common.Mod;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;

public class NEIAdvanceWitcheryConfig implements IConfigureNEI {

    public NEIAdvanceWitcheryConfig() {}

    @Override
    public void loadConfig() {
        registerHandler(new RecipeArmorSplitHandler());
        registerHandler(new RecipeBlackRitualHandler());
        registerHandler(new RecipeTradeElleHandler());
    }

    private void registerHandler(TemplateRecipeHandler handler) {
        API.registerRecipeHandler(handler);
        API.registerUsageHandler(handler);
    }

    @Override
    public String getName() {
        return ((Mod) AdvanceWitchery.class.getAnnotation(Mod.class)).name();
    }

    @Override
    public String getVersion() {
        return ((Mod) AdvanceWitchery.class.getAnnotation(Mod.class)).version();
    }

}
