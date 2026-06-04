package com.renyigesai.sip_moment.common.potion;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;

public class DrunkMobEffect extends MobEffect {
    public DrunkMobEffect() {
        super(MobEffectCategory.HARMFUL, 0);
    }

//    @Override
//    public boolean applyEffectTick(ServerLevel serverLevel, LivingEntity mob, int amplification) {
//        return amplification > -1;
//    }
}
