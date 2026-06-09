package com.renyigesai.sip_moment.common.entitys;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.renyigesai.sip_moment.common.blocks.CupBlock;
import com.renyigesai.sip_moment.common.client.particles.WineLiquidParticleOptions;
import com.renyigesai.sip_moment.common.init.SMSounds;
import com.renyigesai.sip_moment.common.items.BottledWineItem;
import com.renyigesai.sip_moment.common.utils.ItemUtils;
import it.unimi.dsi.fastutil.HashCommon;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.block.BlockModelRenderState;
import net.minecraft.client.renderer.block.BlockModelResolver;
import net.minecraft.client.renderer.block.model.BlockDisplayContext;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.client.renderer.item.ItemModelResolver;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.entity.IEntityWithComplexSpawn;
import org.jspecify.annotations.Nullable;

import java.awt.*;
import java.util.UUID;

public class PouringWineEntity extends Entity implements IEntityWithComplexSpawn {
    protected int time;
    private static final EntityDataAccessor<ItemStack> DATA_WINE_STACK = SynchedEntityData.defineId(PouringWineEntity.class, EntityDataSerializers.ITEM_STACK);
    private static final EntityDataAccessor<BlockPos> DATA_BLOCKPOS = SynchedEntityData.defineId(PouringWineEntity.class, EntityDataSerializers.BLOCK_POS);
    private static final EntityDataAccessor<Float> DATA_INIT_Y = SynchedEntityData.defineId(PouringWineEntity.class, EntityDataSerializers.FLOAT);
    protected static final EntityDataAccessor<String> DATA_OWNERUUID_ID = SynchedEntityData.defineId(
            PouringWineEntity.class, EntityDataSerializers.STRING
    );
    protected int wineColor;
    public PouringWineEntity(EntityType<?> type, Level level) {
        super(type, level);
    }

    @Override
    public void tick() {
        super.tick();
        if (level().isClientSide()){
            return;
        }
        if (time < 60){
            time ++;
            refreshExistence();
            if (level() instanceof ServerLevel serverLevel){
                if (time == 10){
                    serverLevel.playSound(null,this.getX(), this.getY(), this.getZ(), SMSounds.POURING_WINE.get(), SoundSource.BLOCKS);
                }
                if (time > 10 && time < 55){
                    WineLiquidParticleOptions particleOptions = new WineLiquidParticleOptions(
                            getWineColor(), 1.0f
                    );
                    serverLevel.sendParticles(particleOptions, this.getX(), this.getY() + getInitY() + 0.5f, this.getZ(), 1, 0, 0, 0, 0);
                }

            }
        }else {
            spawnWine();
            this.discard();
        }
    }

    public void spawnWine(){
        ItemStack wineStack = getWineStack();
        BlockPos cupPos = getCupPos();
        if (wineStack.getItem() instanceof BottledWineItem bottledWineItem){
            if (bottledWineItem.getWineStack().getItem() instanceof BlockItem blockItem){
                BlockState wineState = blockItem.getBlock().defaultBlockState();
                level().setBlock(cupPos,wineState,3);
            }else {
                ItemEntity itemEntity = new ItemEntity(level(),cupPos.getX(),cupPos.getY(),cupPos.getZ(),((BottledWineItem)getWineStack().getItem()).getWineStack());
                level().addFreshEntity(itemEntity);
                level().removeBlock(cupPos,false);
            }
        }
        returnWine();
        if (level() instanceof ServerLevel serverLevel){
            serverLevel.playSound(null,cupPos, SoundEvents.BOTTLE_FILL, SoundSource.BLOCKS);
        }
    }

    public void refreshExistence() {
        BlockState blockState = level().getBlockState(getCupPos());
        if (getWineStack().getItem() instanceof BottledWineItem bottledWineItem) {
            Item cupItem = bottledWineItem.getCupStack().getItem();
            if (!(cupItem instanceof BlockItem cupBlockItem) || blockState.getBlock() != cupBlockItem.getBlock()) {
                returnWine();
                this.discard();
            }
        }
    }

    public void returnWine(){
        LivingEntity owner = getOwner();
        if (owner != null){
            if (owner instanceof Player player && !player.hasInfiniteMaterials()){
                if (getWineStack().getItem() instanceof BottledWineItem bottledWineItem){
                    ItemStack newWine = bottledWineItem.onPouringWine(getWineStack()).copy();
                    ItemUtils.givePlayerItem(player,newWine);
                }
            }else {

            }
        }
    }

    @Override
    public void setDeltaMovement(Vec3 p_213317_1_) {}

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder entityData) {
        entityData.define(DATA_WINE_STACK, ItemStack.EMPTY);
        entityData.define(DATA_BLOCKPOS, BlockPos.ZERO);
        entityData.define(DATA_INIT_Y, 0f);
        entityData.define(DATA_OWNERUUID_ID, "");
    }

    @Override
    public boolean hurtServer(ServerLevel level, DamageSource source, float damage) {
        return false;
    }

    @Override
    protected void readAdditionalSaveData(ValueInput input) {
        String owner = input.getStringOr("Owner", "");
        this.entityData.set(DATA_OWNERUUID_ID,owner);
    }

    @Override
    protected void addAdditionalSaveData(ValueOutput output) {
        String ownerUUID = getOwnerUUID();
        output.putString("Owner",ownerUUID);
    }

    public void setWineStack(ItemStack wineStack) {
        this.entityData.set(DATA_WINE_STACK, wineStack);
    }

    public ItemStack getWineStack() {
        return this.entityData.get(DATA_WINE_STACK);
    }

    public void setCupPos(BlockPos pos) {
        this.entityData.set(DATA_BLOCKPOS, pos);
    }

    public BlockPos getCupPos() {
        return this.entityData.get(DATA_BLOCKPOS);
    }

    public void setWineColor(int wineColor) {
        this.wineColor = wineColor;
    }

    public int getWineColor() {
        return wineColor;
    }

    public float getInitY() {
        return this.entityData.get(DATA_INIT_Y);
    }

    public void setInitY(float initY) {
        this.entityData.set(DATA_INIT_Y,initY);
    }

    public void setOwner(@Nullable LivingEntity owner) {
        if (owner.getUUID() != null){
            this.entityData.set(DATA_OWNERUUID_ID, owner.getUUID().toString());
        }
    }


    @Override
    public void writeSpawnData(RegistryFriendlyByteBuf buffer) {

    }

    @Override
    public void readSpawnData(RegistryFriendlyByteBuf additionalData) {

    }

    public LivingEntity getOwner(){
        String string = this.entityData.get(DATA_OWNERUUID_ID);
        UUID uuid = UUID.fromString(string);
        Entity entity = level().getEntity(uuid);
        if (entity instanceof LivingEntity){
            return (LivingEntity) entity;
        }
        return null;
    }

    public String getOwnerUUID(){
       return this.entityData.get(DATA_OWNERUUID_ID);
    }

    public static class PouringWineRender extends EntityRenderer<PouringWineEntity,PouringWineRenderState> {
        public final ItemModelResolver itemModelResolver;
        public final BlockModelResolver blockModelResolver;
        public static final BlockDisplayContext BLOCK_DISPLAY_CONTEXT = BlockDisplayContext.create();

        public PouringWineRender(EntityRendererProvider.Context context) {
            super(context);
            this.itemModelResolver = context.getItemModelResolver();
            this.blockModelResolver = context.getBlockModelResolver();
        }

        @Override
        public PouringWineRenderState createRenderState() {
            return new PouringWineRenderState();
        }

        @Override
        public void extractRenderState(PouringWineEntity entity, PouringWineRenderState state, float partialTicks) {
            super.extractRenderState(entity, state, partialTicks);
            state.wineStack = entity.getWineStack();
            state.partialTicks = partialTicks;
            state.level = entity.level();
            BlockState blockState = entity.level().getBlockState(entity.getCupPos());
            if (blockState.hasProperty(CupBlock.FACING)){
                state.direction = blockState.getValue(CupBlock.FACING).getOpposite();
            }
            state.initY = entity.getInitY();
            state.ageInTicks = (entity.tickCount + partialTicks) / 20f;
            state.seed = HashCommon.long2int(entity.getOnPos().asLong());
        }

        @Override
        public void submit(PouringWineRenderState state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState camera) {
            animation(state, poseStack, submitNodeCollector, camera);
        }

        private void animation(PouringWineRenderState state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState camera){
            if (!state.wineStack.isEmpty() && state.wineStack.getItem() instanceof BlockItem){
                if (state.ageInTicks > 3.0f) return;
                poseStack.pushPose();
                applyPouringAnimation(state,poseStack);
                BlockModelRenderState block = new BlockModelRenderState();
                this.blockModelResolver.update(block, ((BlockItem)state.wineStack.getItem()).getBlock().defaultBlockState(),BLOCK_DISPLAY_CONTEXT);
                if (!block.isEmpty()){
                    block.submit(poseStack, submitNodeCollector, state.lightCoords, OverlayTexture.NO_OVERLAY, 0);
                }
                poseStack.popPose();
            }
        }

        private void applyPouringAnimation(PouringWineRenderState state, PoseStack poseStack) {
            float totalTime = state.ageInTicks;   // 实体存在时间（秒）
            float duration = 3.0f;                // 总动画时长

            float riseEnd = 1.0f;      // 上升持续时间
            float tiltStart = 0.5f;    // 倾斜开始时间
            float tiltEnd = 1.0f;      // 倾斜结束时间
            float fadeStart = 2.8f;    // 退场开始时间

            // ---- 统一的上升曲线（先快后慢，缓出） ----
            float riseProgress = Math.clamp(totalTime / riseEnd, 0f, 1f);
            float riseHeight = easeOutQuad(riseProgress) * 0.5f; // 上升0.3格

            // ---- 倾斜曲线（先快后慢） ----
            float tiltProgress = Math.clamp((totalTime - tiltStart) / (tiltEnd - tiltStart), 0f, 1f);
            float extraTilt = easeOutQuad(tiltProgress) * 20f;   // 额外向下倾斜15°

            // ---- 退场缩放曲线（先慢后快，缓入） ----
            float fadeProgress = Math.clamp((totalTime - fadeStart) / (duration - fadeStart), 0f, 1f);
            float scaleOut = 1.0f - easeInQuad(fadeProgress);    // 从1缩小到0

            // 1. 基础朝向（面向玩家）
            float rotation = -state.direction.toYRot();
            poseStack.mulPose(Axis.YP.rotationDegrees(rotation));

            // 2. 整体上升（基础高度0.5 + 动态上升）
            float yOffset = state.initY + riseHeight;
            poseStack.translate(-0.45, yOffset, 0);

            // 3. 倾斜（基础-65°，额外向下倾斜）
            float currentTilt = -65f - extraTilt;
            poseStack.mulPose(Axis.ZP.rotationDegrees(currentTilt));

            // 4. 动态缩放：退场时缩小，其他时间保持1
            float currentScale = (totalTime < fadeStart) ? 1.0f : scaleOut;
            poseStack.scale(currentScale, currentScale, currentScale);

            // 5. 补偿模型锚点
            poseStack.translate(-0.5, -0.5, -0.5);
        }

        private static float easeInOutCubic(float t) {
            return t < 0.5 ? 4 * t * t * t : 1 - (float) Math.pow(-2 * t + 2, 3) / 2;
        }

        /** 缓入曲线 */
        private static float easeInQuad(float t) {
            return t * t;
        }

        /** 缓出曲线 */
        private static float easeOutQuad(float t) {
            return 1 - (1 - t) * (1 - t);
        }

    }

    public static class PouringWineRenderState extends EntityRenderState{
        protected ItemStack wineStack;
        protected float partialTicks;
        protected Level level;
        protected int seed;
        protected Direction direction;
        protected float initY;
        protected float ageInTicks;

        public PouringWineRenderState() {
            this.direction = Direction.NORTH;
        }
    }
}
