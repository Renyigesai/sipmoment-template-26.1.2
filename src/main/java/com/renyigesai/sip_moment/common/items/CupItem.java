package com.renyigesai.sip_moment.common.items;

import net.minecraft.world.level.block.Block;

public class CupItem extends PileItem{
    public CupItem(Block block, Properties pProperties, boolean effectTooltip, boolean customField) {
        super(block, pProperties, effectTooltip, customField);
    }

    public CupItem(Block block, PileProperties pileProperties) {
        super(block, pileProperties);
    }

    public CupItem(Block block, Properties pProperties) {
        super(block, pProperties);
    }
}
