package com.renyigesai.sip_moment.common.blocks.sofa;

import com.mojang.serialization.MapCodec;
import com.renyigesai.sip_moment.common.init.SMEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class BarChairBlock extends HorizontalDirectionalBlock {

    private static final VoxelShape BOX;

    public BarChairBlock(Properties pProperties) {
        super(pProperties);
    }

    @Override
    protected MapCodec<? extends HorizontalDirectionalBlock> codec() {
        return simpleCodec(BarChairBlock::new);
    }


    @Override
    public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        return BOX;
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        if (player.isShiftKeyDown()) {
            return InteractionResult.PASS;
        }
        sit(level,pos,player);
        return InteractionResult.SUCCESS;
    }

    private void sit(Level world, BlockPos pos, Player pPlayer){
        if (world.isClientSide())
            return;
        BarChairEntity seat = new BarChairEntity(SMEntityTypes.BAR_CHAIR.get(), world);
        seat.setPos(pos.getX() + 0.5f, pos.getY() + 0.75f, pos.getZ() + 0.5f);
        world.addFreshEntity(seat);
        pPlayer.startRiding(seat);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext pContext) {
        return this.defaultBlockState().setValue(FACING, pContext.getHorizontalDirection());
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

    static {
        BOX = box(4,0,4,12,14,12);
    }
}
