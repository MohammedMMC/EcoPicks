package dev.moma.ecopicks.entity.custom;

import dev.moma.ecopicks.entity.ModEntities;
import net.minecraft.block.BlockState;
import net.minecraft.entity.AnimationState;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.TargetPredicate;
import net.minecraft.entity.ai.TargetPredicate;
import net.minecraft.entity.ai.goal.AttackWithOwnerGoal;
import net.minecraft.entity.ai.goal.FollowOwnerGoal;
import net.minecraft.entity.ai.goal.FollowParentGoal;
import net.minecraft.entity.ai.goal.LookAroundGoal;
import net.minecraft.entity.ai.goal.LookAtEntityGoal;
import net.minecraft.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.entity.ai.goal.SitGoal;
import net.minecraft.entity.ai.goal.SwimGoal;
import net.minecraft.entity.ai.goal.TemptGoal;
import net.minecraft.entity.ai.goal.RevengeGoal;
import net.minecraft.entity.ai.goal.TrackOwnerAttackerGoal;
import net.minecraft.entity.ai.goal.WanderAroundFarGoal;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.recipe.Ingredient;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class LeafeeEntity extends TameableEntity {
    public final AnimationState sittingAnimationState = new AnimationState();
    public final AnimationState idleAnimationState = new AnimationState();
    private int idleAnimationTimeout = 0;

    public LeafeeEntity(EntityType<? extends TameableEntity> entityType, World world) {
        super(entityType, world);
    }

    @Override
    protected void initGoals() {
        this.goalSelector.add(0, new SwimGoal(this));
        this.goalSelector.add(1, new SitGoal(this));
        this.goalSelector.add(2, new TemptGoal(this, 1.2D, Ingredient.ofItems(Items.APPLE), false));
        this.goalSelector.add(3, new MeleeAttackGoal(this, 1.2D, true));
        this.goalSelector.add(4, new FollowOwnerGoal(this, 1.1D, 5.0F, 2.0F));
        this.goalSelector.add(5, new FollowParentGoal(this, 1.05D));
        this.goalSelector.add(6, new WanderAroundFarGoal(this, 1.0D));
        this.goalSelector.add(7, new LookAtEntityGoal(this, PlayerEntity.class, 6.0F));
        this.goalSelector.add(8, new LookAroundGoal(this));

        this.targetSelector.add(1, new TrackOwnerAttackerGoal(this));
        this.targetSelector.add(2, new AttackWithOwnerGoal(this));
        this.targetSelector.add(3, new RevengeGoal(this));
    }

    public static DefaultAttributeContainer.Builder createAttributes() {
        return TameableEntity.createMobAttributes()
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 20)
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.35)
                .add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 3)
                .add(EntityAttributes.GENERIC_GRAVITY, 0.06)
                .add(EntityAttributes.GENERIC_FOLLOW_RANGE, 20);
    }

    @Override
    public ActionResult interactMob(PlayerEntity player, Hand hand) {
        ItemStack stack = player.getStackInHand(hand);

        if (!this.isTamed() && stack.isOf(Items.APPLE)) {
            if (!player.getAbilities().creativeMode)
                stack.decrement(1);

            if (!this.getWorld().isClient) {
                if (this.getRandom().nextInt(3) == 0) {
                    this.setOwner(player);
                    this.navigation.stop();
                    this.getWorld().sendEntityStatus(this, (byte) 7);
                } else {
                    this.getWorld().sendEntityStatus(this, (byte) 6);
                }
            }
            return ActionResult.success(this.getWorld().isClient);
        }

        if (!super.interactMob(player, hand).isAccepted() && this.isTamed() && this.isOwner(player)
                && hand == Hand.MAIN_HAND) {
            if (!this.getWorld().isClient) {
                boolean newSitting = !this.isSitting();
                this.setSitting(newSitting);
                this.setInSittingPose(newSitting);
                this.navigation.stop();
                this.setTarget(null);
            }
            return ActionResult.SUCCESS_NO_ITEM_USED;
        }

        return super.interactMob(player, hand);
    }

    @Override
    protected void playStepSound(BlockPos pos, BlockState state) {
        this.playSound(SoundEvents.BLOCK_CHERRY_LEAVES_STEP, 0.6F, 1.0F);
    }

    private void setupAnimationStates() {
        if (this.isSitting() || this.isInSittingPose() || (!this.isOnGround() && this.getVelocity().y < 0)) {
            this.idleAnimationState.stop();
            this.sittingAnimationState.startIfNotRunning(this.age);
        } else {
            this.sittingAnimationState.stop();
            if (this.idleAnimationTimeout <= 0) {
                this.idleAnimationTimeout = 40;
                this.idleAnimationState.start(this.age);
            } else {
                --this.idleAnimationTimeout;
            }
        }
    }

    @Override
    public void tick() {
        super.tick();

        if (!this.isOnGround()) {
            var v = this.getVelocity();
            if (v.y < 0) {
                this.setVelocity(v.x, v.y * 0.80D, v.z);
            }
        }

        if (this.getWorld().isClient()) {
            this.setupAnimationStates();
        }
    }

    @Override
    public boolean isBreedingItem(ItemStack stack) {
        return stack.isOf(Items.APPLE);
    }

    @Override
    public PassiveEntity createChild(ServerWorld world, PassiveEntity entity) {
        LeafeeEntity child = ModEntities.LEAFEE.create(world);
        if (this.isTamed())
            child.setOwnerUuid(this.getOwnerUuid());
        return child;
    }

    @Override
    public boolean handleFallDamage(float fallDistance, float damageMultiplier, net.minecraft.entity.damage.DamageSource damageSource) {
        return false;
    }

}
