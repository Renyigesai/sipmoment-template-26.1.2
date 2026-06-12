package com.renyigesai.sip_moment.common.init;


import com.renyigesai.sip_moment.SipMomentMod;
import com.renyigesai.sip_moment.common.blocks.sofa.BarChairEntity;
import com.renyigesai.sip_moment.common.entitys.PouringWineEntity;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

@SuppressWarnings("removal")
@EventBusSubscriber(value = Dist.CLIENT)
public class SMEntityTypes {
    public static final DeferredRegister<EntityType<?>> ENTITY =
            DeferredRegister.create(Registries.ENTITY_TYPE, SipMomentMod.MODID);

    public static final Supplier<EntityType<PouringWineEntity>> POURING_WINE = ENTITY.register("pouring_wine", () ->
            EntityType.Builder.of(PouringWineEntity::new, MobCategory.MISC).sized(0.05f, 0.05f).build(ResourceKey.create(Registries.ENTITY_TYPE,Identifier.fromNamespaceAndPath(SipMomentMod.MODID,"pouring_wine"))));

    public static final Supplier<EntityType<BarChairEntity>> BAR_CHAIR = ENTITY.register("bar_chair", () ->
            EntityType.Builder.of(BarChairEntity::new, MobCategory.MISC).sized(0.05f, 0.05f).build(ResourceKey.create(Registries.ENTITY_TYPE,Identifier.fromNamespaceAndPath(SipMomentMod.MODID,"bar_chair"))));

    @SubscribeEvent
    public static void init(FMLCommonSetupEvent event) {
        event.enqueueWork(() -> EntityRenderers.register(POURING_WINE.get(), PouringWineEntity.PouringWineRender::new));
        event.enqueueWork(() -> EntityRenderers.register(BAR_CHAIR.get(), BarChairEntity.BarChairEntityRender::new));
    }

}
