package com.nikita23830.ewitchery.common.stats;

import com.emoniph.witchery.Witchery;
import com.nikita23830.ewitchery.common.CommonProxy;
import com.nikita23830.ewitchery.common.ModItems;
import com.nikita23830.ewitchery.common.items.ItemOlderBook;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.GuiIngameMenu;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.stats.Achievement;
import net.minecraftforge.common.AchievementPage;

public class StatManager {
    public static Achievement firstContract;
    public static Achievement readSecondPart;
    public static Achievement elfBlood;
    public static Achievement killEnt;
    public static Achievement readFourPart;
    public static Achievement killJabberwock;
    public static Achievement readFivePart;
    public static Achievement killGuardian;
    public static Achievement readSixPart;
    public static Achievement makeSunArrow;
    public static Achievement readSevenPart;

    public static void init() {
        firstContract = new AchievementMod("firstContract", -2, -2, Witchery.Items.GENERIC.itemContractOwnership.createStack(), null);
        readSecondPart = new AchievementMod("secondPart", -4, -2, ModItems.orderBook.create(), firstContract);
        elfBlood = new AchievementMod("elfBlood", -4, 0, ModItems.itemHeart.create(), readSecondPart);
        killEnt = new AchievementMod("killEnt", -2, 0, ModItems.dropBlood.create(2), elfBlood);
        readFourPart = new AchievementMod("readFourPart", -2, 2, ModItems.orderBook.create(2), killEnt);
        killJabberwock = new AchievementMod("killJabberwock", -4, 2, new ItemStack(Witchery.Items.HUNTSMANS_SPEAR), readFourPart);
        readFivePart = new AchievementMod("fivePart", 4, 2, ModItems.orderBook.create(3), killJabberwock);
        killGuardian = new AchievementMod("killGuardian", 4, 4, ModItems.breakArmor.create(1), readFivePart);
        readSixPart = new AchievementMod("sixPart", 4, 6, ModItems.elfArrow.create(), killGuardian);
        makeSunArrow = new AchievementMod("sunArrow", 2, 4, ModItems.sunArrow.create(), readSixPart);
        readSevenPart = new AchievementMod("sevenPart", 0, 4, ModItems.orderBook.create(4), makeSunArrow);

        AchievementPage advanceWitchery = new AchievementPage("AdvanceWitchery", (Achievement[])AchievementMod.achievements.toArray(new Achievement[AchievementMod.achievements.size()]));
        AchievementPage.registerAchievementPage(advanceWitchery);
    }

    public static boolean hasStat(Achievement achievement, EntityPlayer player) {
        if (player instanceof EntityPlayerMP)
            return MinecraftServer.getServer().getConfigurationManager().func_152602_a(player).hasAchievementUnlocked(achievement);
        return ((EntityClientPlayerMP)player).getStatFileWriter().hasAchievementUnlocked(achievement);
    }

    public static int getIdDialog(EntityPlayer player) {
        if (!hasStat(firstContract, player)) {
            return 0;
        }
        if (hasStat(firstContract, player) && !hasStat(readSecondPart, player) && CommonProxy.hasItem(Witchery.Items.GENERIC.itemContractOwnership.createStack(), player)) {
            CommonProxy.increaseItem(Witchery.Items.GENERIC.itemContractOwnership.createStack(), player);
            CommonProxy.addOrDropItem(ModItems.orderBook.create(1), player);
            player.triggerAchievement(readSecondPart);
            return 1;
        }
        if (hasStat(readSecondPart, player) && !hasStat(elfBlood, player)) {
            if (CommonProxy.hasItem(ModItems.syringe.create(1), player)) {
                player.triggerAchievement(elfBlood);
                CommonProxy.addOrDropItem(ModItems.orderBook.create(2), player);
                return 2;
            } else
                return 1;
        }
        if (hasStat(elfBlood, player) && !hasStat(killEnt, player)) {
            return 2;
        }
        if (hasStat(killEnt, player) && !hasStat(readFourPart, player)) {
            player.triggerAchievement(readFourPart);
            CommonProxy.addOrDropItem(ModItems.orderBook.create(3), player);
            return 3;
        }
        if (hasStat(readFourPart, player) && !hasStat(killJabberwock, player)) {

            return 3;
        }
        if (hasStat(killJabberwock, player) && !hasStat(killGuardian, player)) {
            if (!hasStat(readFivePart, player)) {
                player.triggerAchievement(readFivePart);
                CommonProxy.addOrDropItem(ModItems.orderBook.create(4), player);
            }
            return 4;
        }
        if (hasStat(killGuardian, player) && !hasStat(makeSunArrow, player)) {
            if (!hasStat(readSixPart, player)) {
                CommonProxy.addOrDropItem(ModItems.orderBook.create(5), player);
                player.triggerAchievement(readSixPart);
            }
            return 5;
        }
        if (hasStat(makeSunArrow, player)) {
            if (!hasStat(readSevenPart, player)) {
                CommonProxy.addOrDropItem(ModItems.orderBook.create(6), player);
                player.triggerAchievement(readSevenPart);
            }
            return 6;
        }
        if (CommonProxy.hasItem(ModItems.ring.create(), player)) {
            CommonProxy.increaseItem(ModItems.ring.create(), player);
            return 7;
        }
        return -1;
    }
}
