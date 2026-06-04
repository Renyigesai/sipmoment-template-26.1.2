package com.renyigesai.sip_moment;

import com.renyigesai.sip_moment.common.init.*;
import org.slf4j.Logger;

import com.mojang.logging.LogUtils;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.event.server.ServerStartingEvent;

// The value here should match an entry in the META-INF/neoforge.mods.toml file
@Mod(SipMomentMod.MODID)
public class SipMomentMod {
    public static final String MODID = "sip_moment";
    public static final Logger LOGGER = LogUtils.getLogger();
    public SipMomentMod(IEventBus modEventBus) {
        SMBlocks.BLOCKS.register(modEventBus);
        SMBlocks.Entitys.REGISTER.register(modEventBus);
        SMDataComponents.DATA_COMPONENT_TYPE.register(modEventBus);
        SMItems.ITEMS.register(modEventBus);
        SMCreativeModeTabs.REGISTER.register(modEventBus);
        SMEntityTypes.ENTITY.register(modEventBus);
        SMMobEffects.EFFECTS.register(modEventBus);
        SMParticleTypes.PARTICLE_TYPE.register(modEventBus);
        SMSounds.REGISTRY.register(modEventBus);
//        modEventBus.addListener(this::commonSetup);
    }

    private void commonSetup(FMLCommonSetupEvent event) {
        // Some common setup code
        LOGGER.info("HELLO FROM COMMON SETUP");

        if (Config.LOG_DIRT_BLOCK.getAsBoolean()) {
            LOGGER.info("DIRT BLOCK >> {}", BuiltInRegistries.BLOCK.getKey(Blocks.DIRT));
        }

        LOGGER.info("{}{}", Config.MAGIC_NUMBER_INTRODUCTION.get(), Config.MAGIC_NUMBER.getAsInt());

        Config.ITEM_STRINGS.get().forEach((item) -> LOGGER.info("ITEM >> {}", item));
    }

    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
        // Do something when the server starts
        LOGGER.info("HELLO from server starting");
    }
}
