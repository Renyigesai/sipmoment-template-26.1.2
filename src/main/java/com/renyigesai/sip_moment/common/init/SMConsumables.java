package com.renyigesai.sip_moment.common.init;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.ItemUseAnimation;
import net.minecraft.world.item.component.Consumable;
import net.minecraft.world.item.component.Consumables;
import net.minecraft.world.item.consume_effects.ApplyStatusEffectsConsumeEffect;

public class SMConsumables {
    public static final Consumable HIGHBALL;

    static {

        HIGHBALL = Consumables.defaultFood()
                .animation(ItemUseAnimation.DRINK).sound(SoundEvents.GENERIC_DRINK)
                /*.onConsume(new ApplyStatusEffectsConsumeEffect(new MobEffectInstance(SMMobEffects.DRUNK,600)))*/
                .hasConsumeParticles(false)
                .build();
    }

    private static Consumable.Builder defaultEffectFood(MobEffectInstance effect) {
        return Consumables.defaultFood()
                .onConsume(new ApplyStatusEffectsConsumeEffect(effect));
    }

    public static Consumable.Builder defaultShake() {
        return Consumable.builder().consumeSeconds(1.6F).animation(ItemUseAnimation.DRINK).sound(SoundEvents.GENERIC_DRINK).hasConsumeParticles(false);
    }
}