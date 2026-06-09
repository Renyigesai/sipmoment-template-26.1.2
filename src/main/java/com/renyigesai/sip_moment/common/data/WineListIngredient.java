package com.renyigesai.sip_moment.common.data;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public class WineListIngredient {

    private final List<ItemStack> gives;
    private final List<ItemStack> results;

    public static final StreamCodec<RegistryFriendlyByteBuf, WineListIngredient> STREAM_CODEC = StreamCodec.composite(
            ItemStack.STREAM_CODEC.apply(ByteBufCodecs.list()),
            WineListIngredient::getGives,
            ItemStack.STREAM_CODEC.apply(ByteBufCodecs.list()),
            WineListIngredient::getResults,
            WineListIngredient::new
    );

    public WineListIngredient(List<ItemStack> gives, List<ItemStack> results) {
        this.gives = gives;
        this.results = results;
    }

    public List<ItemStack> getGives() {
        return gives;
    }

    public List<ItemStack> getResults() {
        return results;
    }

    public static List<ItemStack> toArr(ItemStack... stacks){
        return List.of(stacks);
    }

}
