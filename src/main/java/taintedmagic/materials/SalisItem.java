package taintedmagic.materials;

import net.minecraft.core.Holder;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.clock.WorldClock;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.saveddata.WeatherData;
import thaumcraft.registry.ModSounds;
import taintedmagic.fx.TmFx;

public class SalisItem extends Item {
    public enum Kind {
        TEMPESTAS(7),
        AEVUM(6);

        private final int sparkle;

        Kind(int sparkle) {
            this.sparkle = sparkle;
        }
    }

    public static final int LIFETIME = 100;

    private final Kind kind;

    public SalisItem(Kind kind, Properties properties) {
        super(properties);
        this.kind = kind;
    }

    public Kind kind() {
        return kind;
    }

    @Override
    public boolean onEntityItemUpdate(ItemStack stack, ItemEntity entity) {
        Level level = entity.level();
        if (level.dimension() != Level.OVERWORLD) {
            return false;
        }
        if (entity.tickCount == LIFETIME) {
            if (level instanceof ServerLevel server) {
                apply(server);
                server.playSound(null, entity.getX(), entity.getY(), entity.getZ(), ModSounds.ICE.get(), SoundSource.NEUTRAL, 0.3F, 1.0F + server.getRandom().nextFloat() * 0.25F);
                entity.discard();
            } else {
                for (int index = 0; index < 200; index++) {
                    TmFx.get().salisSparkle(entity, kind.sparkle, true);
                }
            }
            return false;
        }
        if (entity.tickCount % 30 == 1 && level instanceof ServerLevel server) {
            server.playSound(null, entity.getX(), entity.getY(), entity.getZ(), ModSounds.WIND.get(), SoundSource.NEUTRAL, 0.1F, 1.0F + server.getRandom().nextFloat() * 0.5F);
        }
        if (level.isClientSide()) {
            TmFx.get().salisSparkle(entity, kind.sparkle, false);
        }
        return false;
    }

    public void apply(ServerLevel level) {
        switch (kind) {
            case TEMPESTAS -> toggleWeather(level);
            case AEVUM -> skipToDayOrNight(level);
        }
    }

    public static void toggleWeather(ServerLevel level) {
        WeatherData weather = level.getWeatherData();
        boolean raining = weather.isRaining();
        weather.setRainTime(raining ? 24000 : 0);
        weather.setRaining(!raining);
        weather.setThundering(!raining && level.getRandom().nextInt(10) == 0);
        weather.setDirty();
    }

    public static void skipToDayOrNight(ServerLevel level) {
        Holder<WorldClock> clock = level.dimensionTypeRegistration().value().defaultClock().orElse(null);
        if (clock == null) {
            return;
        }
        var clocks = level.getServer().clockManager();
        long time = clocks.getInstance(clock).totalTicks();
        long next = time - time % 24000 + (level.isBrightOutside() ? 14000 : 24000);
        if (next <= time) {
            next += 24000;
        }
        clocks.setTotalTicks(clock, next);
    }
}
