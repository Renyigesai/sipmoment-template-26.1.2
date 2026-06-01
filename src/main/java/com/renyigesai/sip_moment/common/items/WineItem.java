package com.renyigesai.sip_moment.common.items;

import com.renyigesai.sip_moment.common.blocks.AbstractWineBlock;
import com.renyigesai.sip_moment.common.blocks.mix_block.MixBlock;
import com.renyigesai.sip_moment.common.blocks.mix_block.MixBlockEntity;
import com.renyigesai.sip_moment.common.init.SMBlocks;
import com.renyigesai.sip_moment.common.utils.TextUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

public class WineItem extends BlockItem {
    public final boolean customField;
    private final SoundEvent placeSound;
    public WineItem(Block block, Properties pProperties, boolean effectTooltip, boolean customField) {
        super(block, pProperties);
        this.placeSound = SoundEvents.GLASS_PLACE;
        this.customField = customField;
    }

    public WineItem(Block block, PileProperties pileProperties) {
        super(block, pileProperties.itemProperties);
        this.placeSound = pileProperties.placeSound;
        this.customField = pileProperties.customField;
    }

    public WineItem(Block block, Item.Properties pProperties) {
        super(block, pProperties);
        this.placeSound = SoundEvents.GLASS_PLACE;
        this.customField = false;
    }

//    @Override
//    public InteractionResult use(Level level, Player player, InteractionHand usedHand) {
//        ItemStack itemInHand = player.getItemInHand(usedHand);
//        if (isPerfect(itemInHand)){
//            player.startUsingItem(usedHand);
//            return InteractionResult.SUCCESS;
//        }
//        return super.use(level, player, usedHand);
//    }


    @Override
    public @NotNull InteractionResult useOn(UseOnContext pContext) {
        return InteractionResult.FAIL;
    }

    public InteractionResult pileUseOn(UseOnContext pContext) {
        Player player = pContext.getPlayer();
        if (player == null) return InteractionResult.PASS;
        Level level = pContext.getLevel();
        Block thisBlock = this.getBlock();
        BlockPos pos = pContext.getClickedPos();
        BlockState state = level.getBlockState(pos);
        if (player.isShiftKeyDown() && this.isExtra(pContext)) {
            if (state.is(SMBlocks.MIX_BLOCK.get())) {
                addMixBlock(level, pos, thisBlock, player, pContext);
                return InteractionResult.SUCCESS;
            }
            if (!state.is(thisBlock) &&
                    state.getBlock() instanceof AbstractWineBlock pileBlock &&
                    state.getValue(pileBlock.getPileProperty()) < pileBlock.getMaxPile() &&
                    thisBlock instanceof AbstractWineBlock) {
                placeMixBlock(state, thisBlock, level, pos, player, pContext);
                return InteractionResult.SUCCESS;
            }
            if (state.is(thisBlock)) {
                addPileBlock(state, thisBlock, level, pos, player, pContext);
                return InteractionResult.SUCCESS;
            }
            BlockPlaceContext placeContext = new BlockPlaceContext(pContext);
            InteractionResult placeResult = this.place(placeContext);
            if (placeResult.consumesAction()) {
                player.awardStat(Stats.ITEM_USED.get(this));
                level.playSound(null, pos, getPlaceSound(), SoundSource.PLAYERS, 0.8F, 0.8F);
                return InteractionResult.SUCCESS;
            }
            return InteractionResult.PASS;
        }
        return InteractionResult.PASS;
    }

    public boolean isExtra(UseOnContext pContext) {
        return true;
    }

    @Override
    public int getEnchantmentLevel(ItemInstance stack, Holder<Enchantment> enchantment) {
        return 0;
    }

    public InteractionResult addMixBlock(Level level,BlockPos pos,Block thisBlock,Player player,UseOnContext pContext){
        BlockEntity blockEntity = level.getBlockEntity(pos);
        if (blockEntity instanceof MixBlockEntity mix){
            boolean flag = mix.addItem(new ItemStack(thisBlock.asItem()));
            if (flag){
                if (!player.getAbilities().instabuild) {
                    pContext.getItemInHand().shrink(1);
                }
                if (thisBlock instanceof AbstractWineBlock){
                    level.playSound(null, pos, ((AbstractWineBlock)thisBlock).getPlaceSound(), SoundSource.PLAYERS, 0.8F, 0.8F);
                }
            }
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.FAIL;
    }

    private void fillMixBlock(BlockState state, Block block, Level level, BlockPos pos) {
        AbstractWineBlock pileBlock = (AbstractWineBlock) state.getBlock();
        int integerProperty = state.getValue(pileBlock.getPileProperty());
        level.setBlock(pos, SMBlocks.MIX_BLOCK.get().defaultBlockState().setValue(MixBlock.FACING, state.getValue(AbstractWineBlock.FACING)), 3);
        MixBlockEntity mix = (MixBlockEntity) level.getBlockEntity(pos);
        if (mix == null){
            return;
        }
        Item item = state.getBlock().asItem();
        for (int i = 0; i < integerProperty; i++) {
            if (!mix.addItem(new ItemStack(item))) {

            }
        }
        if (!mix.addItem(new ItemStack(block.asItem()))) {

        }
        mix.updateBlock();
    }

    public InteractionResult placeMixBlock(BlockState state,Block thisBlock,Level level,BlockPos pos,Player player,UseOnContext pContext){
        fillMixBlock(state, thisBlock, level, pos);
        if (!player.getAbilities().instabuild) {
            pContext.getItemInHand().shrink(1);
        }
        level.playSound(null, pos, getPlaceSound(), SoundSource.PLAYERS, 0.8F, 0.8F);
        return InteractionResult.SUCCESS;
    }

    public InteractionResult addPileBlock(BlockState state, Block thisBlock, Level level, BlockPos pos, Player player, UseOnContext pContext) {
        if (state.hasProperty(((AbstractWineBlock)state.getBlock()).getPileProperty())) {
            int value = state.getValue(((AbstractWineBlock)state.getBlock()).getPileProperty());
            AbstractWineBlock newBlock = (AbstractWineBlock) thisBlock;
            if (value < newBlock.getMaxPile()) {
                level.setBlock(pos, state.setValue(((AbstractWineBlock)state.getBlock()).getPileProperty(), value + 1), 3);
                level.playSound(null, pos, getPlaceSound(state,level,pos,player), SoundSource.PLAYERS, 0.8F, 0.8F);
                if (!player.getAbilities().instabuild) {
                    pContext.getItemInHand().shrink(1);
                }
                return InteractionResult.SUCCESS;
            }
        }
        return InteractionResult.PASS;
    }

    @Override
    public void appendHoverText(ItemStack itemStack, TooltipContext context, TooltipDisplay display, Consumer<Component> builder, TooltipFlag tooltipFlag) {
        super.appendHoverText(itemStack, context, display, builder, tooltipFlag);
        builder.accept(Component.translatable("tooltips.bakeries.pile_item_place").withStyle(ChatFormatting.GRAY).withStyle(ChatFormatting.ITALIC));
        if (itemStack.has(DataComponents.FOOD) && itemStack.has(DataComponents.CONSUMABLE)){
            TextUtils.addFoodEffectTooltip(itemStack, builder, 1.0F,context.tickRate());
        }
    }

    public SoundEvent getPlaceSound(){
        return placeSound;
    }

    public static class PileProperties{
        private SoundEvent placeSound;
        private boolean customField;
        private Properties itemProperties;

        public PileProperties() {
            this.itemProperties = new Properties();
            this.placeSound = SoundEvents.GLASS_PLACE;
            this.customField = false;
        }

        public PileProperties itemProperties(Properties properties){
            this.itemProperties = properties;
            return this;
        }

        public PileProperties customField(boolean customField){
            this.customField = customField;
            return this;
        }

        public PileProperties placeSound(SoundEvent placeSound){
            this.placeSound = placeSound;
            return this;
        }
    }
}
