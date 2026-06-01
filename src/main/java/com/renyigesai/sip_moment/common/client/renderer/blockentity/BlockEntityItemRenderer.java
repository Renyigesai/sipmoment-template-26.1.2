package com.renyigesai.sip_moment.common.client.renderer.blockentity;

import com.mojang.blaze3d.vertex.PoseStack;
import it.unimi.dsi.fastutil.HashCommon;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.block.BlockModelRenderState;
import net.minecraft.client.renderer.block.BlockModelResolver;
import net.minecraft.client.renderer.block.model.BlockDisplayContext;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.item.ItemModelResolver;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.core.NonNullList;
import net.minecraft.world.entity.ItemOwner;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

public abstract class BlockEntityItemRenderer<T extends BlockEntity & ItemOwner,S extends BlockEntityRenderItemState> implements BlockEntityRenderer<@NotNull T, @NotNull S>{
    public final ItemModelResolver itemModelResolver;
    public final BlockModelResolver blockModelResolver;
    public static final BlockDisplayContext BLOCK_DISPLAY_CONTEXT = BlockDisplayContext.create();

    protected BlockEntityItemRenderer(BlockEntityRendererProvider.Context context) {
        this.itemModelResolver = context.itemModelResolver();
        this.blockModelResolver = context.blockModelResolver();
    }

    /**用于准备渲染数据，在这个获取方块数据赋值给你state的参数*/
    @Override
    public void extractRenderState(T blockEntity, @NotNull S state, float partialTicks, Vec3 cameraPosition, ModelFeatureRenderer.CrumblingOverlay breakProgress) {
        BlockEntityRenderer.super.extractRenderState(blockEntity, state, partialTicks, cameraPosition, breakProgress);
        NonNullList<ItemStack> items = items(blockEntity);
        int seed = HashCommon.long2int(blockEntity.getBlockPos().asLong());
        for (int slot = 0; slot < items.size(); ++slot) {
            ItemStack itemStack = items.get(slot);
            if (!itemStack.isEmpty()) {
                ItemStackRenderState itemStackRenderState = new ItemStackRenderState();
                BlockModelRenderState blockModelRenderState = new BlockModelRenderState();
                this.itemModelResolver.updateForTopItem(itemStackRenderState, itemStack, ItemDisplayContext.ON_SHELF, level(blockEntity), blockEntity, seed + slot);
                state.items[slot] = itemStackRenderState;
                if (itemStack.getItem() instanceof BlockItem blockItem){
                    this.blockModelResolver.update(blockModelRenderState, blockItem.getBlock().defaultBlockState(), BLOCK_DISPLAY_CONTEXT);
                    state.blockStates[slot] = blockModelRenderState;
                }
            }
        }
        state.blockPos = blockEntity.getBlockPos();
        extract(blockEntity, state, partialTicks, cameraPosition, breakProgress);
    }

    public abstract NonNullList<ItemStack> items(T blockEntity);

    public abstract Level level(T blockEntity);

    public void extract(T blockEntity, @NotNull S state, float partialTicks, Vec3 cameraPosition, ModelFeatureRenderer.CrumblingOverlay breakProgress){

    }

    /**这里就是渲染，理解成以前的render()方法就行*/
    @Override
    public void submit(@NotNull S state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState camera) {
        render(state.items,state.blockStates,state,poseStack,submitNodeCollector,camera);
    }

    public abstract void render(ItemStackRenderState[] itemStackRenderStates,BlockModelRenderState[] blockModelRenderStates,S state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState camera);

    public Vec3 transitionVec3(Vec2 position) {
        float x = position.x;
        float z = position.y;
        return new Vec3(x,0,z);
    }
}
