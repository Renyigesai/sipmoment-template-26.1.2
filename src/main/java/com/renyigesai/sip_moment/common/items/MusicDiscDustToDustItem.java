package com.renyigesai.sip_moment.common.items;

import com.renyigesai.sip_moment.SipMomentMod;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.*;

public class MusicDiscDustToDustItem extends Item {
    public MusicDiscDustToDustItem(Identifier identifier) {
        super(new Item.Properties().stacksTo(1).rarity(Rarity.RARE).jukeboxPlayable(ResourceKey.create(Registries.JUKEBOX_SONG,Identifier.fromNamespaceAndPath(SipMomentMod.MODID,"dust_to_dust"))).setId(ResourceKey.create(Registries.ITEM,identifier)));
    }
}
