package com.nikita23830.ewitchery.client.nei;

import codechicken.lib.gui.GuiDraw;
import codechicken.nei.ItemList;
import codechicken.nei.NEIClientConfig;
import codechicken.nei.NEIClientUtils;
import codechicken.nei.NEIServerUtils;
import codechicken.nei.PositionedStack;
import codechicken.nei.api.DefaultOverlayRenderer;
import codechicken.nei.api.IOverlayHandler;
import codechicken.nei.api.IRecipeOverlayRenderer;
import codechicken.nei.api.IStackPositioner;
import codechicken.nei.guihook.GuiContainerManager;
import codechicken.nei.guihook.IContainerInputHandler;
import codechicken.nei.guihook.IContainerTooltipHandler;
import codechicken.nei.recipe.*;
import com.google.common.base.Stopwatch;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.Container;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntityFurnace;
import org.lwjgl.opengl.GL11;

public abstract class TemplateRecipeHandler implements ICraftingHandler, IUsageHandler {
    protected static ReentrantLock lock = new ReentrantLock();
    private static Set<Item> efuels;
    public int cycleticks = Math.abs((int)System.currentTimeMillis());
    public ArrayList<TemplateRecipeHandler.CachedRecipe> arecipes = new ArrayList<>();
    public LinkedList<TemplateRecipeHandler.RecipeTransferRect> transferRects = new LinkedList<>();

    public static void findFuelsOnce() {
        lock.lock();

        try {
            if (FurnaceRecipeHandler.afuels == null || FurnaceRecipeHandler.afuels.isEmpty()) {
                findFuels(false);
            }
        } finally {
            lock.unlock();
        }

    }

    public static void findFuelsOnceParallel() {
        lock.lock();

        try {
            if (FurnaceRecipeHandler.afuels == null || FurnaceRecipeHandler.afuels.isEmpty()) {
                findFuels(true);
            }
        } finally {
            lock.unlock();
        }

    }

    private static void findFuels(boolean parallel) {
        efuels = new HashSet<>();
        efuels.add(Item.getItemFromBlock(Blocks.brown_mushroom));
        efuels.add(Item.getItemFromBlock(Blocks.red_mushroom));
        efuels.add(Item.getItemFromBlock(Blocks.standing_sign));
        efuels.add(Item.getItemFromBlock(Blocks.wall_sign));
        efuels.add(Item.getItemFromBlock(Blocks.wooden_door));
        efuels.add(Item.getItemFromBlock(Blocks.trapped_chest));
        Stopwatch stopwatch = Stopwatch.createStarted();
        if (parallel) {
            try {
                FurnaceRecipeHandler.afuels = (ArrayList)ItemList.forkJoinPool.submit(() -> {
                    return (ArrayList)ItemList.items.parallelStream().map(TemplateRecipeHandler::identifyFuel).filter(Objects::nonNull).collect(Collectors.toCollection(ArrayList::new));
                }).get();
            } catch (ExecutionException | InterruptedException var3) {
                FurnaceRecipeHandler.afuels = new ArrayList();
                var3.printStackTrace();
            }
        } else {
            FurnaceRecipeHandler.afuels = (ArrayList)ItemList.items.stream().map(TemplateRecipeHandler::identifyFuel).filter(Objects::nonNull).collect(Collectors.toCollection(ArrayList::new));
        }

        NEIClientConfig.logger.info("FindFuels took " + stopwatch.stop());
    }

    private static FurnaceRecipeHandler.FuelPair identifyFuel(ItemStack itemStack) {
        if (efuels.contains(itemStack.getItem())) {
            return null;
        } else {
            int burnTime = TileEntityFurnace.getItemBurnTime(itemStack);
            return burnTime <= 0 ? null : new FurnaceRecipeHandler.FuelPair(itemStack.copy(), burnTime);
        }
    }

    public TemplateRecipeHandler() {
        this.loadTransferRects();
        TemplateRecipeHandler.RecipeTransferRectHandler.registerRectsToGuis(this.getRecipeTransferRectGuis(), this.transferRects);
    }

    public void loadTransferRects() {
    }

    public void loadCraftingRecipes(String outputId, Object... results) {
        if (outputId.equals("item")) {
            this.loadCraftingRecipes((ItemStack)results[0]);
        }

    }

    public void loadCraftingRecipes(ItemStack result) {
    }

    public void loadUsageRecipes(String inputId, Object... ingredients) {
        if (inputId.equals("item")) {
            this.loadUsageRecipes((ItemStack)ingredients[0]);
        }

    }

    public void loadUsageRecipes(ItemStack ingredient) {
    }

    public abstract String getGuiTexture();

    public String getOverlayIdentifier() {
        return null;
    }

    public void drawExtras(int recipe) {
    }

    public void drawProgressBar(int x, int y, int tx, int ty, int w, int h, int ticks, int direction) {
        this.drawProgressBar(x, y, tx, ty, w, h, (float)(this.cycleticks % ticks) / (float)ticks, direction);
    }

    public void drawProgressBar(int x, int y, int tx, int ty, int w, int h, float completion, int direction) {
        if (direction > 3) {
            completion = 1.0F - completion;
            direction %= 4;
        }

        int var = (int)(completion * (float)(direction % 2 == 0 ? w : h));
        switch (direction) {
            case 0:
                GuiDraw.drawTexturedModalRect(x, y, tx, ty, var, h);
                break;
            case 1:
                GuiDraw.drawTexturedModalRect(x, y, tx, ty, w, var);
                break;
            case 2:
                GuiDraw.drawTexturedModalRect(x + w - var, y, tx + w - var, ty, var, h);
                break;
            case 3:
                GuiDraw.drawTexturedModalRect(x, y + h - var, tx, ty + h - var, w, var);
        }

    }

    public List<Class<? extends GuiContainer>> getRecipeTransferRectGuis() {
        Class<? extends GuiContainer> clazz = this.getGuiClass();
        if (clazz != null) {
            LinkedList<Class<? extends GuiContainer>> list = new LinkedList();
            list.add(clazz);
            return list;
        } else {
            return null;
        }
    }

    public Class<? extends GuiContainer> getGuiClass() {
        return null;
    }

    public TemplateRecipeHandler newInstance() {
        try {
            findFuelsOnce();
            return (TemplateRecipeHandler)this.getClass().newInstance();
        } catch (Exception var2) {
            throw new RuntimeException(var2);
        }
    }

    public ICraftingHandler getRecipeHandler(String outputId, Object... results) {
        TemplateRecipeHandler handler = this.newInstance();
        handler.loadCraftingRecipes(outputId, results);
        return handler;
    }

    public IUsageHandler getUsageHandler(String inputId, Object... ingredients) {
        TemplateRecipeHandler handler = this.newInstance();
        handler.loadUsageRecipes(inputId, ingredients);
        return handler;
    }

    public IUsageHandler getUsageAndCatalystHandler(String inputId, Object... ingredients) {
        TemplateRecipeHandler handler = this.newInstance();
        if (inputId.equals("item")) {
            ItemStack candidate = (ItemStack)ingredients[0];
            if (RecipeCatalysts.containsCatalyst(handler, candidate)) {
                Iterator var5 = this.transferRects.iterator();

                while(true) {
                    if (!var5.hasNext()) {
                        NEIClientConfig.logger.info("failed to load catalyst handler, implement `loadTransferRects` for your handler " + handler.getClass().getName());
                        break;
                    }

                    TemplateRecipeHandler.RecipeTransferRect rect = (TemplateRecipeHandler.RecipeTransferRect)var5.next();
                    if (this.specifyTransferRect() == null || Objects.equals(rect.outputId, this.specifyTransferRect())) {
                        handler.loadCraftingRecipes(rect.outputId, rect.results);
                        return handler;
                    }
                }
            }
        }

        return this.getUsageHandler(inputId, ingredients);
    }

    public String specifyTransferRect() {
        return null;
    }

    public int numRecipes() {
        return this.arecipes.size();
    }

    public void drawBackground(int recipe) {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GuiDraw.changeTexture(this.getGuiTexture());
        GuiDraw.drawTexturedModalRect(0, 0, 5, 11, 166, 65);
    }

    public void drawForeground(int recipe) {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glDisable(2896);
        GuiDraw.changeTexture(this.getGuiTexture());
        this.drawExtras(recipe);
    }

    public List<PositionedStack> getIngredientStacks(int recipe) {
        return ((TemplateRecipeHandler.CachedRecipe)this.arecipes.get(recipe)).getIngredients();
    }

    public PositionedStack getResultStack(int recipe) {
        return ((TemplateRecipeHandler.CachedRecipe)this.arecipes.get(recipe)).getResult();
    }

    public List<PositionedStack> getOtherStacks(int recipe) {
        return ((TemplateRecipeHandler.CachedRecipe)this.arecipes.get(recipe)).getOtherStacks();
    }

    public void onUpdate() {
        if (!NEIClientUtils.shiftKey()) {
            ++this.cycleticks;
        }

    }

    public boolean hasOverlay(GuiContainer gui, Container container, int recipe) {
        return RecipeInfo.hasDefaultOverlay(gui, this.getOverlayIdentifier()) || RecipeInfo.hasOverlayHandler(gui, this.getOverlayIdentifier());
    }

    public IRecipeOverlayRenderer getOverlayRenderer(GuiContainer gui, int recipe) {
        IStackPositioner positioner = RecipeInfo.getStackPositioner(gui, this.getOverlayIdentifier());
        return positioner == null ? null : new DefaultOverlayRenderer(this.getIngredientStacks(recipe), positioner);
    }

    public IOverlayHandler getOverlayHandler(GuiContainer gui, int recipe) {
        return RecipeInfo.getOverlayHandler(gui, this.getOverlayIdentifier());
    }

    public int recipiesPerPage() {
        return 2;
    }

    public List<String> handleTooltip(GuiRecipe gui, List<String> currenttip, int recipe) {
        if (GuiContainerManager.shouldShowTooltip(gui) && currenttip.size() == 0) {
            Point offset = gui.getRecipePosition(recipe);
            currenttip = transferRectTooltip(gui, this.transferRects, offset.x, offset.y, currenttip);
        }

        return currenttip;
    }

    public List<String> handleItemTooltip(GuiRecipe gui, ItemStack stack, List<String> currenttip, int recipe) {
        return currenttip;
    }

    public boolean keyTyped(GuiRecipe gui, char keyChar, int keyCode, int recipe) {
        if (keyCode == NEIClientConfig.getKeyBinding("gui.recipe")) {
            return this.transferRect(gui, recipe, false);
        } else {
            return keyCode == NEIClientConfig.getKeyBinding("gui.usage") ? this.transferRect(gui, recipe, true) : false;
        }
    }

    public boolean mouseClicked(GuiRecipe gui, int button, int recipe) {
        if (button == 0) {
            return this.transferRect(gui, recipe, false);
        } else {
            return button == 1 ? this.transferRect(gui, recipe, true) : false;
        }
    }

    public boolean mouseScrolled(GuiRecipe gui, int scroll, int recipe) {
        return false;
    }

    private boolean transferRect(GuiRecipe gui, int recipe, boolean usage) {
        Point offset = gui.getRecipePosition(recipe);
        return transferRect(gui, this.transferRects, offset.x, offset.y, usage);
    }

    private static boolean transferRect(GuiContainer gui, Collection<TemplateRecipeHandler.RecipeTransferRect> transferRects, int offsetx, int offsety, boolean usage) {
        Point pos = GuiDraw.getMousePosition();
        Point relMouse = new Point(pos.x - gui.guiLeft - offsetx, pos.y - gui.guiTop - offsety);
        Iterator var7 = transferRects.iterator();

        while(true) {
            TemplateRecipeHandler.RecipeTransferRect rect;
            do {
                if (!var7.hasNext()) {
                    return false;
                }

                rect = (TemplateRecipeHandler.RecipeTransferRect)var7.next();
            } while(!rect.rect.contains(relMouse));

            if (usage) {
                if (GuiUsageRecipe.openRecipeGui(rect.outputId, rect.results)) {
                    break;
                }
            } else if (GuiCraftingRecipe.openRecipeGui(rect.outputId, rect.results)) {
                break;
            }
        }

        return true;
    }

    private static List<String> transferRectTooltip(GuiContainer gui, Collection<TemplateRecipeHandler.RecipeTransferRect> transferRects, int offsetx, int offsety, List<String> currenttip) {
        Point pos = GuiDraw.getMousePosition();
        Point relMouse = new Point(pos.x - gui.guiLeft - offsetx, pos.y - gui.guiTop - offsety);
        Iterator var7 = transferRects.iterator();

        while(var7.hasNext()) {
            TemplateRecipeHandler.RecipeTransferRect rect = (TemplateRecipeHandler.RecipeTransferRect)var7.next();
            if (rect.rect.contains(relMouse)) {
                currenttip.add(NEIClientUtils.translate("recipe.tooltip", new Object[0]));
                break;
            }
        }

        return currenttip;
    }

    static {
        GuiContainerManager.addInputHandler(new TemplateRecipeHandler.RecipeTransferRectHandler());
        GuiContainerManager.addTooltipHandler(new TemplateRecipeHandler.RecipeTransferRectHandler());
    }

    public static class RecipeTransferRectHandler implements IContainerInputHandler, IContainerTooltipHandler {
        private static HashMap<Class<? extends GuiContainer>, HashSet<TemplateRecipeHandler.RecipeTransferRect>> guiMap = new HashMap();

        public RecipeTransferRectHandler() {
        }

        public static void registerRectsToGuis(List<Class<? extends GuiContainer>> classes, List<TemplateRecipeHandler.RecipeTransferRect> rects) {
            if (classes != null) {
                Iterator var2 = classes.iterator();

                while(var2.hasNext()) {
                    Class<? extends GuiContainer> clazz = (Class)var2.next();
                    HashSet<TemplateRecipeHandler.RecipeTransferRect> set = (HashSet)guiMap.computeIfAbsent(clazz, (k) -> {
                        return new HashSet();
                    });
                    set.addAll(rects);
                }

            }
        }

        public boolean canHandle(GuiContainer gui) {
            return guiMap.containsKey(gui.getClass());
        }

        public boolean lastKeyTyped(GuiContainer gui, char keyChar, int keyCode) {
            if (!this.canHandle(gui)) {
                return false;
            } else if (keyCode == NEIClientConfig.getKeyBinding("gui.recipe")) {
                return this.transferRect(gui, false);
            } else {
                return keyCode == NEIClientConfig.getKeyBinding("gui.usage") ? this.transferRect(gui, true) : false;
            }
        }

        public boolean mouseClicked(GuiContainer gui, int mousex, int mousey, int button) {
            if (!this.canHandle(gui)) {
                return false;
            } else if (button == 0) {
                return this.transferRect(gui, false);
            } else {
                return button == 1 ? this.transferRect(gui, true) : false;
            }
        }

        private boolean transferRect(GuiContainer gui, boolean usage) {
            int[] offset = RecipeInfo.getGuiOffset(gui);
            return TemplateRecipeHandler.transferRect(gui, (Collection)guiMap.get(gui.getClass()), offset[0], offset[1], usage);
        }

        public void onKeyTyped(GuiContainer gui, char keyChar, int keyID) {
        }

        public void onMouseClicked(GuiContainer gui, int mousex, int mousey, int button) {
        }

        public void onMouseUp(GuiContainer gui, int mousex, int mousey, int button) {
        }

        public boolean keyTyped(GuiContainer gui, char keyChar, int keyID) {
            return false;
        }

        public boolean mouseScrolled(GuiContainer gui, int mousex, int mousey, int scrolled) {
            return false;
        }

        public void onMouseScrolled(GuiContainer gui, int mousex, int mousey, int scrolled) {
        }

        public List<String> handleTooltip(GuiContainer gui, int mousex, int mousey, List<String> currenttip) {
            if (!this.canHandle(gui)) {
                return currenttip;
            } else {
                if (GuiContainerManager.shouldShowTooltip(gui) && currenttip.size() == 0) {
                    int[] offset = RecipeInfo.getGuiOffset(gui);
                    currenttip = TemplateRecipeHandler.transferRectTooltip(gui, (Collection)guiMap.get(gui.getClass()), offset[0], offset[1], currenttip);
                }

                return currenttip;
            }
        }

        public List<String> handleItemDisplayName(GuiContainer gui, ItemStack itemstack, List<String> currenttip) {
            return currenttip;
        }

        public List<String> handleItemTooltip(GuiContainer gui, ItemStack itemstack, int mousex, int mousey, List<String> currenttip) {
            return currenttip;
        }

        public void onMouseDragged(GuiContainer gui, int mousex, int mousey, int button, long heldTime) {
        }
    }

    public static class RecipeTransferRect {
        Rectangle rect;
        String outputId;
        Object[] results;

        public RecipeTransferRect(Rectangle rectangle, String outputId, Object... results) {
            this.rect = rectangle;
            this.outputId = outputId;
            this.results = results;
        }

        public boolean equals(Object obj) {
            return !(obj instanceof TemplateRecipeHandler.RecipeTransferRect) ? false : this.rect.equals(((TemplateRecipeHandler.RecipeTransferRect)obj).rect);
        }

        public int hashCode() {
            return this.rect.hashCode();
        }
    }

    public abstract static class CachedRecipe {
        final long offset = System.currentTimeMillis();

        public CachedRecipe() {
        }

        public abstract PositionedStack getResult();

        public List<PositionedStack> getIngredients() {
            ArrayList<PositionedStack> stacks = new ArrayList();
            PositionedStack stack = this.getIngredient();
            if (stack != null) {
                stacks.add(stack);
            }

            return stacks;
        }

        public PositionedStack getIngredient() {
            return null;
        }

        public List<PositionedStack> getOtherStacks() {
            ArrayList<PositionedStack> stacks = new ArrayList();

            try {
                PositionedStack stack = this.getOtherStack();
                if (stack != null) {
                    stacks.add(stack);
                }
            } catch (ArithmeticException var3) {
                NEIClientConfig.logger.error("Error in getOtherStacks: " + var3);
            }

            return stacks;
        }

        public PositionedStack getOtherStack() {
            return null;
        }

        public List<PositionedStack> getCycledIngredients(int cycle, List<PositionedStack> ingredients) {
            for(int itemIndex = 0; itemIndex < ingredients.size(); ++itemIndex) {
                this.randomRenderPermutation((PositionedStack)ingredients.get(itemIndex), (long)(cycle + itemIndex));
            }

            return ingredients;
        }

        public void randomRenderPermutation(PositionedStack stack, long cycle) {
            Random rand = new Random(cycle + this.offset);
            stack.setPermutationToRender(Math.abs(rand.nextInt()) % stack.items.length);
        }

        public void setIngredientPermutation(Collection<PositionedStack> ingredients, ItemStack ingredient) {
            Iterator var3 = ingredients.iterator();

            while(true) {
                while(var3.hasNext()) {
                    PositionedStack stack = (PositionedStack)var3.next();

                    for(int i = 0; i < stack.items.length; ++i) {
                        if (NEIServerUtils.areStacksSameTypeCrafting(ingredient, stack.items[i])) {
                            stack.item = stack.items[i];
                            stack.item.setItemDamage(ingredient.getItemDamage());
                            if (ingredient.hasTagCompound()) {
                                stack.item.setTagCompound((NBTTagCompound)ingredient.getTagCompound().copy());
                            }

                            stack.items = new ItemStack[]{stack.item};
                            stack.setPermutationToRender(0);
                            break;
                        }
                    }
                }

                return;
            }
        }

        public boolean contains(Collection<PositionedStack> ingredients, ItemStack ingredient) {
            Iterator var3 = ingredients.iterator();

            PositionedStack stack;
            do {
                if (!var3.hasNext()) {
                    return false;
                }

                stack = (PositionedStack)var3.next();
            } while(!stack.contains(ingredient));

            return true;
        }

        public boolean containsWithNBT(Collection<PositionedStack> ingredients, ItemStack ingredient) {
            Iterator var3 = ingredients.iterator();

            PositionedStack stack;
            do {
                if (!var3.hasNext()) {
                    return false;
                }

                stack = (PositionedStack)var3.next();
            } while(!stack.containsWithNBT(ingredient));

            return true;
        }

        public boolean contains(Collection<PositionedStack> ingredients, Item ingred) {
            Iterator var3 = ingredients.iterator();

            PositionedStack stack;
            do {
                if (!var3.hasNext()) {
                    return false;
                }

                stack = (PositionedStack)var3.next();
            } while(!stack.contains(ingred));

            return true;
        }
    }
}
