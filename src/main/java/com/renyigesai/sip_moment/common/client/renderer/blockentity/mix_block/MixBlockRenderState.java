package com.renyigesai.sip_moment.common.client.renderer.blockentity.mix_block;
import com.renyigesai.sip_moment.common.client.renderer.blockentity.BlockEntityRenderItemState;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.core.Direction;

public class MixBlockRenderState extends BlockEntityRenderItemState {
    public Direction facing;
    public Boolean tray;
    public MixBlockRenderState(int count) {
        super(count);
        facing = Direction.DOWN;
    }

    public int getInventoryCount() {
        int count = 0;
        for (ItemStackRenderState itemStackRenderState : items) {
            if (itemStackRenderState != null) {
                count++;
            }
        }
        return count;
    }

    public boolean isTray(){
        return tray;
    }
}
