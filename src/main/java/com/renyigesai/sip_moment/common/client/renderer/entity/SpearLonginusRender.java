package com.renyigesai.sip_moment.common.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.renyigesai.sip_moment.SipMomentMod;
import com.renyigesai.sip_moment.common.client.model.SpearLonginusModel;
import com.renyigesai.sip_moment.common.entitys.ThrownSpearLonginus;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.state.ThrownTridentRenderState;
import net.minecraft.client.renderer.feature.ItemFeatureRenderer;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Unit;

public class SpearLonginusRender extends EntityRenderer<ThrownSpearLonginus, ThrownTridentRenderState> {
    public static final Identifier TRIDENT_LOCATION = Identifier.fromNamespaceAndPath(SipMomentMod.MODID,"textures/entity/spear_longinus.png");
    private final SpearLonginusModel model;
    public SpearLonginusRender(EntityRendererProvider.Context context) {
        super(context);
        this.model = new SpearLonginusModel(context.bakeLayer(SpearLonginusModel.LAYER_LOCATION));
    }

    @Override
    public ThrownTridentRenderState createRenderState() {
        return new ThrownTridentRenderState();
    }

    public void extractRenderState(ThrownSpearLonginus entity, ThrownTridentRenderState state, float partialTicks) {
        super.extractRenderState(entity, state, partialTicks);
        state.yRot = entity.getYRot(partialTicks);
        state.xRot = entity.getXRot(partialTicks);
        state.isFoil = entity.isFoil();
    }

    public void submit(ThrownTridentRenderState state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState camera) {
        poseStack.pushPose();
        poseStack.mulPose(Axis.YP.rotationDegrees(state.yRot - 90.0F));
        poseStack.mulPose(Axis.ZP.rotationDegrees(state.xRot + 90.0F));
        poseStack.scale(2,2,2);
        submitNodeCollector.order(0)
                .submitModel(this.model, Unit.INSTANCE, poseStack, TRIDENT_LOCATION, state.lightCoords, OverlayTexture.NO_OVERLAY, state.outlineColor, null);
        if (state.isFoil) {
            submitNodeCollector.order(1)
                    .submitModel(
                            this.model,
                            Unit.INSTANCE,
                            poseStack,
                            ItemFeatureRenderer.getFoilRenderType(this.model.renderType(TRIDENT_LOCATION), false),
                            state.lightCoords,
                            OverlayTexture.NO_OVERLAY,
                            state.outlineColor,
                            null
                    );
        }

        poseStack.popPose();
        super.submit(state, poseStack, submitNodeCollector, camera);
    }
}
