package com.nikita23830.ewitchery.common.events;

import com.emoniph.witchery.util.CreatureUtil;
import cpw.mods.fml.common.eventhandler.Event;
import net.minecraft.entity.EntityLivingBase;

public class CheckSunEvent extends Event {
    private CreatureUtil util;
    private EntityLivingBase entity;
    private boolean hasResult = false;
    private boolean result = true;

    public CheckSunEvent(CreatureUtil util, EntityLivingBase entity) {
        this.util = util;
        this.entity = entity;
    }

    @Override
    public boolean hasResult() {
        return hasResult;
    }

    public void setHasResult(boolean hasResult) {
        this.hasResult = hasResult;
    }

    public void setResult(boolean result) {
        setHasResult(true);
        this.result = result;
    }

    public boolean isResult() {
        return result;
    }

    public CreatureUtil getUtil() {
        return util;
    }

    public EntityLivingBase getEntity() {
        return entity;
    }
}
