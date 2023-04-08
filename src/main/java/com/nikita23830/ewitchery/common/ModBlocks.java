package com.nikita23830.ewitchery.common;

import com.nikita23830.ewitchery.common.block.*;
import net.minecraft.block.Block;

public class ModBlocks {
    public static Block blockCircle;
    public static Block blockHeartCircle;
    public static Block blackFire;
    public static Block bloodFire;
    public static Block pedestalElfArrow;

    public static void init() {
        blockCircle = new BlockCircle();
        blockHeartCircle = new BlockHeartCircle();
        blackFire = new BlockBlackFire();
        bloodFire = new BlockBloodFire();
        pedestalElfArrow = new BlockPedestalElfArrow();
    }
}
