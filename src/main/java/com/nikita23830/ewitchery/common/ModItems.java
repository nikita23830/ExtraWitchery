package com.nikita23830.ewitchery.common;

import com.nikita23830.ewitchery.common.items.*;
import com.nikita23830.ewitchery.common.items.armor.ArmorBoots;
import com.nikita23830.ewitchery.common.items.armor.ArmorChest;
import com.nikita23830.ewitchery.common.items.armor.ArmorHelm;
import com.nikita23830.ewitchery.common.items.armor.ArmorLegs;
import com.nikita23830.ewitchery.common.items.tools.ItemAxe;
import com.nikita23830.ewitchery.common.items.tools.ItemPick;
import com.nikita23830.ewitchery.common.items.tools.ItemShovel;
import com.nikita23830.ewitchery.common.items.tools.ItemSword;
import net.minecraft.item.Item;

public class ModItems {
    public static ModItem chalk;
    public static ModItem heartChalk;
    public static ModItem orderBook;
    public static ModItem syringe;
    public static ModItem dropBlood;
    public static ModItem elfSkull;
    public static ModItem itemHeart;
    public static ModItem elfArrow;
    public static ModItem sunArrow;
    public static ModItem bloodArrow;
    public static Item elfBow;
    public static ModItem itemHerbal;
    public static ModItem blackPotion;
    public static ModItem breakArmor;
    public static ModItem fragmentNetherStar;
    public static ModItem demonEye;
    public static ModItem ring;

    public static Item helm;
    public static Item chest;
    public static Item legs;
    public static Item boots;

    public static Item pick;
    public static Item axe;
    public static Item shovel;
    public static Item sword;

    public static void init() {
        chalk = new ItemChalk();
        heartChalk = new ItemHeartChalk();
        orderBook = new ItemOlderBook();
        syringe = new ItemSyringe();
        dropBlood = new ItemDropBlood();
        elfSkull = new ItemElfSkull();
        itemHeart = new ItemHeart();
        elfArrow = new ItemElfArrow();
        sunArrow = new ItemSunArrow();
        bloodArrow = new ItemBloodArrow();
        elfBow = new ItemElfBow();
        itemHerbal = new ItemHerbal();
        blackPotion = new ItemBlackPotion();
        breakArmor = new ItemBreakArmor();
        fragmentNetherStar = new ItemFragmentNetherStar();
        demonEye = new ItemDemonEye();
        ring = new ItemRing();

        helm = new ArmorHelm();
        chest = new ArmorChest();
        legs = new ArmorLegs();
        boots = new ArmorBoots();

        pick = new ItemPick();
        axe = new ItemAxe();
        shovel = new ItemShovel();
        sword = new ItemSword();
    }
}
