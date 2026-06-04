package com.renyigesai.sip_moment.common.client.particles;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.renyigesai.sip_moment.common.init.SMParticleTypes;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.ScalableParticleOptionsBase;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.ARGB;
import net.minecraft.util.ExtraCodecs;
import org.joml.Vector3f;

public class WineLiquidParticleOptions extends ScalableParticleOptionsBase {

    public static final MapCodec<WineLiquidParticleOptions> CODEC = RecordCodecBuilder.mapCodec(
            i -> i.group(
                            ExtraCodecs.RGB_COLOR_CODEC.fieldOf("color").forGetter(o -> o.color), SCALE.fieldOf("scale").forGetter(ScalableParticleOptionsBase::getScale)
                    )
                    .apply(i, WineLiquidParticleOptions::new)
    );
    public static final StreamCodec<RegistryFriendlyByteBuf, WineLiquidParticleOptions> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.INT, o -> o.color, ByteBufCodecs.FLOAT, ScalableParticleOptionsBase::getScale, WineLiquidParticleOptions::new
    );
    private final int color;

    public WineLiquidParticleOptions(int color,float scale) {
        super(scale);
        this.color = color;
    }

    public Vector3f getColor() {
        return ARGB.vector3fFromRGB24(this.color);
    }

    @Override
    public ParticleType<?> getType() {
        return SMParticleTypes.WINE_LIQUID.get();
    }
}
