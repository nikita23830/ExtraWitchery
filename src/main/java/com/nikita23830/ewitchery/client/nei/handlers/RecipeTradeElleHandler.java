package com.nikita23830.ewitchery.client.nei.handlers;

import codechicken.nei.PositionedStack;
import codechicken.nei.recipe.GuiRecipe;
import com.nikita23830.ewitchery.client.nei.TemplateRecipeHandler;
import com.nikita23830.ewitchery.client.nei.recipe.CachedRecipeTradeElle;
import com.nikita23830.ewitchery.common.recipes.RecipeManager;
import com.nikita23830.ewitchery.common.recipes.RecipeTradeElle;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.StatCollector;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class RecipeTradeElleHandler extends TemplateRecipeHandler {
    private int stage = 0;

    public RecipeTradeElleHandler() {

    }

    public String getRecipeName() {
        return StatCollector.translateToLocal("awitchery.nei.tradeElle");
    }

    public String getRecipeID() {
        return "awitchery.tradeElle";
    }

    @Override
    public String getGuiTexture() {
        return "nei_crafter_gui.png";
    }

    @Override
    public void loadTransferRects() {
        transferRects.add(new RecipeTransferRect(new Rectangle(110, 30, 25, 30), "awitchery.tradeElle"));
    }

    @Override
    public int recipiesPerPage() {
        return 4;
    }

    @Override
    public void drawBackground(int recipe) {
        //CachedRecipeArmorSplit current = (CachedRecipeArmorSplit) this.arecipes.get(recipe);
//        RenderHelper.drawGradientRect(37, 10, 1, 39, 71, Color.BLACK.getRGB(), Color.BLACK.getRGB());
//        RenderHelper.drawGradientRect(57, 10, 1, 59, 71, Color.BLACK.getRGB(), Color.BLACK.getRGB());
//
//        RenderHelper.drawGradientRect(15, 32, 1, 81, 34, Color.BLACK.getRGB(), Color.BLACK.getRGB());
//        RenderHelper.drawGradientRect(15, 52, 1, 81, 54, Color.BLACK.getRGB(), Color.BLACK.getRGB());
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
            for(IRecipe recipe : RecipeManager.getListRecipeElle()) {
                if(recipe == null)
                    continue;

                arecipes.add(new CachedRecipeTradeElle((RecipeTradeElle) recipe));
            }
        } else
            super.loadCraftingRecipes(outputId, results);
    }

    @Override
    public void loadCraftingRecipes(ItemStack result) {
        if (result == null)
            return;
        load(result);
    }

    private void load(ItemStack stack) {
        for (IRecipe recipe : RecipeManager.getListRecipeElle()) {
            RecipeTradeElle r = (RecipeTradeElle) recipe;
            if (r.matches(stack) || r.matchesOutput(stack))
                this.arecipes.add(new CachedRecipeTradeElle(r));
        }
    }

    @Override
    public void loadUsageRecipes(ItemStack ingredient) {
        if (ingredient == null)
            return;
        load(ingredient);
    }

    @Override
    public String getRecipeTabName() {
        return "Торговля с Элли";
    }


    @Override
    public java.util.List<PositionedStack> getOtherStacks(int recipe) {
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

    public java.util.List<String> handleTooltip(GuiRecipe gui, java.util.List<String> currenttip, int recipe) {
        java.util.List<String> a = super.handleTooltip(gui, currenttip, recipe);
        return a;
    }

    public List<PositionedStack> getIngredientStacks(int recipe) {

        return new ArrayList<>();
    }

    @Override
    public PositionedStack getResultStack(int recipe) {
        return null;
    }
}
