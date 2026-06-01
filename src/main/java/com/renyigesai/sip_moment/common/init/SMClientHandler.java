package com.renyigesai.sip_moment.common.init;

import com.renyigesai.sip_moment.SipMomentMod;
import com.renyigesai.sip_moment.common.client.renderer.blockentity.mix_block.MixBlockRender;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;

@EventBusSubscriber(value = Dist.CLIENT,modid = SipMomentMod.MODID)
public class SMClientHandler {

    @SubscribeEvent
    public static void onRenders(EntityRenderersEvent.RegisterRenderers event){
        event.registerBlockEntityRenderer(SMBlocks.Entitys.MIX_BLOCK_ENTITY.get(), MixBlockRender::new);
    }

}
