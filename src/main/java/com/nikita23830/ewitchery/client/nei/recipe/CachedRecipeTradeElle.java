package com.nikita23830.ewitchery.client.nei.recipe;

import codechicken.nei.PositionedStack;
import com.nikita23830.ewitchery.client.nei.TemplateRecipeHandler;
import com.nikita23830.ewitchery.common.recipes.RecipeTradeElle;

import java.util.ArrayList;
import java.util.List;

public class CachedRecipeTradeElle extends TemplateRecipeHandler.CachedRecipe {
    public final RecipeTradeElle recipe;

    public CachedRecipeTradeElle(RecipeTradeElle recipe) {
        this.recipe = recipe;
    }

    @Override
    public List<PositionedStack> getIngredients() {
        ArrayList<PositionedStack> list = new ArrayList<>();

        return list;
    }

    @Override
    public PositionedStack getResult() {
        return null;
    }
}
