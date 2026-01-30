package dev.moma.ecopicks.entity.client;

import net.minecraft.client.render.entity.state.LivingEntityRenderState;
import net.minecraft.entity.AnimationState;
import net.minecraft.inventory.SimpleInventory;

public class LeafeeRenderState extends LivingEntityRenderState {
    public final AnimationState sittingAnimationState = new AnimationState();
    public final AnimationState idleAnimationState = new AnimationState();
    public SimpleInventory inventory = new SimpleInventory(7);
    public int idleAnimationTimeout = 0;
    public int progressTicks = 0;
    public static final int MAX_PROGRESS = 6000;
}
