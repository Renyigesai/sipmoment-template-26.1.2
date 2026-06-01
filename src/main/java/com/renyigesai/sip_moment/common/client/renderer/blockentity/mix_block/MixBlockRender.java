package com.renyigesai.sip_moment.common.client.renderer.blockentity.mix_block;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.renyigesai.sip_moment.common.blocks.mix_block.MixBlock;
import com.renyigesai.sip_moment.common.blocks.mix_block.MixBlockEntity;
import com.renyigesai.sip_moment.common.client.renderer.blockentity.BlockEntityItemRenderer;
import com.renyigesai.sip_moment.common.utils.ItemUtils;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.block.BlockModelRenderState;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

public class MixBlockRender extends BlockEntityItemRenderer<MixBlockEntity, MixBlockRenderState> {

    public static final float ADD_SIZE = 0.25f;
    public static final Vec2[][] VEC2S = {
            new Vec2[]{},
            new Vec2[]{new Vec2(0.5f, 0.5f)},
            new Vec2[]{new Vec2(0.5f + ADD_SIZE, 0.5f), new Vec2(0.5f - ADD_SIZE, 0.5f)},
            new Vec2[]{new Vec2(0.5f + ADD_SIZE, 0.5f + ADD_SIZE), new Vec2(0.5f - ADD_SIZE, 0.5f + ADD_SIZE), new Vec2(0.5f, 0.5f - ADD_SIZE)},
            new Vec2[]{new Vec2(0.5f + ADD_SIZE, 0.5f + ADD_SIZE), new Vec2(0.5f - ADD_SIZE, 0.5f + ADD_SIZE), new Vec2(0.5f + ADD_SIZE, 0.5f - ADD_SIZE), new Vec2(0.5f - ADD_SIZE, 0.5f - ADD_SIZE)}
    };

    public MixBlockRender(BlockEntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public NonNullList<ItemStack> items(MixBlockEntity blockEntity) {
        return ItemUtils.toNonNullList(blockEntity.getInventory());
    }

    @Override
    public void extract(MixBlockEntity blockEntity, @NotNull MixBlockRenderState state, float partialTicks, Vec3 cameraPosition, ModelFeatureRenderer.CrumblingOverlay breakProgress) {
        state.facing = blockEntity.getBlockState().getValue(BlockStateProperties.HORIZONTAL_FACING).getOpposite();
        state.tray = blockEntity.getBlockState().getValue(MixBlock.TRAY);
    }

    @Override
    public Level level(MixBlockEntity blockEntity) {
        return blockEntity.level();
    }

    @Override
    public void render(ItemStackRenderState[] itemStackRenderStates, BlockModelRenderState[] blockModelRenderStates, MixBlockRenderState state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState camera) {
        if (!(state.getInventoryCount() >= 1)){
            return;
        }
        for (int slot = 0; slot < blockModelRenderStates.length; ++slot) {
            BlockModelRenderState blockModelRenderState = blockModelRenderStates[slot];
            if (blockModelRenderState != null){
                Direction facing = state.facing;
                float rotation = -facing.toYRot();
                Vec2 pos2d = transformPositionByDirection(VEC2S[state.getInventoryCount()][slot],facing);
                // ������Ʒ�������еľ������꣨���߶ȣ�
                Vec3 worldPos = transitionVec3(pos2d);
                poseStack.pushPose();
                // 1. ƽ�Ƶ���Ʒ������λ��
                poseStack.translate(worldPos.x, 0.5, worldPos.z);
                // 2. ��Ʒ�������ת��15��΢�� + ������ת��
                poseStack.mulPose(Axis.YP.rotationDegrees(rotation + 15));
                // 3. ����
                float size = 1.0f;
                poseStack.scale(size, size, size);
                // 4. ����ģ��ê�㣺ʹģ�����Ķ��뵱ǰԭ��
                poseStack.translate(-0.5, -0.5, -0.5);
                // 5. �ύ
                blockModelRenderState.submit(poseStack, submitNodeCollector, state.lightCoords, OverlayTexture.NO_OVERLAY, 0);
                poseStack.popPose();
            }
        }
    }

    @Override
    public MixBlockRenderState createRenderState() {
        return new MixBlockRenderState(4);
    }

    private Vec2 transformPositionByDirection(Vec2 position, Direction direction) {
        float x = position.x;
        float y = position.y;
        return switch (direction) {
            case NORTH -> new Vec2(1 - x, 1 - y);
            case SOUTH -> new Vec2(x, y);
            case EAST -> new Vec2(y, 1 - x);
            case WEST -> new Vec2(1 - y, x);
            default -> position;
        };
    }
}
