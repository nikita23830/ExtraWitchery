package com.nikita23830.ewitchery.common;

import com.nikita23830.ewitchery.AdvanceWitchery;
import com.nikita23830.ewitchery.common.entity.*;
import com.nikita23830.ewitchery.common.entity.projectile.EntityElfArrow;
import com.nikita23830.ewitchery.common.entity.projectile.EntitySunArrow;
import cpw.mods.fml.common.registry.EntityRegistry;

public class ModEntity {

    public static void init() {
        int id = 1;
        EntityRegistry.registerModEntity(EntityCursedElf.class, "cursedElf", id++, AdvanceWitchery.instance, 64, 10, true);
        EntityRegistry.registerModEntity(EntityDarkElf.class, "darkElf", id++, AdvanceWitchery.instance, 64, 10, true);
        EntityRegistry.registerModEntity(EntityBehemoth.class, "behemoth", id++, AdvanceWitchery.instance, 64, 10, true);
        EntityRegistry.registerModEntity(EntityGuardian.class, "guardian", id++, AdvanceWitchery.instance, 64, 10, true);
        EntityRegistry.registerModEntity(EntityPhantom.class, "phantom", id++, AdvanceWitchery.instance, 64, 10, true);
        EntityRegistry.registerModEntity(EntityBeholder.class, "beholder", id++, AdvanceWitchery.instance, 64, 10, true);
        EntityRegistry.registerModEntity(EntityJabberwock.class, "jabberwock", id++, AdvanceWitchery.instance, 64, 10, true);
        EntityRegistry.registerModEntity(EntityTrent.class, "trent", id++, AdvanceWitchery.instance, 64, 10, true);
        EntityRegistry.registerModEntity(EntityEnt.class, "ent", id++, AdvanceWitchery.instance, 64, 10, true);
        EntityRegistry.registerModEntity(EntityBlaze.class, "blazeFreeze", id++, AdvanceWitchery.instance, 64, 10, true);
        EntityRegistry.registerModEntity(EntityElly.class, "elly", id++, AdvanceWitchery.instance, 64, 3, true);

        EntityRegistry.registerModEntity(EntityElfArrow.class, "elfArrow", id++, AdvanceWitchery.instance, 64, 3, true);
        EntityRegistry.registerModEntity(EntitySunArrow.class, "sunArrow", id++, AdvanceWitchery.instance, 64, 3, true);
    }
}
