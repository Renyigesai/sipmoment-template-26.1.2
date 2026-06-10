package com.renyigesai.sip_moment.common.data;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public class WineListIngredient {

    private List<ItemStack> gives;
    private List<ItemStack> results;

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

    public WineListIngredient setGives(List<ItemStack> gives) {
        this.gives = gives;
        return this;
    }

    public WineListIngredient setResults(List<ItemStack> results) {
        this.results = results;
        return this;
    }

    public static List<ItemStack> toArr(ItemStack stack){
        return List.of(stack);
    }

    public static List<ItemStack> toArr(ItemStack stack1,ItemStack stack2){
        return List.of(stack1,stack2);
    }

}
