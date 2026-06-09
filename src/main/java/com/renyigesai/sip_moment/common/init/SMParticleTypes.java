package com.renyigesai.sip_moment.common.init;

import com.mojang.serialization.MapCodec;
import com.renyigesai.sip_moment.SipMomentMod;
import com.renyigesai.sip_moment.common.client.particles.DrunkParticleOptions;
import com.renyigesai.sip_moment.common.client.particles.WineLiquidParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class SMParticleTypes {
    public static final DeferredRegister<ParticleType<?>> PARTICLE_TYPE = DeferredRegister.create(Registries.PARTICLE_TYPE, SipMomentMod.MODID);

    public static final Supplier<ParticleType<WineLiquidParticleOptions>> WINE_LIQUID =
            PARTICLE_TYPE.register("wine_liquid",
                    ()-> new ParticleType<>(false) {

                        @Override
                        public MapCodec<WineLiquidParticleOptions> codec() {
                            return WineLiquidParticleOptions.CODEC;
                        }

                        @Override
                        public StreamCodec<? super RegistryFriendlyByteBuf, WineLiquidParticleOptions> streamCodec() {
                            return WineLiquidParticleOptions.STREAM_CODEC;
                        }
                    }
            );

    public static final Supplier<ParticleType<DrunkParticleOptions>> DRUNK =
            PARTICLE_TYPE.register("drunk",
                    ()-> new ParticleType<>(false) {

                        @Override
                        public MapCodec<DrunkParticleOptions> codec() {
                            return DrunkParticleOptions.CODEC;
                        }

                        @Override
                        public StreamCodec<? super RegistryFriendlyByteBuf, DrunkParticleOptions> streamCodec() {
                            return DrunkParticleOptions.STREAM_CODEC;
                        }
                    }
            );
}
