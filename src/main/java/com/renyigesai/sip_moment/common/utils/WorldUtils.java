package com.renyigesai.sip_moment.common.utils;
import com.renyigesai.sip_moment.SipMomentMod;
import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.core.Vec3i;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.ItemStackWithSlot;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.*;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.AABB;
import net.neoforged.neoforge.items.ItemStackHandler;

public class WorldUtils {

    public static void saveAllItems(ValueOutput output, NonNullList<ItemStack> itemStacks,String name) {
        ValueOutput.TypedOutputList<ItemStackWithSlot> itemsOutput = output.list(name, ItemStackWithSlot.CODEC);

        for (int i = 0; i < itemStacks.size(); i++) {
            ItemStack itemStack = itemStacks.get(i);
            if (!itemStack.isEmpty()) {
                itemsOutput.add(new ItemStackWithSlot(i, itemStack));
            }
        }
    }

    public static void loadAllItems(ValueInput input, NonNullList<ItemStack> itemStacks,String name) {
        for (ItemStackWithSlot item : input.listOrEmpty(name, ItemStackWithSlot.CODEC)) {
            if (item.isValidInContainer(itemStacks.size())) {
                itemStacks.set(item.slot(), item.stack());
            }
        }
    }

    @SuppressWarnings("removal")
    public static void loadItemsToHandler(ValueInput input, ItemStackHandler handler, String key) {
        NonNullList<ItemStack> nonNullList = NonNullList.withSize(handler.getSlots(),ItemStack.EMPTY);
        loadAllItems(input, nonNullList, key);
        for (int i = 0; i < nonNullList.size(); i++) {
            handler.setStackInSlot(i, nonNullList.get(i));
        }
    }

    @SuppressWarnings("removal")
    public static void saveItemsToHandler(ValueOutput output, ItemStackHandler handler, String key) {
        NonNullList<ItemStack> nonNullList = NonNullList.withSize(handler.getSlots(),ItemStack.EMPTY);
        saveAllItems(output, nonNullList, key);
        for (int i = 0; i < nonNullList.size(); i++) {
            handler.setStackInSlot(i, nonNullList.get(i));
        }
    }

    public static boolean isDoneAdvancement(Player player, Level level, Identifier resourceLocation){
        if (player instanceof ServerPlayer serverPlayer && level instanceof ServerLevel) {
            try {
                AdvancementHolder advancementHolder = serverPlayer.server.getAdvancements().get(resourceLocation);
                if (advancementHolder == null){
                    return false;
                }
                return serverPlayer.getAdvancements().getOrStartProgress(advancementHolder).isDone();
            }catch (Exception e){
                SipMomentMod.LOGGER.error("Failed to check advancement {} for player {}", resourceLocation, player.getName(), e);
                return false;
            }

        }
        return false;
    }

    public static BlockPos findSafePosition(ServerLevel level, double x, double z, double preferredY, EntityDimensions dims) {
        int bx = Mth.floor(x);
        int bz = Mth.floor(z);
        level.getChunk(bx >> 4, bz >> 4);

        int surfaceY = level.getHeightmapPos(
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                new BlockPos(bx, 0, bz)
        ).getY();
        int minY = level.getMinY() + 1;
        int maxY = level.getMaxY() - (int) Math.ceil(dims.height()) - 1;
        int startY = Mth.clamp((int) Math.max(preferredY, surfaceY), minY, maxY);
        for (int cy = startY; cy <= maxY; cy++) {
            AABB box = dims.makeBoundingBox(bx + 0.5, cy, bz + 0.5);

            if (!level.isInWorldBounds(BlockPos.containing(box.getCenter()))) {
                continue;
            }
            if (level.noCollision(box) && !level.containsAnyLiquid(box)) {
                return new BlockPos(bx, cy, bz);
            }
        }

        for (int cy = startY - 1; cy >= minY; cy--) {
            AABB box = dims.makeBoundingBox(bx + 0.5, cy, bz + 0.5);
            if (!level.isInWorldBounds(BlockPos.containing(box.getCenter()))) {
                continue;
            }
            if (level.noCollision(box) && !level.containsAnyLiquid(box)) {
                return new BlockPos(bx, cy, bz);
            }
        }
        return new BlockPos((int) x,(int)preferredY,(int)z);
    }

}
