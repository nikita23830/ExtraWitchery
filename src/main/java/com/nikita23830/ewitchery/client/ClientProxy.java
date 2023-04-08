package com.nikita23830.ewitchery.client;

import alfheim.client.model.entity.ModelEntityElf;
import com.meteor.extrabotany.client.render.block.RenderBlockAutoPlate;
import com.meteor.extrabotany.client.render.entity.RenderAsgardNpc;
import com.meteor.extrabotany.common.entity.EntityAsgard;
import com.nikita23830.ewitchery.client.models.ModelBehemoth;
import com.nikita23830.ewitchery.client.renderBlock.RenderBlockPedestalElfArrow;
import com.nikita23830.ewitchery.client.renderEntity.*;
import com.nikita23830.ewitchery.client.renderEntity.projectile.RenderArrow;
import com.nikita23830.ewitchery.client.renderTiles.RenderTileCircle;
import com.nikita23830.ewitchery.client.renderTiles.RenderTilePedestalElfArrow;
import com.nikita23830.ewitchery.common.CommonProxy;
import com.nikita23830.ewitchery.common.entity.*;
import com.nikita23830.ewitchery.common.entity.projectile.EntityElfArrow;
import com.nikita23830.ewitchery.common.entity.projectile.EntityModArrow;
import com.nikita23830.ewitchery.common.tiles.TileCirle;
import com.nikita23830.ewitchery.common.tiles.TilePedestalElfArrow;
import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.event.FMLConstructionEvent;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import net.minecraft.client.model.ModelBiped;
import net.minecraftforge.common.MinecraftForge;

public class ClientProxy extends CommonProxy {
    public static int renderPedestalElfArrow = -1;

    public void cons(FMLConstructionEvent event) {
        super.cons(event);
    }

    public void preInit(FMLPreInitializationEvent event) {
        super.preInit(event);
    }

    public void init(FMLInitializationEvent event) {
        super.init(event);
        renderPedestalElfArrow = RenderingRegistry.getNextAvailableRenderId();

        initBlockRender();
        initTileRender();
        initEntityRender();

        MinecraftForge.EVENT_BUS.register(new HUDHandler());
    }

    private void initBlockRender() {
        RenderingRegistry.registerBlockHandler(new RenderBlockPedestalElfArrow());
    }

    private void initTileRender() {
        ClientRegistry.bindTileEntitySpecialRenderer(TileCirle.class, new RenderTileCircle());
        ClientRegistry.bindTileEntitySpecialRenderer(TilePedestalElfArrow.class, new RenderTilePedestalElfArrow());
    }

    private void initEntityRender() {
        RenderingRegistry.registerEntityRenderingHandler(EntityDarkElf.class, new RenderDarkElf());
        RenderingRegistry.registerEntityRenderingHandler(EntityCursedElf.class, new RenderCursedElf());
        RenderingRegistry.registerEntityRenderingHandler(EntityBehemoth.class, new RenderBehemoth());
        RenderingRegistry.registerEntityRenderingHandler(EntityGuardian.class, new RenderGuardian());
        RenderingRegistry.registerEntityRenderingHandler(EntityPhantom.class, new RenderPhantom());
        RenderingRegistry.registerEntityRenderingHandler(EntityBeholder.class, new RenderBeholder());
        RenderingRegistry.registerEntityRenderingHandler(EntityJabberwock.class, new RenderJabberwock());
        RenderingRegistry.registerEntityRenderingHandler(EntityTrent.class, new RenderTrent());
        RenderingRegistry.registerEntityRenderingHandler(EntityEnt.class, new RenderEnt());
        RenderingRegistry.registerEntityRenderingHandler(EntityElly.class, new RenderElly(new ModelBiped()));

        RenderingRegistry.registerEntityRenderingHandler(EntityModArrow.class, new RenderArrow());
    }

    public void postInit(FMLPostInitializationEvent event) {
        super.postInit(event);
    }
}
