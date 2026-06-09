package com.renyigesai.sip_moment.common.event;

import com.renyigesai.sip_moment.common.data.WineListIngredient;
import net.minecraft.resources.Identifier;
import net.neoforged.bus.api.Event;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class WineListInitEvent extends Event {

    private Map<Identifier,WineListIngredient> wineListMap;

    public WineListInitEvent(Map<Identifier,WineListIngredient> wineListMap) {
        this.wineListMap = wineListMap;
    }

    public void addWineList(Identifier id,WineListIngredient wineListIngredient){
        this.wineListMap.put(id,wineListIngredient);
    }

    public void removeWineList(Identifier id){
        if (this.wineListMap.get(id) != null){
            this.wineListMap.remove(id);
        }
    }

    public List<WineListIngredient> getWineList() {
        return wineListMap.values().stream().toList();
    }

    public Map<Identifier, WineListIngredient> getWineListMap() {
        return wineListMap;
    }
}
