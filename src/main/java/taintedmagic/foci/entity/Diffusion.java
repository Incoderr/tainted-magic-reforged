package taintedmagic.foci.entity;

import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import net.minecraft.world.level.Level;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import thaumcraft.entity.CombatUtils;
import taintedmagic.foci.FociFeature;
import taintedmagic.fx.TmFx;

public class Diffusion extends ThrowableProjectile {
    public static final int LIFETIME = 20;

    private float damage;
    private boolean corrosive;

    public Diffusion(EntityType<? extends Diffusion> type, Level level) {
        super(type, level);
    }

    public static Diffusion create(ServerLevel level, LivingEntity thrower, float scatter, float damage, boolean corrosive) {
        Diffusion diffusion = new Diffusion(FociFeature.DIFFUSION_ENTITY.get(), level);
        CombatUtils.launch(diffusion, thrower, 1.5F);
        Vec3 motion = diffusion.getDeltaMovement();
        CombatUtils.aim(diffusion, motion.x, motion.y, motion.z, 1.0F, scatter);
        Vec3 heading = diffusion.getDeltaMovement();
        diffusion.setPos(diffusion.getX() + heading.x, diffusion.getY() + heading.y, diffusion.getZ() + heading.z);
        diffusion.damage = damage;
        diffusion.corrosive = corrosive;
        return diffusion;
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
    }

    @Override
    protected double getDefaultGravity() {
        return 0.0;
    }

    @Override
    public void tick() {
        if (!this.level().isClientSide() && this.tickCount > LIFETIME) {
            this.discard();
            return;
        }
        Vec3 motion = this.getDeltaMovement().scale(0.95);
        if (this.onGround()) {
            motion = motion.scale(0.66);
        }
        this.setDeltaMovement(motion);
        super.tick();
    }

    @Override
    protected void onHit(HitResult hit) {
        if (this.level() instanceof ServerLevel level && this.getOwner() instanceof LivingEntity owner) {
            if (hit.getType() == HitResult.Type.ENTITY) {
                DamageSource source = level.damageSources().indirectMagic(this, owner);
                for (Entity entity : level.getEntities(owner, this.getBoundingBox().inflate(1.0))) {
                    if (entity instanceof LivingEntity living) {
                        living.hurt(source, this.damage);
                        if (this.corrosive) {
                            living.addEffect(new MobEffectInstance(MobEffects.WITHER, 40, 1));
                        }
                        living.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 40, 1));
                    }
                }
            }
            level.broadcastEntityEvent(this, (byte) 16);
        }
        if (!this.level().isClientSide()) {
            this.discard();
        }
    }

    @Override
    public void handleEntityEvent(byte id) {
        if (id == 16) {
            TmFx.get().diffusionBurst(this.level(), this.getX(), this.getY(), this.getZ());
        } else {
            super.handleEntityEvent(id);
        }
    }

    @Override
    public boolean isPickable() {
        return false;
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        return false;
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag output) {
        super.addAdditionalSaveData(output);
        output.putFloat("dmg", this.damage);
        output.putBoolean("corrosive", this.corrosive);
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag input) {
        super.readAdditionalSaveData(input);
        this.damage = input.getFloat("dmg");
        this.corrosive = input.getBoolean("corrosive");
    }
}
