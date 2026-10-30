package com.renyigesai.sip_moment.common.event;

import com.renyigesai.sip_moment.SipMomentMod;
import com.renyigesai.sip_moment.common.data.WineListCatalog;
import com.renyigesai.sip_moment.common.entitys.AfterimageEntity;
import com.renyigesai.sip_moment.common.entitys.ITimeScaledEntity;
import com.renyigesai.sip_moment.common.init.SMEntityTypes;
import com.renyigesai.sip_moment.common.network.SyncWineListPayload;
import com.renyigesai.sip_moment.common.utils.WorldUtils;
import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.tick.EntityTickEvent;
import net.neoforged.neoforge.network.PacketDistributor;

import java.util.Set;

@EventBusSubscriber
public class SMEvents {

    @SubscribeEvent
    public static void onPlayerLogin(PlayerEvent.PlayerLoggedInEvent event) {
        if (event.getEntity() instanceof ServerPlayer serverPlayer) {
            if (WineListCatalog.wineListMap != null) {
                PacketDistributor.sendToPlayer(serverPlayer,
                        new SyncWineListPayload(WineListCatalog.wineListMap));
            }
        }
    }

    @SubscribeEvent
    public static void onEntityTickPre(EntityTickEvent.Pre event) {
        Entity entity = event.getEntity();
        if (entity.level().isClientSide()) return;
        if (entity instanceof Player) return;
        if (!(entity instanceof ITimeScaledEntity ts)) return;

        int lease = ts.getTimeStopLease();
        if (lease > 0) {
            ts.setTimeStopLease(lease - 1);
            if (lease - 1 == 0) {
                ts.setTimeScale(1.0f);
            }
        }
    }

    @SubscribeEvent
    public static void onAttributeCreation(EntityAttributeCreationEvent event) {
        event.put(SMEntityTypes.AFTERIMAGE.get(), AfterimageEntity.createAttributes().build());
    }

    @SubscribeEvent
    public static void onLivingDeath(LivingDeathEvent event) {
        LivingEntity entity = event.getEntity();
        if (!(entity instanceof Player player)) {
            return;
        }
        if (event.getSource().is(ResourceKey.create(Registries.DAMAGE_TYPE, Identifier.fromNamespaceAndPath(SipMomentMod.MODID, "at_field")))){
            if (player.level() instanceof ServerLevel serverLevel) {
                if (WorldUtils.isDoneAdvancement(player,serverLevel,Identifier.fromNamespaceAndPath(SipMomentMod.MODID, "hey_jude"))) {
                    return;
                }
                ServerLevel deadSea = serverLevel.getServer().getLevel(ResourceKey.create(Registries.DIMENSION, Identifier.fromNamespaceAndPath(SipMomentMod.MODID, "dead_sea")));
                if (deadSea == null) {
                    return;
                }
                if (serverLevel.dimension() == deadSea.dimension()) {
                    return;
                }
                event.setCanceled(true);
                player.setHealth(player.getMaxHealth());
                player.teleportTo(deadSea,player.getX(),64,player.getZ(), Set.of(), player.getYRot(), player.getXRot(),false);
                triggerAdvancement(player);
            }
        }
    }

    private static void triggerAdvancement(Player player){
        if (player instanceof ServerPlayer serverPlayer){
            AdvancementHolder advancement = serverPlayer.server.getAdvancements().get(Identifier.fromNamespaceAndPath(SipMomentMod.MODID,"hey_jude"));
            if (advancement != null) {
                serverPlayer.getAdvancements().award(advancement, "hey_jude");
            }
        }
    }

}
