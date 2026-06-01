package com.renyigesai.sip_moment.common.client.renderer.blockentity;

import net.minecraft.client.renderer.block.BlockModelRenderState;
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import org.jspecify.annotations.Nullable;

public class BlockEntityRenderItemState extends BlockEntityRenderState {
    public final @Nullable ItemStackRenderState[] items;
    public final BlockModelRenderState[] blockStates;
    public final int count;

    public BlockEntityRenderItemState(int count) {
        this.count = count;
        this.items = new ItemStackRenderState[this.count];
        this.blockStates = new BlockModelRenderState[this.count];
    }
}
