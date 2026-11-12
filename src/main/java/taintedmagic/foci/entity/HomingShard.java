package taintedmagic.foci.entity;

import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;
import thaumcraft.entity.CombatUtils;
import thaumcraft.fx.Fx;
import thaumcraft.registry.ModSounds;
import taintedmagic.foci.FociFeature;

public class HomingShard extends ThrowableProjectile {
    private static final EntityDataAccessor<Byte> DATA_STRENGTH = SynchedEntityData.defineId(HomingShard.class, EntityDataSerializers.BYTE);
    public static final int LIFETIME = 300;

    private @Nullable LivingEntity target;
    private @Nullable Class<?> targetClass;
    private boolean persistent;

    public HomingShard(EntityType<? extends HomingShard> type, Level level) {
        super(type, level);
    }

    public static HomingShard create(ServerLevel level, LivingEntity thrower, LivingEntity target, int strength, boolean persistent) {
        HomingShard shard = new HomingShard(FociFeature.HOMING_SHARD.get(), level);
        shard.setOwner(thrower);
        shard.target = target;
        shard.targetClass = target.getClass();
        shard.persistent = persistent;
        shard.setStrength(strength);
        Vec3 look = thrower.getLookAngle();
        shard.snapTo(thrower.getX() + look.x / 2.0, thrower.getEyeY() + look.y / 2.0, thrower.getZ() + look.z / 2.0, thrower.getYRot(), thrower.getXRot());
        float yaw = thrower.getYRot() + (shard.random.nextFloat() - shard.random.nextFloat()) * 60.0F;
        float pitch = thrower.getXRot() + (shard.random.nextFloat() - shard.random.nextFloat()) * 60.0F;
        float speed = 0.5F;
        shard.setDeltaMovement(
            -Mth.sin(yaw / 180.0F * (float) Math.PI) * Mth.cos(pitch / 180.0F * (float) Math.PI) * speed,
            -Mth.sin(pitch / 180.0F * (float) Math.PI) * speed,
            Mth.cos(yaw / 180.0F * (float) Math.PI) * Mth.cos(pitch / 180.0F * (float) Math.PI) * speed
        );
        return shard;
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        builder.define(DATA_STRENGTH, (byte) 0);
    }

    public void setStrength(int strength) {
        this.entityData.set(DATA_STRENGTH, (byte) strength);
    }

    public int getStrength() {
        return this.entityData.get(DATA_STRENGTH);
    }

    public @Nullable LivingEntity target() {
        return target;
    }

    @Override
    protected double getDefaultGravity() {
        return 0.0;
    }

    @Override
    protected void onHitEntity(EntityHitResult hit) {
        if (this.level() instanceof ServerLevel level && hit.getEntity() != this.getOwner()) {
            Entity owner = this.getOwner();
            hit.getEntity().hurtServer(level, level.damageSources().indirectMagic(this, owner), 2.0F + this.getStrength() * 0.5F);
            level.playSound(null, this.getX(), this.getY(), this.getZ(), ModSounds.ZAP.get(), SoundSource.PLAYERS, 1.0F, 1.0F + (this.random.nextFloat() - this.random.nextFloat()) * 0.2F);
            level.broadcastEntityEvent(this, (byte) 16);
            this.discard();
        }
    }

    @Override
    protected void onHitBlock(BlockHitResult hit) {
        BlockState state = this.level().getBlockState(hit.getBlockPos());
        if (!passable(state)) {
            this.setDeltaMovement(this.getDeltaMovement().scale(-0.8));
        }
    }

    public static boolean passable(BlockState state) {
        return state.isAir() || state.is(BlockTags.LEAVES) || state.is(Blocks.NETHER_PORTAL) || state.is(Blocks.END_PORTAL) || state.is(Blocks.COBWEB) || state.canBeReplaced();
    }

    @Override
    public void tick() {
        Vec3 motion = this.getDeltaMovement();
        if (this.level().isClientSide() && Math.abs(motion.x) + Math.abs(motion.y) + Math.abs(motion.z) > 0.0) {
            Fx.get().sparkle(
                this.level(),
                (float) this.getX() + (this.random.nextFloat() - this.random.nextFloat()) * 0.25F,
                (float) this.getY() + (this.random.nextFloat() - this.random.nextFloat()) * 0.25F,
                (float) this.getZ() + (this.random.nextFloat() - this.random.nextFloat()) * 0.25F,
                0.5F,
                0,
                0.0F
            );
        }
        super.tick();
        if (this.isRemoved()) {
            return;
        }
        if (this.level() instanceof ServerLevel level) {
            if (this.persistent && (this.target == null || !this.target.isAlive() || this.target.distanceToSqr(this) > 1250.0) && this.targetClass != null) {
                Entity owner = this.getOwner();
                for (Entity entity : CombatUtils.getEntitiesInRange(level, this.getX(), this.getY(), this.getZ(), this, Entity.class, 16.0)) {
                    if (this.targetClass.isInstance(entity) && entity instanceof LivingEntity living && living.isAlive() && (owner == null || entity.getId() != owner.getId())) {
                        this.target = living;
                        break;
                    }
                }
            }
            if (this.target == null || !this.target.isAlive()) {
                level.broadcastEntityEvent(this, (byte) 16);
                this.discard();
                return;
            }
            if (this.tickCount > LIFETIME) {
                level.broadcastEntityEvent(this, (byte) 16);
                this.discard();
                return;
            }
            if (this.tickCount % 10 == 0) {
                double distance = this.distanceTo(this.target);
                this.setDeltaMovement(
                    (this.target.getX() - this.getX()) / distance,
                    (this.target.getBoundingBox().minY + this.target.getBbHeight() * 0.6 - this.getY()) / distance,
                    (this.target.getZ() - this.getZ()) / distance
                );
                this.needsSync = true;
                level.playSound(null, this.getX(), this.getY(), this.getZ(), taintedmagic.registry.ModSounds.SHARD.get(), SoundSource.PLAYERS, 0.1F, 2.0F * this.random.nextFloat());
            }
        }
        this.setDeltaMovement(this.getDeltaMovement().scale(0.85));
    }

    @Override
    public void handleEntityEvent(byte id) {
        if (id == 16) {
            Fx.get().shockBurst(this.level(), this.getX(), this.getY(), this.getZ(), 0.3F);
        } else {
            super.handleEntityEvent(id);
        }
    }
}
