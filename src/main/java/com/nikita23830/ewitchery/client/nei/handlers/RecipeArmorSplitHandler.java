package com.nikita23830.ewitchery.client.nei.handlers;

import codechicken.nei.PositionedStack;
import codechicken.nei.recipe.GuiRecipe;
import com.nikita23830.ewitchery.client.nei.TemplateRecipeHandler;
import com.nikita23830.ewitchery.client.nei.recipe.CachedRecipeArmorSplit;
import com.nikita23830.ewitchery.common.ModItems;
import com.nikita23830.ewitchery.common.recipes.RecipeArmorSplit;
import com.nikita23830.ewitchery.common.recipes.RecipeManager;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.StatCollector;
import org.lwjgl.opengl.GL11;
import vazkii.botania.client.core.helper.RenderHelper;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class RecipeArmorSplitHandler extends TemplateRecipeHandler {
    private int stage = 0;

    public RecipeArmorSplitHandler() {

    }

    public String getRecipeName() {
        return StatCollector.translateToLocal("awitchery.nei.splitArmor");
    }

    public String getRecipeID() {
        return "awitchery.splitArmor";
    }

    @Override
    public String getGuiTexture() {
        return "nei_crafter_gui.png";
    }

    @Override
    public void loadTransferRects() {
        transferRects.add(new RecipeTransferRect(new Rectangle(110, 30, 25, 30), "awitchery.splitArmor"));
    }

    @Override
    public int recipiesPerPage() {
        return 4;
    }

    @Override
    public void drawBackground(int recipe) {
        //CachedRecipeArmorSplit current = (CachedRecipeArmorSplit) this.arecipes.get(recipe);
        RenderHelper.drawGradientRect(37, 10, 1, 39, 71, Color.BLACK.getRGB(), Color.BLACK.getRGB());
        RenderHelper.drawGradientRect(57, 10, 1, 59, 71, Color.BLACK.getRGB(), Color.BLACK.getRGB());

        RenderHelper.drawGradientRect(15, 32, 1, 81, 34, Color.BLACK.getRGB(), Color.BLACK.getRGB());
        RenderHelper.drawGradientRect(15, 52, 1, 81, 54, Color.BLACK.getRGB(), Color.BLACK.getRGB());
    }

    @Override
    public void drawForeground(int recipe) {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glDisable(2896);
        // NO CHANGE RECIPE
        super.drawExtras(recipe);
    }

    @Override
    public void loadCraftingRecipes(String outputId, Object... results) {
        if (outputId.equals(getRecipeID())) {
            for(IRecipe recipe : RecipeManager.getListArmorSplit()) {
                if(recipe == null)
                    continue;

                arecipes.add(new CachedRecipeArmorSplit((RecipeArmorSplit) recipe));
            }
        } else
            super.loadCraftingRecipes(outputId, results);
    }

    @Override
    public void loadCraftingRecipes(ItemStack result) {
        if (result == null)
            return;
        if (result.getItem() instanceof ItemArmor) {
            int type = ((ItemArmor)result.getItem()).armorType;
            for(IRecipe recipe : RecipeManager.getListArmorSplit()) {
                if (((RecipeArmorSplit)recipe).type == type) {
                    this.arecipes.add(new CachedRecipeArmorSplit((RecipeArmorSplit) recipe));
                }
            }
        }
    }

    @Override
    public void loadUsageRecipes(ItemStack ingredient) {
        int typeArmor = 0;
        if (ingredient == null)
            return;
        if (!(ingredient.getItem() instanceof ItemArmor))
            return;
        typeArmor = ((ItemArmor)ingredient.getItem()).armorType;
        for(Object recipe : CraftingManager.getInstance().getRecipeList()) {
            if(!(recipe instanceof RecipeArmorSplit))
                continue;

            if (((RecipeArmorSplit)recipe).type == typeArmor) {
                CachedRecipeArmorSplit crecipe = new CachedRecipeArmorSplit((RecipeArmorSplit) recipe);
                this.arecipes.add(crecipe);
            }
        }

    }

    @Override
    public String getRecipeTabName() {
        return "Объединение брони";
    }


    @Override
    public List<PositionedStack> getOtherStacks(int recipe) {
        return new ArrayList<>();
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
        if (cycleticks % 20 == 0) {
            ++stage;
        }
    }

    @Override
    public String getOverlayIdentifier() {
        return super.getOverlayIdentifier();
    }

    public List<String> handleTooltip(GuiRecipe gui, List<String> currenttip, int recipe) {
        List<String> a = super.handleTooltip(gui, currenttip, recipe);
        return a;
    }

    public List<PositionedStack> getIngredientStacks(int recipe) {
        CachedRecipeArmorSplit split = (CachedRecipeArmorSplit) this.arecipes.get(recipe);
        ArrayList<PositionedStack> stacks = new ArrayList<>();
        switch (split.recipe.type) {
            case 0: {
                if (stage >= RecipeManager.listHelm.length)
                    stage = 0;
                stacks.add(new PositionedStack(RecipeManager.listHelm[stage], 20, 15));
                stacks.add(new PositionedStack(new ItemStack(ModItems.helm), 40, 15));
                break;
            }
            case 1: {
                if (stage >= RecipeManager.listChest.length)
                    stage = 0;
                stacks.add(new PositionedStack(RecipeManager.listChest[stage], 20, 15));
                stacks.add(new PositionedStack(new ItemStack(ModItems.chest), 40, 15));
                break;
            }
            case 2: {
                if (stage >= RecipeManager.listLegs.length)
                    stage = 0;
                stacks.add(new PositionedStack(RecipeManager.listLegs[stage], 20, 15));
                stacks.add(new PositionedStack(new ItemStack(ModItems.legs), 40, 15));
                break;
            }
            case 3: {
                if (stage >= RecipeManager.listBoots.length)
                    stage = 0;
                stacks.add(new PositionedStack(RecipeManager.listBoots[stage], 20, 15));
                stacks.add(new PositionedStack(new ItemStack(ModItems.boots), 40, 15));
                break;
            }
        }
        return stacks;
    }

    @Override
    public PositionedStack getResultStack(int recipe) {
        CachedRecipeArmorSplit split = (CachedRecipeArmorSplit) this.arecipes.get(recipe);
        switch (split.recipe.type) {
            case 0: {
                if (stage >= RecipeManager.listHelm.length)
                    stage = 0;
                return new PositionedStack(RecipeManager.convertHelm[stage], 130, 35);
            }
            case 1: {
                if (stage >= RecipeManager.listChest.length)
                    stage = 0;
                return new PositionedStack(RecipeManager.convertChest[stage], 130, 35);
            }
            case 2: {
                if (stage >= RecipeManager.listLegs.length)
                    stage = 0;
                return new PositionedStack(RecipeManager.convertLegs[stage], 130, 35);
            }
            case 3: {
                if (stage >= RecipeManager.listBoots.length)
                    stage = 0;
                return new PositionedStack(RecipeManager.convertBoots[stage], 130, 35);
            }
        }
        return super.getResultStack(recipe);
    }
}

