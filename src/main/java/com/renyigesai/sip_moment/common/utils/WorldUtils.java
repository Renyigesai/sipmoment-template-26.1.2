package com.renyigesai.sip_moment.common.utils;
import com.renyigesai.sip_moment.SipMomentMod;
import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.core.NonNullList;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.ItemStackWithSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.*;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.neoforged.neoforge.items.ItemStackHandler;

public class WorldUtils {
//    /**
//     * Gets the capability of a block at a given location if it is loaded
//     *
//     * @param level   Level
//     * @param cap     Capability to look up
//     * @param pos     position
//     * @param state   the block state, if known, or {@code null} if unknown
//     * @param tile    the block entity, if known, or {@code null} if unknown
//     * @param context Capability context
//     *
//     * @return capability if present, null if either not found or not loaded
//     */
//    @Nullable
//    @Contract("null, _, _, _, _, _ -> null")
//    public static <CAP, CONTEXT> CAP getCapability(@Nullable Level level, BlockCapability<CAP, CONTEXT> cap, BlockPos pos, @Nullable BlockState state,
//                                                   @Nullable BlockEntity tile, CONTEXT context) {
//        if (!isBlockLoaded(level, pos)) {
//            //If the world is null, or it is a world reader and the block is not loaded, return null
//            return null;
//        }
//        return level.getCapability(cap, pos, state, tile, context);
//    }
//    /**
//     * Checks if a position is in bounds of the world, and is loaded
//     *
//     * @param world world
//     * @param pos   position
//     *
//     * @return True if the position is loaded or the given world is of a superclass of IWorldReader that does not have a concept of being loaded.
//     */
//    @Contract("null, _ -> false")
//    public static boolean isBlockLoaded(@Nullable BlockGetter world, @NotNull BlockPos pos) {
//        if (world == null) {
//            return false;
//        } else if (world instanceof LevelReader reader) {
//            if (reader instanceof Level level && !level.isInWorldBounds(pos)) {
//                return false;
//            }
//            //TODO: If any cases come up where things are behaving oddly due to the change from reader.hasChunkAt(pos)
//            // re-evaluate this and if the specific case is being handled properly
//            return isChunkLoaded(reader, pos);
//        }
//        return true;
//    }
//
//    /**
//     * Checks if the chunk at the given position is loaded but does not validate the position is in bounds of the world.
//     *
//     * @param world world
//     * @param pos   position
//     *
//     * @see #isBlockLoaded(BlockGetter, BlockPos)
//     */
//    @Contract("null, _ -> false")
//    public static boolean isChunkLoaded(@Nullable LevelReader world, @NotNull BlockPos pos) {
//        return isChunkLoaded(world, SectionPos.blockToSectionCoord(pos.getX()), SectionPos.blockToSectionCoord(pos.getZ()));
//    }
//    /**
//     * Checks if the chunk at the given position is loaded.
//     *
//     * @param world    world
//     * @param chunkPos Chunk position
//     */
//    @Contract("null, _ -> false")
//    public static boolean isChunkLoaded(@Nullable LevelReader world, ChunkPos chunkPos) {
//        return isChunkLoaded(world, chunkPos.x, chunkPos.z);
//    }
//
//    /**
//     * Checks if the chunk at the given position is loaded.
//     *
//     * @param world  world
//     * @param chunkX Chunk X coordinate
//     * @param chunkZ Chunk Z coordinate
//     */
//    @Contract("null, _, _ -> false")
//    public static boolean isChunkLoaded(@Nullable LevelReader world, int chunkX, int chunkZ) {
//        if (world == null) {
//            return false;
//        } else if (world instanceof LevelAccessor accessor) {
//            if (!(accessor instanceof Level level) || !level.isClientSide) {
//                return accessor.hasChunk(chunkX, chunkZ);
//            }
//            //Don't allow the client level to just return true for all cases, as we actually care if it is present
//            // and instead use the fallback logic that we have
//        }
//        return world.getChunk(chunkX, chunkZ, ChunkStatus.FULL, false) != null;
//    }
//
//    /*通过输入资源地址获取一个战利品表*/
//    public static LootTable getLootTables(ResourceLocation location, Level world){
//        if (!world.isClientSide() && world.getServer() != null) {
//            return world.getServer().reloadableRegistries().getLootTable(ResourceKey.create(Registries.LOOT_TABLE, location));
//        }
//        return LootTable.lootTable().build();
//    }
//    /*从战利品表中获取物品列表*/
//    public static List<ItemStack> getFromLootTableItemStack(LootTable lootTable, Level world, BlockPos pos){
//        List<ItemStack> stacks = new ArrayList<>();
//        if (!world.isClientSide() && world.getServer() != null){
//            stacks.addAll(lootTable.getRandomItems(new LootParams.Builder((ServerLevel) world).withParameter(LootContextParams.ORIGIN, Vec3.atCenterOf(pos)).withParameter(LootContextParams.BLOCK_STATE, world.getBlockState(pos)).withOptionalParameter(LootContextParams.BLOCK_ENTITY, world.getBlockEntity(pos)).create(LootContextParamSets.EMPTY)));
//        }
//        return stacks;
//    }
//
//    public static boolean isDoneAdvancement(Player player, Level level, Identifier resourceLocation){
//        if (player instanceof ServerPlayer serverPlayer && level instanceof ServerLevel) {
//            try {
//                AdvancementHolder advancementHolder = serverPlayer.server.getAdvancements().get(resourceLocation);
//                if (advancementHolder == null){
//                    return false;
//                }
//                return serverPlayer.getAdvancements().getOrStartProgress(advancementHolder).isDone();
//            }catch (Exception e){
//                SipMomentMod.LOGGER.error("Failed to check advancement {} for player {}", resourceLocation, player.getName(), e);
//                return false;
//            }
//
//        }
//        return false;
//    }

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

}
