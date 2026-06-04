package com.renyigesai.sip_moment.common.items;

import com.renyigesai.sip_moment.SipMomentMod;
import com.renyigesai.sip_moment.common.init.SMDataComponents;
import com.renyigesai.sip_moment.common.init.SMMobEffects;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUseAnimation;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;

import java.util.function.Consumer;

public class WineItem extends PileItem{
    public WineItem(Block block, Properties pProperties, boolean effectTooltip, boolean customField) {
        super(block, pProperties, effectTooltip, customField);
    }

    public WineItem(Block block, PileProperties pileProperties) {
        super(block, pileProperties);
    }

    public WineItem(Block block, Properties pProperties,int eatCount) {
        super(block, pProperties.component(SMDataComponents.EAT_COUNT_MAX,eatCount).component(SMDataComponents.EAT_COUNT,eatCount));
    }

    public boolean isExtra(UseOnContext pContext) {
        ItemStack itemInHand = pContext.getItemInHand();
        if (isRepeatEat(itemInHand)){
            int eatCount = itemInHand.getOrDefault(SMDataComponents.EAT_COUNT,-1);
            int eatCountMax = itemInHand.getOrDefault(SMDataComponents.EAT_COUNT_MAX,-1);
            return eatCount == eatCountMax;
        }
        return false;
    }

    @Override
    public ItemUseAnimation getUseAnimation(ItemStack pStack) {
        return ItemUseAnimation.DRINK;
    }

    public static boolean isRepeatEat(ItemStack stack){
        return stack.has(SMDataComponents.EAT_COUNT_MAX) && stack.has(SMDataComponents.EAT_COUNT);
    }

    @Override
    public int getBarColor(ItemStack stack) {
        return 5592575;
    }

    public ItemStack onConsume(Level level, LivingEntity living, ItemStack stack){
        if (isRepeatEat(stack)){
            int eatCount = stack.getOrDefault(SMDataComponents.EAT_COUNT,-1);
            ItemStack cache = ItemStack.EMPTY;
            eat(level, living, stack);
            if (eatCount - 1 == 0){
                cache = stack.copy();
                stack.consume(1,living);
            }else {
                stack.set(SMDataComponents.EAT_COUNT,eatCount - 1);
            }
            return (eatCount - 1 == 0 && cache.getCraftingRemainder() != null) ? cache.getCraftingRemainder().create() : stack;
        }
        return stack;
    }

    public void eat(Level level,LivingEntity living,ItemStack stack){
        if (!level.isClientSide()) {
            try {
                if (living.hasEffect(SMMobEffects.DRUNK) ) {
                    if (living.getEffect(SMMobEffects.DRUNK).getAmplifier() < 4){
                        int amplifier = living.getEffect(SMMobEffects.DRUNK).getAmplifier();
                        living.addEffect(new MobEffectInstance(SMMobEffects.DRUNK, living.getEffect(SMMobEffects.DRUNK).getDuration() + 200, amplifier + 1), living);
                    }else {
                        living.removeEffect(SMMobEffects.DRUNK);
                        living.addEffect(new MobEffectInstance(SMMobEffects.DRUNK, 600,4));
                    }
                }else {
                    living.addEffect(new MobEffectInstance(SMMobEffects.DRUNK, 600,0));
                }
            }catch (NullPointerException e){
                SipMomentMod.LOGGER.error("WineItem eat {}", e);
            }
        }
    }

    public int getBarWidth(ItemStack stack) {
        if (isRepeatEat(stack)) {
            Integer eatCount = stack.get(SMDataComponents.EAT_COUNT);
            Integer eatCountMax = stack.get(SMDataComponents.EAT_COUNT_MAX);
            if (eatCount != null && eatCountMax != null && eatCountMax > 0) {
                return Mth.clamp(Math.round(13.0F * eatCount / eatCountMax), 0, 13);
            }
        }
        return super.getBarWidth(stack);
    }

    @Override
    public boolean isBarVisible(ItemStack stack) {
        if (!isRepeatEat(stack)){
            return false;
        }
        Integer eatCount = stack.get(SMDataComponents.EAT_COUNT);
        Integer eatCountMax = stack.get(SMDataComponents.EAT_COUNT_MAX);
        if (eatCount != null &&  eatCountMax != null){
            return eatCount < eatCountMax;
        }
        return false;
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, TooltipDisplay display, Consumer<Component> builder, TooltipFlag tooltipFlag) {
        super.appendHoverText(stack, context, display, builder, tooltipFlag);
        builder.accept(Component.translatable("effect.sip_moment.drunk").append(" (00:30)").withStyle(ChatFormatting.RED));
        if (isRepeatEat(stack)) {
            int eatCount = stack.getOrDefault(SMDataComponents.EAT_COUNT, -1);
            int eatCountMax = stack.getOrDefault(SMDataComponents.EAT_COUNT_MAX, -1);
            String translatable = "tooltips.sip_moment.repeat_eat_item_drink";
            builder.accept(Component.literal(Component.translatable(translatable).getString() + eatCount + " / " + eatCountMax));
        }
    }
}
