package com.nikita23830.ewitchery.client.nei.handlers;

import codechicken.lib.gui.GuiDraw;
import codechicken.nei.PositionedStack;
import codechicken.nei.recipe.GuiCraftingRecipe;
import com.nikita23830.ewitchery.client.nei.TemplateRecipeHandler;
import com.nikita23830.ewitchery.client.nei.recipe.CachedRecipeBlackRitual;
import com.nikita23830.ewitchery.common.recipes.InventoryCraftingUtil;
import com.nikita23830.ewitchery.common.recipes.RecipeBlackRitual;
import com.nikita23830.ewitchery.common.recipes.RecipeManager;
import com.nikita23830.ewitchery.common.recipes.RecipeSize;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.StatCollector;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class RecipeBlackRitualHandler extends TemplateRecipeHandler {
    private static final int[][] recipeMini =
            new int[][]{{72, 12}, {102, 12}, {132, 42}, {132, 72}, {132, 102}, {102, 132}, {72, 132}, {42, 132}, {12, 102}, {12, 72}, {12, 42}, {42, 12}};
    private static final int[][] recipeNormal =
            new int[][]{{72,12},{88,12},{104,12},{120,28},{136,44},{136,60},{136,76},{136,92},{136,108},{120,124},{104,140},{88,140},{72,140},{56,140},{40,140},{24,124},{8,108},{8,92},{8,76},{8,60},{8,44},{24,28},{40,12},{56,12}};

    public String getRecipeName() {
        return StatCollector.translateToLocal("awitchery.nei.blackRitual");
    }

    public String getRecipeID() {
        return "awitchery.blackRitual";
    }

    @Override
    public String getGuiTexture() {
        return "advancewitchery:textures/nei-black.png";
    }

    @Override
    public void loadTransferRects() {
        transferRects.add(new RecipeTransferRect(new Rectangle(110, 30, 0, 0), "awitchery.blackRitual"));
    }

    @Override
    public int recipiesPerPage() {
        return 1;
    }

    @Override
    public void drawBackground(int recipe) {
        GuiDraw.changeTexture(this.getGuiTexture());
        GL11.glPushMatrix();
        GL11.glTranslatef(82, 80, 0);
        float transfer = cycleticks % 200;
        transfer = 1.8F * transfer;
        GL11.glRotatef(transfer, 0, 0, 1);
        GuiDraw.drawTexturedModalRect(-80, -80, 0, 0, 160, 160);
        GL11.glPopMatrix();
    }

    @Override
    public void drawForeground(int recipe) {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glDisable(2896);
        super.drawExtras(recipe);
    }

    @Override
    public void loadCraftingRecipes(String outputId, Object... results) {
        if (outputId.equals(getRecipeID())) {
            for(IRecipe recipe : RecipeManager.getListBlackRituals()) {
                if(recipe == null)
                    continue;
                if (((RecipeBlackRitual)recipe).isClosed())
                    continue;
                arecipes.add(new CachedRecipeBlackRitual((RecipeBlackRitual) recipe));
            }
        } else
            super.loadCraftingRecipes(outputId, results);
    }

    @Override
    public void loadCraftingRecipes(ItemStack result) {
        loadRecipes(result);
    }

    private void loadRecipes(ItemStack stack) {
        for (IRecipe recipe : RecipeManager.getListBlackRituals()) {
            if (recipe.getRecipeOutput().getItem() == stack.getItem()
                    && recipe.getRecipeOutput().getItemDamage() == stack.getItemDamage()) {
                if (((RecipeBlackRitual)recipe).isClosed())
                    continue;
                this.arecipes.add(new CachedRecipeBlackRitual((RecipeBlackRitual) recipe));
            }
        }
        for (IRecipe recipe : RecipeManager.getListBlackRituals()) {
            if (InventoryCraftingUtil.hasStack(((RecipeBlackRitual)recipe).getInput(), stack)) {
                if (((RecipeBlackRitual)recipe).isClosed())
                    continue;
                this.arecipes.add(new CachedRecipeBlackRitual((RecipeBlackRitual) recipe));
            }
        }
    }

    @Override
    public void loadUsageRecipes(ItemStack ingredient) {
        loadRecipes(ingredient);
    }

    @Override
    public String getRecipeTabName() {
        return "Тёмный ритуал";
    }

    @Override
    public List<PositionedStack> getOtherStacks(int recipe) {
        return new ArrayList<>();
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
    }

    @Override
    public String getOverlayIdentifier() {
        return super.getOverlayIdentifier();
    }

    public List<PositionedStack> getIngredientStacks(int recipe) {
        CachedRecipeBlackRitual ritual = (CachedRecipeBlackRitual) this.arecipes.get(recipe);
        RecipeBlackRitual rec = ritual.recipe;
        ArrayList<PositionedStack> stacks = new ArrayList<>();
        if (rec.size() == RecipeSize.MINI) {
            for (int i = 0; i < rec.size().size; ++i) {
                ItemStack stack = rec.getInput()[i];
                if (stack == null)
                    continue;
                stacks.add(new PositionedStack(stack, recipeMini[i][0], recipeMini[i][1]));
            }
        } else if (rec.size() == RecipeSize.NORMAL) {
            for (int i = 0; i < rec.size().size; ++i) {
                ItemStack stack = rec.getInput()[i];
                if (stack == null)
                    continue;
                stacks.add(new PositionedStack(stack, recipeNormal[i][0], recipeNormal[i][1]));
            }
        }

        return stacks;
    }

    @Override
    public PositionedStack getResultStack(int recipe) {
        CachedRecipeBlackRitual ritual = (CachedRecipeBlackRitual) this.arecipes.get(recipe);
        return new PositionedStack(ritual.recipe.getRecipeOutput(), 72, 72);
    }
}
