package com.renyigesai.sip_moment.common.init;

import com.renyigesai.sip_moment.SipMomentMod;
import com.renyigesai.sip_moment.common.items.WineItem;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class SMItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(SipMomentMod.MODID);

    public static final DeferredItem<Item> HIGHBALL;
    public static final DeferredItem<Item> KYOHO_WINE;
    public static final DeferredItem<Item> BUTTER_BEER;
    public static final DeferredItem<Item> BLACK_STAR_COFFEE;
    public static final DeferredItem<Item> WHISKY;
    public static final DeferredItem<Item> DAVID_MARTINEZ;


    static {
        HIGHBALL = wineItem(SMBlocks.HIGHBALL);
        KYOHO_WINE = wineItem(SMBlocks.KYOHO_WINE);
        BUTTER_BEER = wineItem(SMBlocks.BUTTER_BEER);
        BLACK_STAR_COFFEE = wineItem(SMBlocks.BLACK_STAR_COFFEE);
        WHISKY = wineItem(SMBlocks.WHISKY);
        DAVID_MARTINEZ = wineItem(SMBlocks.DAVID_MARTINEZ);
    }

    private static DeferredItem<Item> wineItem(Holder<Block> block){
        Identifier identifier = block.unwrapKey().get().identifier();
        return ITEMS.register(identifier.getPath(),()-> new WineItem(block.value(),new Item.Properties().useBlockDescriptionPrefix().setId(ResourceKey.create(Registries.ITEM,identifier))));
    }

    public static ResourceKey<Item> modItemId(String name){
        return ResourceKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath(SipMomentMod.MODID,name));
    }
}
