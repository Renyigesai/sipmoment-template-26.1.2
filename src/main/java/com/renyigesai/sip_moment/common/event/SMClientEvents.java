package com.renyigesai.sip_moment.common.event;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.LevelLoadingScreen;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterDimensionTransitionScreenEvent;

@EventBusSubscriber(value = Dist.CLIENT)
public class SMClientEvents {
//    @SubscribeEvent
//    public static void onRegisterDimensionTransitionScreen(RegisterDimensionTransitionScreenEvent event) {
//        event.registerConditionalEffect(null, null, (tracker, reason) -> new LevelLoadingScreen(tracker, reason) {
//            @Override
//            public void extractBackground(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a) {
//
//            }
//        });
//    }
}
