package com.renyigesai.sip_moment.common.blocks.mix_block;

import com.mojang.serialization.MapCodec;
import com.renyigesai.sip_moment.common.blocks.AbstractPileBlock;
import com.renyigesai.sip_moment.common.utils.ItemUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
@SuppressWarnings("removal")
public class BeverageDisplayBlock extends BaseEntityBlock {

    public static final EnumProperty<Direction> FACING = BlockStateProperties.HORIZONTAL_FACING;
    public static final BooleanProperty TRAY = BooleanProperty.create("tray");

    public BeverageDisplayBlock(Identifier id) {
        super(Properties.of().strength(0.5F,0.5F).sound(SoundType.GLASS).noOcclusion().isRedstoneConductor((bs, br, bp) -> false).setId(ResourceKey.create(Registries.BLOCK,id)));
        this.registerDefaultState(this.stateDefinition.any().setValue(TRAY,false).setValue(FACING, Direction.NORTH));
    }

    public BeverageDisplayBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return simpleCodec(BeverageDisplayBlock::new);
    }

    public RenderShape getRenderShape(BlockState pState) {
        return RenderShape.INVISIBLE;
    }

    @Override
    public boolean addLandingEffects(BlockState state1, ServerLevel level, BlockPos pos, BlockState state2, LivingEntity entity, int numberOfParticles) {
        return true;
    }

    @Override
    public boolean addRunningEffects(BlockState state, Level level, BlockPos pos, Entity entity) {
        return true;
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext pContext) {
        return this.defaultBlockState().setValue(FACING, pContext.getHorizontalDirection());
    }

    @Override
    protected BlockState updateShape(BlockState state, LevelReader level, ScheduledTickAccess ticks, BlockPos pos, Direction facing, BlockPos neighbourPos, BlockState neighbourState, RandomSource random) {
        return facing == Direction.DOWN && !state.canSurvive(level, neighbourPos) ? Blocks.AIR.defaultBlockState() : super.updateShape(state, level, ticks, pos, facing, neighbourPos, neighbourState, random);
    }

    @Override
    public boolean canSurvive(@NotNull BlockState state, LevelReader level, BlockPos pos) {
        return level.getBlockState(pos.below()).isSolid();
    }
    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(TRAY,FACING);
    }

    @Override
    public @NotNull VoxelShape getShape(@NotNull BlockState state, @NotNull BlockGetter level, @NotNull BlockPos pos, @NotNull CollisionContext context) {
        return Block.box(1.0D, 0.0D, 1.0D, 15.0D, 4.0D, 15.0D);
    }

    @Override
    protected InteractionResult useItemOn(ItemStack itemStack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        if (level.isClientSide()){
            return InteractionResult.SUCCESS;
        }
        if (!player.isShiftKeyDown()){
//            ItemStack itemInHand = player.getItemInHand(hand);
//            if (itemInHand.is(BakeriesItems.WOOD_TRAY)){
//                level.setBlock(pos,state.setValue(TRAY,true),3);
//                return InteractionResult.SUCCESS;
//            }
            return take(level, pos, player);
        }
        return super.useItemOn(itemStack, state, level, pos, player, hand, hitResult);
    }

    public InteractionResult take(Level pLevel, BlockPos pPos, Player pPlayer){
        BlockEntity blockEntity = pLevel.getBlockEntity(pPos);
        BeverageDisplayBlockEntity mix;
        if (!(blockEntity instanceof BeverageDisplayBlockEntity)){
            return InteractionResult.FAIL;
        }
        mix = (BeverageDisplayBlockEntity) blockEntity;
        net.neoforged.neoforge.items.ItemStackHandler inventory = mix.getInventory();
        ItemStack outStack;
        int inventoryCount = mix.getInventoryCount();
        ItemStack stackInSlot = inventory.getStackInSlot(inventoryCount - 1);
        outStack = stackInSlot.copy();
        inventory.setStackInSlot(inventoryCount - 1,ItemStack.EMPTY);
        mix.updateBlock();
        if (inventoryCount == 1){
            pLevel.removeBlock(pPos,false);
        }
        SoundEvent soundEvent;
        if (outStack.getItem() instanceof BlockItem blockItem && blockItem.getBlock() instanceof AbstractPileBlock pileBlock){
            soundEvent = pileBlock.getTakeSound();
        }else {
            soundEvent = SoundEvents.ITEM_FRAME_REMOVE_ITEM;
        }
        ItemUtils.givePlayerItem(pPlayer,outStack);
        pLevel.playSound(null,pPos,soundEvent, SoundSource.BLOCKS);
        return InteractionResult.SUCCESS;
    }

    @Override
    public ItemStack getCloneItemStack(LevelReader level, BlockPos pos, BlockState state, boolean includeData, Player player) {
        BlockEntity blockEntity = level.getBlockEntity(pos);
        if (blockEntity instanceof BeverageDisplayBlockEntity mix){
            return new ItemStack(mix.getInventory().getStackInSlot(0).getItem());
        }
        return super.getCloneItemStack(level, pos, state, includeData, player);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return new BeverageDisplayBlockEntity(pPos,pState);
    }
}
