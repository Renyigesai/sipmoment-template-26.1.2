package com.renyigesai.sip_moment.common.event;

import com.renyigesai.sip_moment.common.items.WineItem;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;

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
        if (!(handItem.getItem() instanceof WineItem pileItem)){
            return;
        }
        event.setCanceled(true);
        event.setCancellationResult(InteractionResult.SUCCESS);
        UseOnContext context = new UseOnContext(level, player, hand, handItem, event.getHitVec());
        InteractionResult result = pileItem.pileUseOn(context);
        if (result == InteractionResult.PASS) {

        }
    }
}
