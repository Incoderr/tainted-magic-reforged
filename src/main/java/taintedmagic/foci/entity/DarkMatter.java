package taintedmagic.foci.entity;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.HitResult;
import thaumcraft.entity.CombatUtils;
import thaumcraft.entity.projectile.EldritchOrb;
import taintedmagic.foci.FociFeature;

public class DarkMatter extends EldritchOrb {
    private float damage;
    private int enlarge;
    private boolean corrosive;

    public DarkMatter(EntityType<? extends DarkMatter> type, Level level) {
        super(type, level);
    }

    public static DarkMatter create(ServerLevel level, LivingEntity thrower, float damage, int enlarge, boolean corrosive) {
        DarkMatter orb = new DarkMatter(FociFeature.DARK_MATTER.get(), level);
        CombatUtils.launch(orb, thrower, 1.5F);
        orb.damage = damage;
        orb.enlarge = enlarge;
        orb.corrosive = corrosive;
        return orb;
    }

    @Override
    protected void onHit(HitResult hit) {
        if (this.level() instanceof ServerLevel level && this.getOwner() instanceof LivingEntity owner) {
            double expand = 1.5 + this.enlarge * 0.5;
            DamageSource source = level.damageSources().indirectMagic(this, owner);
            for (Entity entity : level.getEntities(owner, this.getBoundingBox().inflate(expand))) {
                if (entity instanceof LivingEntity living) {
                    living.hurtServer(level, source, this.damage);
                    if (this.corrosive) {
                        living.addEffect(new MobEffectInstance(MobEffects.WITHER, 160, 1));
                    }
                    living.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 160, 1));
                }
            }
            level.playSound(null, this.blockPosition(), SoundEvents.FIRE_EXTINGUISH, SoundSource.PLAYERS, 0.5F, 2.6F + (this.random.nextFloat() - this.random.nextFloat()) * 0.8F);
            level.broadcastEntityEvent(this, (byte) 16);
        }
        if (!this.level().isClientSide()) {
            this.discard();
        }
    }

    @Override
    protected void addAdditionalSaveData(ValueOutput output) {
        super.addAdditionalSaveData(output);
        output.putFloat("dmg", this.damage);
        output.putInt("enlarge", this.enlarge);
        output.putBoolean("corrosive", this.corrosive);
    }

    @Override
    protected void readAdditionalSaveData(ValueInput input) {
        super.readAdditionalSaveData(input);
        this.damage = input.getFloatOr("dmg", 0.0F);
        this.enlarge = input.getIntOr("enlarge", 0);
        this.corrosive = input.getBooleanOr("corrosive", false);
    }

    public float damage() {
        return damage;
    }
}
