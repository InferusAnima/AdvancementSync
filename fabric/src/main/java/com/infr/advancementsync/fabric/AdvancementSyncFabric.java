package com.infr.advancementsync.fabric;

import com.infr.advancementsync.AdvancementSync;
import net.fabricmc.api.ModInitializer;

public class AdvancementSyncFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        AdvancementSync.init();
    }
}
