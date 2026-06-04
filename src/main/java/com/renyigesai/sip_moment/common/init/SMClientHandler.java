package com.renyigesai.sip_moment.common.init;

import com.renyigesai.sip_moment.SipMomentMod;
import com.renyigesai.sip_moment.common.client.particles.WineLiquidParticle;
import com.renyigesai.sip_moment.common.client.particles.WineLiquidParticleOptions;
import com.renyigesai.sip_moment.common.client.renderer.blockentity.mix_block.MixBlockRender;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.player.Player;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.RegisterParticleProvidersEvent;
import net.neoforged.neoforge.client.event.ViewportEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import org.jspecify.annotations.Nullable;

@EventBusSubscriber(value = Dist.CLIENT,modid = SipMomentMod.MODID)
public class SMClientHandler {
    private static float rollAngle = 0f;

    @SubscribeEvent
    public static void onRenders(EntityRenderersEvent.RegisterRenderers event){
        event.registerBlockEntityRenderer(SMBlocks.Entitys.MIX_BLOCK_ENTITY.get(), MixBlockRender::new);
    }

    @SubscribeEvent
    public static void registerParticleProviders(RegisterParticleProvidersEvent event) {
        event.registerSpriteSet(SMParticleTypes.WINE_LIQUID.get(), (spriteSet)-> new ParticleProvider<>() {
            @Override
            public @Nullable Particle createParticle(WineLiquidParticleOptions options, ClientLevel level, double x, double y, double z, double xAux, double yAux, double zAux,RandomSource source) {
                try {
                    return new WineLiquidParticle(level,x,y,z,xAux,yAux,zAux,options,spriteSet);
                }catch (Exception e){
                    e.printStackTrace();
                    return null;
                }

            }
        });
    }

    @SubscribeEvent
    public static void onCameraAngles(ViewportEvent.ComputeCameraAngles event) {
        Minecraft instance = Minecraft.getInstance();
        LocalPlayer player = instance.player;
        if (player != null && player.hasEffect(SMMobEffects.DRUNK) && player.getEffect(SMMobEffects.DRUNK).getAmplifier() >= 4){
            float time = (player.tickCount + instance.getDeltaTracker().getGameTimeDeltaPartialTick(false)) / 20f;
            float cycleTime = time % 10.0f;
            float rollAngle = (cycleTime / 5.0f) * -360f;
            event.setRoll(rollAngle);
        }
    }

}
