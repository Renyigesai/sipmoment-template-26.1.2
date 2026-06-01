package com.renyigesai.sip_moment.common.init;

import com.renyigesai.sip_moment.SipMomentMod;
import com.renyigesai.sip_moment.common.blocks.WineBlock;
import com.renyigesai.sip_moment.common.blocks.mix_block.MixBlock;
import com.renyigesai.sip_moment.common.blocks.mix_block.MixBlockEntity;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class SMBlocks {
    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(SipMomentMod.MODID);

    public static final DeferredBlock<MixBlock> MIX_BLOCK;
    public static final DeferredBlock<Block> HIGHBALL;
    public static final DeferredBlock<Block> KYOHO_WINE;
    public static final DeferredBlock<Block> BUTTER_BEER;
    public static final DeferredBlock<Block> BLACK_STAR_COFFEE;
    public static final DeferredBlock<Block> WHISKY;
    public static final DeferredBlock<Block> DAVID_MARTINEZ;

    static {
        MIX_BLOCK = BLOCKS.register("mix_block", MixBlock::new);

        HIGHBALL = wineBlock("highball",4);

        KYOHO_WINE = wineBlock("kyoho_wine",2);

        BUTTER_BEER = wineBlock("butter_beer",4);

        BLACK_STAR_COFFEE = wineBlock("black_star_coffee",2);

        WHISKY = wineBlock("whisky",4);
        DAVID_MARTINEZ = wineBlock("david_martinez",4);
    }

    private static DeferredBlock<Block> wineBlock(String name,int maxPile){
        return BLOCKS.register(name,()-> new WineBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.GLASS).setId(modBlockId(name)),maxPile));
    }

    public static ResourceKey<Block> modBlockId(String name){
        return ResourceKey.create(Registries.BLOCK,Identifier.fromNamespaceAndPath(SipMomentMod.MODID,name));
    }

    public static class Entitys{
        public static final DeferredRegister<BlockEntityType<?>> REGISTER = DeferredRegister.create(BuiltInRegistries.BLOCK_ENTITY_TYPE, SipMomentMod.MODID);
        public static final Supplier<BlockEntityType<MixBlockEntity>> MIX_BLOCK_ENTITY = REGISTER.register("mix_block", () -> new BlockEntityType<>(MixBlockEntity::new, MIX_BLOCK.get()));
    }
}
