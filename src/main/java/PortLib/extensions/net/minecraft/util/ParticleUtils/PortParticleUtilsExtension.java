package PortLib.extensions.net.minecraft.util.ParticleUtils;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelAccessor;

public class PortParticleUtilsExtension {
    public static void spawnParticles(LevelAccessor level, BlockPos pos, int count, double xzSpread, double ySpread, boolean allowInAir, ParticleOptions particle) {
        RandomSource random = level.getRandom();
        for (int i = 0; i < count; i++) {
            double xd = random.nextGaussian() * 0.02;
            double yd = random.nextGaussian() * 0.02;
            double zd = random.nextGaussian() * 0.02;
            double xz = 0.5 - xzSpread;
            double x = (double) pos.getX() + xz + random.nextDouble() * xzSpread * 2.0;
            double y = (double) pos.getY() + random.nextDouble() * ySpread;
            double z = (double) pos.getZ() + xz + random.nextDouble() * xzSpread * 2.0;
            if (allowInAir || !level.getBlockState(BlockPos.containing(x, y, z).below()).isAir()) {
                level.addParticle(particle, x, y, z, xd, yd, zd);
            }
        }
    }
}
