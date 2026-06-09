package com.renyigesai.sip_moment.common.client.particles;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.SingleQuadParticle;
import net.minecraft.client.particle.SpriteSet;
import org.joml.Vector3f;

public class DrunkParticle extends SingleQuadParticle {


    private final SpriteSet sprites;
    public DrunkParticle(ClientLevel level, double x, double y, double z, double dx, double dy, double dz, DrunkParticleOptions options, SpriteSet sprites) {
        super(level, x, y, z, dx, dy, dz, sprites.first());
        this.sprites = sprites;
        this.gravity = 0.15F;
        this.setSize(0.01f,0.01f);
        this.friction = 0F;
        this.lifetime = 60;
        this.quadSize = options.getScale();
        this.setSpriteFromAge(sprites);
    }

    @Override
    protected Layer getLayer() {
        return Layer.TRANSLUCENT;
    }

}