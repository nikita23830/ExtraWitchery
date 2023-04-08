package com.nikita23830.ewitchery.common.recipes;

import com.emoniph.witchery.Witchery;
import com.emoniph.witchery.item.ItemGeneral;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.nikita23830.ewitchery.common.ModItems;
import com.nikita23830.ewitchery.common.items.armor.ModArmor;
import com.nikita23830.ewitchery.common.stats.StatManager;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.EntityBlaze;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.RecipesCrafting;
import net.minecraft.util.ChatComponentText;
import net.minecraft.world.World;
import net.minecraft.world.WorldProviderHell;
import scala.collection.mutable.ListBuffer;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class RecipeManager {
    private static final Cache<String, List<IRecipe>> cache_recipe = CacheBuilder.newBuilder().expireAfterWrite(10, TimeUnit.MINUTES).build();
    public static ItemStack[] listHelm = null;
    public static ItemStack[] listChest = null;
    public static ItemStack[] listLegs = null;
    public static ItemStack[] listBoots = null;
    public static ItemStack[] convertHelm = null;
    public static ItemStack[] convertChest = null;
    public static ItemStack[] convertLegs = null;
    public static ItemStack[] convertBoots = null;

    public static void init() {
        initRecipesBlackRitual();
        initRecipesTradeElle();

        GameRegistry.addRecipe(new RecipeArmorSplit(0));
        GameRegistry.addRecipe(new RecipeArmorSplit(1));
        GameRegistry.addRecipe(new RecipeArmorSplit(2));
        GameRegistry.addRecipe(new RecipeArmorSplit(3));
    }

    public static void initArmor() {
        ArrayList<ItemArmor> listHelm = new ArrayList<>();
        ArrayList<ItemArmor> listChest = new ArrayList<>();
        ArrayList<ItemArmor> listLegs = new ArrayList<>();
        ArrayList<ItemArmor> listBoots = new ArrayList<>();
        ArrayList<ItemStack> convertHelm = new ArrayList<>();
        ArrayList<ItemStack> convertChest = new ArrayList<>();
        ArrayList<ItemStack> convertLegs = new ArrayList<>();
        ArrayList<ItemStack> convertBoots = new ArrayList<>();
        for (Object i : Item.itemRegistry) {
            Item item = (Item) i;
            if (!(item instanceof ItemArmor))
                continue;
            if (item instanceof ModArmor)
                continue;
            switch (((ItemArmor)item).armorType) {
                case 0:
                    listHelm.add((ItemArmor) item);
                    convertHelm.add(RecipeArmorSplit.getCraftingResult(new ItemStack(item)));
                    break;
                case 1:
                    listChest.add((ItemArmor) item);
                    convertChest.add(RecipeArmorSplit.getCraftingResult(new ItemStack(item)));
                    break;
                case 2:
                    listLegs.add((ItemArmor) item);
                    convertLegs.add(RecipeArmorSplit.getCraftingResult(new ItemStack(item)));
                    break;
                case 3:
                    listBoots.add((ItemArmor) item);
                    convertBoots.add(RecipeArmorSplit.getCraftingResult(new ItemStack(item)));
                    break;
            }
        }
        put(listHelm, convertHelm, 0);
        put(listChest, convertChest, 1);
        put(listLegs, convertLegs, 2);
        put(listBoots, convertBoots, 3);
    }

    private static void put(List<ItemArmor> obj, List<ItemStack> conv, int type) {
        ItemStack[] a = new ItemStack[obj.size()];
        for (int i = 0; i < obj.size(); ++i) {
            a[i] = new ItemStack(obj.get(i));
        }
        ItemStack[] b = new ItemStack[conv.size()];
        for (int i = 0; i < conv.size(); ++i) {
            b[i] = conv.get(i);
        }
        switch (type) {
            case 0:
                listHelm = a;
                convertHelm = b;
                break;
            case 1:
                listChest = a;
                convertChest = b;
                break;
            case 2:
                listLegs = a;
                convertLegs = b;
                break;
            case 3:
                listBoots = a;
                convertBoots = b;
                break;
        }
    }

    private static void initRecipesBlackRitual() {
        GameRegistry.addRecipe(new RecipeBlackRitual(
                "recipeSpawnGuardian",
                new ItemStack(Blocks.bedrock),
                new ItemStack[] {
                        Witchery.Items.GENERIC.itemCreeperHeart.createStack(),
                        Witchery.Items.GENERIC.itemDemonHeart.createStack(),
                        Witchery.Items.GENERIC.itemAttunedStoneCharged.createStack(),
                        Witchery.Items.GENERIC.itemKobolditePentacle.createStack(),
                },
                EntityBlaze.class.getSimpleName(),
                -1
        ).setClosed());
        GameRegistry.addRecipe(new RecipeBlackRitual(
                "recipeSpawnVampireMaster",
                new ItemStack(Blocks.water),
                new ItemStack[] {
                        ModItems.itemHeart.create(),
                        ModItems.itemHeart.create(1),
                        ModItems.itemHeart.create(2),
                        ModItems.syringe.create(3),
                        Witchery.Items.GENERIC.itemInfernalBlood.createStack(),
                        ModItems.demonEye.create(),
                        Witchery.Items.GENERIC.itemDemonHeart.createStack(),
                        null,
                        null,
                        null,
                        null,
                        null,
                        null
                }
        ).setClosed());
    }

    private static void initRecipesTradeElle() {
        GameRegistry.addRecipe(new RecipeTradeElle(Witchery.Items.GENERIC.itemContractOwnership.createStack(), new ItemStack(ModItems.orderBook, 1, 0)));
    }

    public static List<IRecipe> getListArmorSplit() {
        List<IRecipe> list = cache_recipe.getIfPresent("armor_split");
        if (list == null) {
            list = new ArrayList<IRecipe>();
            for (Object recipe : CraftingManager.getInstance().getRecipeList()) {
                if (recipe instanceof RecipeArmorSplit)
                    list.add((IRecipe) recipe);
            }
            cache_recipe.put("armor_split", list);
        }
        return list;
    }

    public static List<IRecipe> getListBlackRituals() {
        List<IRecipe> list = cache_recipe.getIfPresent("black_ritual");
        if (list == null) {
            list = new ArrayList<IRecipe>();
            for (Object recipe : CraftingManager.getInstance().getRecipeList()) {
                if (recipe instanceof RecipeBlackRitual)
                    list.add((IRecipe) recipe);
            }
            cache_recipe.put("black_ritual", list);
        }
        return list;
    }

    public static List<IRecipe> getListRecipeElle() {
        List<IRecipe> list = cache_recipe.getIfPresent("elle_trade");
        if (list == null) {
            list = new ArrayList<IRecipe>();
            for (Object recipe : CraftingManager.getInstance().getRecipeList()) {
                if (recipe instanceof RecipeTradeElle)
                    list.add((IRecipe) recipe);
            }
            cache_recipe.put("elle_trade", list);
        }
        return list;
    }

    public static RecipeBlackRitual findRecipe(InventoryNull inv, World w) {
        for (IRecipe recipe : getListBlackRituals()) {
            if (recipe instanceof RecipeBlackRitual && ((RecipeBlackRitual)recipe).matches(inv, w))
                return (RecipeBlackRitual) recipe;
        }
        return null;
    }

    public static RecipeBlackRitual findRecipe(ItemStack out) {
        for (IRecipe recipe : getListBlackRituals()) {
            if (recipe instanceof RecipeBlackRitual && ItemStack.areItemStacksEqual(out, ((RecipeBlackRitual)recipe).getRecipeOutput()) && ItemStack.areItemStackTagsEqual(out, ((RecipeBlackRitual)recipe).getRecipeOutput()))
                return (RecipeBlackRitual) recipe;
        }
        return null;
    }
}
