package com.renyigesai.sip_moment.common.items;

import com.renyigesai.sip_moment.SipMomentMod;
import com.renyigesai.sip_moment.common.utils.WorldUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.font.providers.UnihexProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;

import java.util.Set;
import java.util.function.Consumer;

public class OrangeJuiceItem extends DrinkItem {

    public OrangeJuiceItem(Block block, Properties pProperties, int eatCount, boolean customField, boolean canWine) {

        super(block, pProperties, eatCount, customField, canWine);
    }

    @Override
    public ItemStack finishUsingItem(ItemStack itemStack, Level level, LivingEntity entity) {
        if (entity instanceof Player player && level instanceof ServerLevel serverLevel) {
            if (player.isShiftKeyDown() && WorldUtils.isDoneAdvancement(player,serverLevel, Identifier.fromNamespaceAndPath(SipMomentMod.MODID, "hey_jude"))){
                ServerLevel deadSea = serverLevel.getServer().getLevel(ResourceKey.create(Registries.DIMENSION, Identifier.fromNamespaceAndPath(SipMomentMod.MODID, "dead_sea")));
                if (deadSea == null) {
                    return super.finishUsingItem(itemStack, level, entity);
                }
                ResourceKey<Level> levelKey;
                if (serverLevel.dimension() == deadSea.dimension()) {
                    levelKey = Level.OVERWORLD;
                }else {
                    levelKey = deadSea.dimension();
                }
                ServerLevel goLevel = serverLevel.getServer().getLevel(levelKey);

                if (goLevel != null) {
                    EntityDimensions dims = player.getDimensions(player.getPose());
                    BlockPos safe = WorldUtils.findSafePosition(goLevel, player.getX(), player.getY(), player.getZ(), dims);
                    player.teleportTo(goLevel,safe.getX() + 0.5,safe.getY(),safe.getZ() + 0.5, Set.of(), player.getYRot(), player.getXRot(),false);
                }
            }
        }
        return super.finishUsingItem(itemStack, level, entity);
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, TooltipDisplay display, Consumer<Component> builder, TooltipFlag tooltipFlag) {
        super.appendHoverText(stack, context, display, builder, tooltipFlag);
        builder.accept(Component.empty());
        builder.accept(Component.translatable("potion.whenDrank").withStyle(ChatFormatting.DARK_PURPLE));
        builder.accept(Component.translatable("tooltip.sip_moment:orange_juice_2").withStyle(ChatFormatting.BLUE));
    }
}
