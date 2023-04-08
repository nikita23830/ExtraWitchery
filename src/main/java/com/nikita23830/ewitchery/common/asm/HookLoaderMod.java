package com.nikita23830.ewitchery.common.asm;

import gloomyfolken.hooklib.minecraft.HookLoader;
import gloomyfolken.hooklib.minecraft.PrimaryClassTransformer;

public class HookLoaderMod extends HookLoader {

    public static String[] classes = new String[]{
            "com.emoniph.witchery.util.CreatureUtil"
    };

    @Override
    public String[] getASMTransformerClass() {
        return new String[]{PrimaryClassTransformer.class.getName()};
    }

    @Override
    public void registerHooks() {
        registerHookContainer("com.nikita23830.ewitchery.common.asm.HookWitchery");
    }
}
