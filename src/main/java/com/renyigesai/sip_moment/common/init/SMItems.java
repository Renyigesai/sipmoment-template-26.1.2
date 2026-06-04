package com.renyigesai.sip_moment.common.init;

import com.renyigesai.sip_moment.SipMomentMod;
import com.renyigesai.sip_moment.common.items.BottledWineItem;
import com.renyigesai.sip_moment.common.items.PileItem;
import com.renyigesai.sip_moment.common.items.WineItem;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.Container;
import net.minecraft.world.Containers;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.component.Consumable;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public class SMItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(SipMomentMod.MODID);

    public static final FoodProperties COMMON_WINE = new FoodProperties.Builder().alwaysEdible().build();

    public static final DeferredItem<Item> HIGHBALL;
    public static final DeferredItem<Item> KYOHO_WINE;
    public static final DeferredItem<Item> KYOHO_WINE_CUP;
    public static final DeferredItem<Item> BUTTER_BEER;
    public static final DeferredItem<Item> BLACK_STAR_COFFEE;
    public static final DeferredItem<Item> WHISKY;
    public static final DeferredItem<Item> DAVID_MARTINEZ;
    public static final DeferredItem<Item> GOBLET;


    static {
        HIGHBALL = wineItem(SMBlocks.HIGHBALL, 6,SMConsumables.HIGHBALL);
        KYOHO_WINE = ITEMS.register("kyoho_wine",()-> new BottledWineItem(SMBlocks.KYOHO_WINE.get(),new Item.Properties().setId(modItemId("kyoho_wine")).useBlockDescriptionPrefix(),SMItems.GOBLET,SMItems.KYOHO_WINE_CUP,16733525,0.0f));
        KYOHO_WINE_CUP = wineItem(SMBlocks.KYOHO_WINE_CUP,4);
        BUTTER_BEER = wineItem(SMBlocks.BUTTER_BEER,6);
        BLACK_STAR_COFFEE = wineItem(SMBlocks.BLACK_STAR_COFFEE,4);
        WHISKY = wineItem(SMBlocks.WHISKY,4);
        DAVID_MARTINEZ = wineItem(SMBlocks.DAVID_MARTINEZ,4);
        GOBLET = ITEMS.register("goblet",()-> new PileItem(SMBlocks.GOBLET.get(),new Item.Properties().setId(modItemId("goblet")).useBlockDescriptionPrefix()));
    }

    private static DeferredItem<Item> wineItem(Holder<Block> block, int eatCount,Consumable consumable){
        Identifier identifier = block.unwrapKey().get().identifier();
        return ITEMS.register(identifier.getPath(),()-> new WineItem(block.value(),new Item.Properties().food(COMMON_WINE,consumable).stacksTo(1).useBlockDescriptionPrefix().setId(ResourceKey.create(Registries.ITEM,identifier)),eatCount));
    }

    private static DeferredItem<Item> wineItem(Holder<Block> block,int eatCount){
        Identifier identifier = block.unwrapKey().get().identifier();
        return ITEMS.register(identifier.getPath(),()-> new WineItem(block.value(),new Item.Properties().food(COMMON_WINE).stacksTo(1).useBlockDescriptionPrefix().setId(ResourceKey.create(Registries.ITEM,identifier)),eatCount));
    }

    public static ResourceKey<Item> modItemId(String name){
        return ResourceKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath(SipMomentMod.MODID,name));
    }
}
