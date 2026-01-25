package dev.moma.ecopicks.entity.client;

import dev.moma.ecopicks.EcoPicks;
import dev.moma.ecopicks.entity.custom.LeafeeEntity;
import net.minecraft.client.render.entity.EntityRendererFactory.Context;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.util.Identifier;

public class LeafeeRenderer extends MobEntityRenderer<LeafeeEntity, LeafeeModel<LeafeeEntity>> {
    public LeafeeRenderer(Context context) {
        super(context, new LeafeeModel<>(context.getPart(LeafeeModel.LEAFEE)), 0.45F);
    }

    @Override
    public Identifier getTexture(LeafeeEntity entity) {
        return Identifier.of(EcoPicks.MOD_ID, "textures/entity/leafee/leafee.png");
    }

    @Override
    public void render(LeafeeEntity livingEntity, float f, float g, MatrixStack matrixStack,
            VertexConsumerProvider vertexConsumerProvider, int i) {

        if (livingEntity.isBaby()) {
            matrixStack.scale(0.75F, 0.75F, 0.75F);
        } else {
            matrixStack.scale(1.0F, 1.0F, 1.0F);
        }

        super.render(livingEntity, f, g, matrixStack, vertexConsumerProvider, i);
    }
}
