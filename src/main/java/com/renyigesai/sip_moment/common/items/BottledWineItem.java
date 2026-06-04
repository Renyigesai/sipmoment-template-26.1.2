package com.renyigesai.sip_moment.common.items;

import com.renyigesai.sip_moment.common.blocks.CupBlock;
import com.renyigesai.sip_moment.common.entitys.PouringWineEntity;
import com.renyigesai.sip_moment.common.init.SMEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

public class BottledWineItem extends PileItem{

    public final Supplier<Item> cupStack;
    public final Supplier<Item> wineStack;
    private float initY = 0.5f;
    private int color = 16733525;

    public BottledWineItem(Block block, Properties pProperties, Supplier<Item> cupStack, Supplier<Item> wineStack) {
        super(block, pProperties);
        this.cupStack = cupStack;
        this.wineStack = wineStack;
    }

    public BottledWineItem(Block block, Properties pProperties, Supplier<Item> cupStack, Supplier<Item> wineStack,int color,float initY) {
        super(block, pProperties);
        this.cupStack = cupStack;
        this.wineStack = wineStack;
        this.color = color;
        this.initY = initY;
    }

    @Override
    public @NotNull InteractionResult useOn(UseOnContext pContext) {
        BlockPos clickedPos = pContext.getClickedPos();
        Level level = pContext.getLevel();
        BlockState blockState = level.getBlockState(clickedPos);
        if (blockState.getBlock() instanceof CupBlock cupBlock && getCupStack().getItem() instanceof BlockItem blockItem){
            if (blockItem.getBlock() == cupBlock){
                if (level instanceof ServerLevel serverLevel){
                    PouringWineEntity pouringWineEntity = new PouringWineEntity(SMEntityTypes.POURING_WINE.get(), level);
                    ItemStack wine = new ItemStack(this);
                    pouringWineEntity.setWineStack(wine);
                    pouringWineEntity.setWineColor(color);
                    pouringWineEntity.setCupPos(clickedPos);
                    pouringWineEntity.setInitY(initY);
                    pouringWineEntity.setPos(clickedPos.getX() + 0.5,clickedPos.getY() + 0.5,clickedPos.getZ() + 0.5);
                    pouringWineEntity.setOwner(pContext.getPlayer());
                    float yRot = blockState.getValue(CupBlock.FACING).toYRot();
                    pouringWineEntity.setYRot(yRot);
                    pouringWineEntity.yRotO = yRot;
                    serverLevel.addFreshEntity(pouringWineEntity);
                    serverLevel.playSound(null,clickedPos, SoundEvents.BOTTLE_EMPTY, SoundSource.BLOCKS);
                    pContext.getItemInHand().shrink(1);
                }
                return InteractionResult.SUCCESS;
            }
        }
        return super.useOn(pContext);
    }

    public ItemStack getCupStack() {
        return new ItemStack(cupStack.get());
    }

    public ItemStack getWineStack() {
        return new ItemStack(wineStack.get());
    }

    public float getInitY() {
        return initY;
    }

    public int getColor() {
        return color;
    }
}
