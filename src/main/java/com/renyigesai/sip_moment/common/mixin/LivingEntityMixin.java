package com.renyigesai.sip_moment.common.mixin;

import com.renyigesai.sip_moment.common.init.SMMobEffects;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin {

    @ModifyVariable(
            method = "hurtServer",
            at = @At("HEAD"),
            argsOnly = true,
            name = "source")
    private DamageSource modifyDamageSource(DamageSource source) {
        if (source.getEntity() instanceof LivingEntity living && living.hasEffect(SMMobEffects.SINCERELY_FOR_YOU)){
            return new DamageSource(living.self().damageSources().genericKill().typeHolder(), source.getEntity(), source.getDirectEntity(), source.getSourcePosition());
        }
        return source;
    }
}
