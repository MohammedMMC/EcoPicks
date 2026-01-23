package dev.moma.ecopicks;

import dev.moma.ecopicks.entity.ModEntities;
import dev.moma.ecopicks.entity.client.LeafeeModel;
import dev.moma.ecopicks.entity.client.LeafeeRenderer;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;

public class EcoPicksClient implements ClientModInitializer{

    @Override
    public void onInitializeClient() {
        EntityModelLayerRegistry.registerModelLayer(LeafeeModel.LEAFEE, LeafeeModel::getTexturedModelData);
        EntityRendererRegistry.register(ModEntities.LEAFEE, LeafeeRenderer::new);
    }
    
}
