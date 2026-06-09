package com.renyigesai.sip_moment.common.potion;

import com.renyigesai.sip_moment.common.client.particles.DrunkParticleOptions;
import com.renyigesai.sip_moment.common.client.particles.WineLiquidParticleOptions;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;

import java.util.Random;

public class DrunkMobEffect extends MobEffect {
    public DrunkMobEffect() {
        super(MobEffectCategory.HARMFUL, 0);
    }

//    @Override
//    public boolean shouldApplyEffectTickThisTick(int duration, int amplifier) {
//        return amplifier >= 0;
//    }
//
//    @Override
//    public boolean applyEffectTick(ServerLevel serverLevel, LivingEntity mob, int amplification) {
//        float eyeHeight = mob.getEyeHeight();
//        DrunkParticleOptions particleOptions = new DrunkParticleOptions(0.25f);
//        double r = new Random().nextDouble();
//        if (r < 0.1){
//            serverLevel.sendParticles(particleOptions,mob.getX() + 0.5,mob.getY() + eyeHeight,mob.getZ() + 0.5,1,0,0,0,0);
//        }
//        return true;
//    }

}
