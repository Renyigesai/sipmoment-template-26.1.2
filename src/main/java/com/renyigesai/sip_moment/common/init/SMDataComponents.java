package com.renyigesai.sip_moment.common.init;

import com.mojang.serialization.Codec;
import com.renyigesai.sip_moment.SipMomentMod;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.codec.ByteBufCodecs;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;


public class SMDataComponents {
    public static final DeferredRegister<DataComponentType<?>> DATA_COMPONENT_TYPE = DeferredRegister.create(Registries.DATA_COMPONENT_TYPE, SipMomentMod.MODID);
    public static final Supplier<DataComponentType<Integer>> EAT_COUNT;
    public static final Supplier<DataComponentType<Integer>> EAT_COUNT_MAX;

    static {
        EAT_COUNT = DATA_COMPONENT_TYPE.register("eat_count", () -> DataComponentType.<Integer>builder().persistent(Codec.INT).networkSynchronized(ByteBufCodecs.INT).build());
        EAT_COUNT_MAX = DATA_COMPONENT_TYPE.register("eat_count_max", () -> DataComponentType.<Integer>builder().persistent(Codec.INT).networkSynchronized(ByteBufCodecs.INT).build());
    }
}
