package dev.moma.ecopicks.entity.client;

import dev.moma.ecopicks.EcoPicks;
import dev.moma.ecopicks.entity.custom.LeafeeEntity;
import net.minecraft.client.render.entity.EntityRendererFactory.Context;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.util.Identifier;

public class LeafeeRenderer extends MobEntityRenderer<LeafeeEntity, LeafeeRenderState, LeafeeModel> {
    public LeafeeRenderer(Context context) {
        super(context, new LeafeeModel(context.getPart(LeafeeModel.LEAFEE)), 0.45F);
    }
    
    @Override
    public Identifier getTexture(LeafeeRenderState state) {
        return Identifier.of(EcoPicks.MOD_ID, "textures/entity/leafee/leafee.png");
    }

    @Override
    public void render(LeafeeRenderState state, MatrixStack matrixStack,
            VertexConsumerProvider vertexConsumerProvider, int i) {

        if (state.baby) {
            matrixStack.scale(0.75F, 0.75F, 0.75F);
        } else {
            matrixStack.scale(1.0F, 1.0F, 1.0F);
        }

        super.render(state, matrixStack, vertexConsumerProvider, i);
    }

    @Override
    public LeafeeRenderState createRenderState() {
        return new LeafeeRenderState();
    }

    @Override
    public void updateRenderState(LeafeeEntity livingEntity, LeafeeRenderState livingEntityRenderState, float f) {
        super.updateRenderState(livingEntity, livingEntityRenderState, f);
        livingEntityRenderState.sittingAnimationState.copyFrom(livingEntity.sittingAnimationState);
        livingEntityRenderState.idleAnimationState.copyFrom(livingEntity.idleAnimationState);
        livingEntityRenderState.inventory = livingEntity.inventory;
        livingEntityRenderState.idleAnimationTimeout = livingEntity.idleAnimationTimeout;
        livingEntityRenderState.progressTicks = livingEntity.progressTicks;
    }
}
