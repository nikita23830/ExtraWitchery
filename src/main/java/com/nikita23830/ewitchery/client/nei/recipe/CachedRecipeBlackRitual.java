package com.nikita23830.ewitchery.client.nei.recipe;

import codechicken.nei.PositionedStack;
import com.nikita23830.ewitchery.client.nei.TemplateRecipeHandler;
import com.nikita23830.ewitchery.common.recipes.RecipeArmorSplit;
import com.nikita23830.ewitchery.common.recipes.RecipeBlackRitual;

import java.util.ArrayList;
import java.util.List;

public class CachedRecipeBlackRitual extends TemplateRecipeHandler.CachedRecipe {
    public final RecipeBlackRitual recipe;

    public CachedRecipeBlackRitual(RecipeBlackRitual recipe) {
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
