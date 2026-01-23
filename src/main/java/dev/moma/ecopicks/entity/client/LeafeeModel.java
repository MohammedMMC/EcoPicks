package dev.moma.ecopicks.entity.client;

import dev.moma.ecopicks.EcoPicks;
import dev.moma.ecopicks.entity.custom.LeafeeEntity;
import net.minecraft.client.model.Dilation;
import net.minecraft.client.model.ModelData;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.model.ModelPartBuilder;
import net.minecraft.client.model.ModelPartData;
import net.minecraft.client.model.ModelTransform;
import net.minecraft.client.model.TexturedModelData;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.client.render.entity.model.SinglePartEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;

public class LeafeeModel<T extends LeafeeEntity> extends SinglePartEntityModel<T> {
        public static final EntityModelLayer LEAFEE = new EntityModelLayer(Identifier.of(EcoPicks.MOD_ID, "leafee"),
                        "main");

        private final ModelPart bones;
        private final ModelPart left_leg;
        private final ModelPart left_leg_part;
        private final ModelPart body;
        private final ModelPart ears;
        private final ModelPart right_leg;
        private final ModelPart right_leg_part;

        public LeafeeModel(ModelPart root) {
                this.bones = root.getChild("bones");
                this.left_leg = this.bones.getChild("left_leg");
                this.left_leg_part = this.left_leg.getChild("left_leg_part");
                this.body = this.bones.getChild("body");
                this.ears = this.body.getChild("ears");
                this.right_leg = this.bones.getChild("right_leg");
                this.right_leg_part = this.right_leg.getChild("right_leg_part");
        }

        public static TexturedModelData getTexturedModelData() {
                ModelData modelData = new ModelData();
                ModelPartData modelPartData = modelData.getRoot();
                ModelPartData bones = modelPartData.addChild("bones", ModelPartBuilder.create(),
                                ModelTransform.pivot(0.0F, 24.0F, 0.0F));

                ModelPartData left_leg = bones.addChild("left_leg",
                                ModelPartBuilder.create().uv(24, 24).cuboid(-1.0F, 0.0F, -1.0F, 2.0F, 4.0F, 2.0F,
                                                new Dilation(0.0F)),
                                ModelTransform.pivot(3.0F, -8.0F, 0.0F));

                ModelPartData left_leg_part = left_leg.addChild("left_leg_part",
                                ModelPartBuilder.create().uv(0, 30)
                                                .cuboid(-1.0F, 0.0F, -1.0F, 2.0F, 4.0F, 2.0F, new Dilation(0.0F))
                                                .uv(0, 20)
                                                .cuboid(-2.0F, 4.0F, -2.0F, 4.0F, 0.0F, 4.0F, new Dilation(0.0F)),
                                ModelTransform.pivot(0.0F, 4.0F, 0.0F));

                ModelPartData body = bones.addChild("body",
                                ModelPartBuilder.create().uv(0, 0)
                                                .cuboid(-5.0F, -10.0F, -5.0F, 10.0F, 10.0F, 10.0F, new Dilation(0.0F))
                                                .uv(9, 3)
                                                .cuboid(5.0F, 0.0F, -5.0F, 0.0F, 1.0F, 10.0F, new Dilation(0.0F))
                                                .uv(12, 8)
                                                .cuboid(-5.0F, 0.0F, -4.0F, 0.0F, 1.0F, 8.0F, new Dilation(0.0F)),
                                ModelTransform.pivot(0.0F, -8.0F, 0.0F));

                ModelPartData cube_r1 = body.addChild("cube_r1",
                                ModelPartBuilder.create().uv(0, 4).cuboid(0.0F, 0.0F, -9.0F, 0.0F, 1.0F, 9.0F,
                                                new Dilation(0.0F)),
                                ModelTransform.of(-5.0F, 0.0F, 5.0F, 0.0F, -1.5708F, 0.0F));

                ModelPartData cube_r2 = body.addChild("cube_r2",
                                ModelPartBuilder.create().uv(21, 5).cuboid(0.0F, 0.0F, -8.0F, 0.0F, 1.0F, 8.0F,
                                                new Dilation(0.0F)),
                                ModelTransform.of(-5.0F, 0.0F, -5.0F, 0.0F, -1.5708F, 0.0F));

                ModelPartData ears = body.addChild("ears",
                                ModelPartBuilder.create().uv(0, 24)
                                                .cuboid(3.0F, -2.0F, -1.0F, 4.0F, 4.0F, 2.0F, new Dilation(0.0F))
                                                .uv(12, 24)
                                                .cuboid(-7.0F, -2.0F, -1.0F, 4.0F, 4.0F, 2.0F, new Dilation(0.0F)),
                                ModelTransform.pivot(0.0F, -10.0F, 0.0F));

                ModelPartData right_leg = bones.addChild("right_leg",
                                ModelPartBuilder.create().uv(8, 30).cuboid(-1.0F, 0.0F, -1.0F, 2.0F, 4.0F, 2.0F,
                                                new Dilation(0.0F)),
                                ModelTransform.pivot(-3.0F, -8.0F, 0.0F));

                ModelPartData right_leg_part = right_leg.addChild("right_leg_part",
                                ModelPartBuilder.create().uv(16, 30)
                                                .cuboid(-1.0F, 0.0F, -1.0F, 2.0F, 4.0F, 2.0F, new Dilation(0.0F))
                                                .uv(16, 20)
                                                .cuboid(-2.0F, 4.0F, -2.0F, 4.0F, 0.0F, 4.0F, new Dilation(0.0F)),
                                ModelTransform.pivot(0.0F, 4.0F, 0.0F));
                return TexturedModelData.of(modelData, 64, 64);
        }

        @Override
        public void setAngles(LeafeeEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks,
                        float netHeadYaw, float headPitch) {
                this.getPart().traverse().forEach(ModelPart::resetTransform);

                this.animateMovement(LeafeeAnimations.WALK, limbSwing, limbSwingAmount, 2F, 2.5F);
                this.updateAnimation(entity.idleAnimationState, LeafeeAnimations.IDLE, ageInTicks, 1F);

                this.setHeadAngles(netHeadYaw, headPitch);
        }

        private void setHeadAngles(float headYaw, float headPitch) {
                headYaw = MathHelper.clamp(headYaw, -30.0F, 30.0F);
                headPitch = MathHelper.clamp(headPitch, -25.0F, 45.0F);

                this.body.yaw = headYaw * MathHelper.RADIANS_PER_DEGREE;
                this.body.pitch = headPitch * MathHelper.RADIANS_PER_DEGREE;
        }

        @Override
        public void render(MatrixStack matrices, VertexConsumer vertexConsumer, int light, int overlay, int color) {
                bones.render(matrices, vertexConsumer, light, overlay, color);
        }

        @Override
        public ModelPart getPart() {
                return bones;
        }
}
