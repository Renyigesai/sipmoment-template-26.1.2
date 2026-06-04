package com.renyigesai.sip_moment.common.init;

import com.renyigesai.sip_moment.SipMomentMod;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.sounds.SoundEvent;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;


public class SMSounds {
    public static final DeferredRegister<SoundEvent> REGISTRY = DeferredRegister.create(Registries.SOUND_EVENT, SipMomentMod.MODID);

    private static Supplier<SoundEvent> registerSoundEvents(String name) {
        return REGISTRY.register(name,()-> SoundEvent.createFixedRangeEvent(Identifier.fromNamespaceAndPath(SipMomentMod.MODID,name),16F));
    }

    public static final Supplier<SoundEvent> POURING_WINE = registerSoundEvents("block.generic.pouring_wine");

}
