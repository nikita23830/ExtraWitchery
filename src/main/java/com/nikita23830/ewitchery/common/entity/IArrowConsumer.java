package com.nikita23830.ewitchery.common.entity;

import net.minecraft.entity.Entity;

public interface IArrowConsumer {
    void applySunAttack(Entity attacker, float dmg);
    void applyElfAttack(Entity attacker, float dmg);
    void applyBloodAttack(Entity attacker, float dmg);
}
