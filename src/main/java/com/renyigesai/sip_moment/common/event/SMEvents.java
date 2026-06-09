package com.renyigesai.sip_moment.common.event;

import com.renyigesai.sip_moment.common.data.WineListCatalog;
import com.renyigesai.sip_moment.common.items.PileItem;
import com.renyigesai.sip_moment.common.network.SyncWineListPayload;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import net.neoforged.neoforge.network.PacketDistributor;

@EventBusSubscriber
public class SMEvents {
    @SubscribeEvent
    public static void onPileItemUseOn(PlayerInteractEvent.RightClickBlock event) {
        Player player = event.getEntity();
        Level level = event.getLevel();
        ItemStack handItem = event.getItemStack();
        InteractionHand hand = event.getHand();
        if (level.isClientSide()){
            return;
        }
        if (!player.isShiftKeyDown()){
            return;
        }
        if (!(handItem.getItem() instanceof PileItem pileItem)){
            return;
        }
        event.setCanceled(true);
        event.setCancellationResult(InteractionResult.SUCCESS);
        UseOnContext context = new UseOnContext(level, player, hand, handItem, event.getHitVec());
        InteractionResult result = pileItem.pileUseOn(context);
        if (result == InteractionResult.PASS) {

        }
    }

    @SubscribeEvent
    public static void onPlayerLogin(PlayerEvent.PlayerLoggedInEvent event) {
        if (event.getEntity() instanceof ServerPlayer serverPlayer) {
            if (WineListCatalog.wineListMap != null) {
                PacketDistributor.sendToPlayer(serverPlayer,
                        new SyncWineListPayload(WineListCatalog.wineListMap));
            }
        }
    }
}
