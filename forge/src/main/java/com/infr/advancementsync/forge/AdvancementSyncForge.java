package com.infr.advancementsync.forge;

import dev.architectury.platform.forge.EventBuses;
import com.infr.advancementsync.AdvancementSync;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(AdvancementSync.MOD_ID)
public class AdvancementSyncForge {
    public AdvancementSyncForge() {
        // Submit our event bus to let architectury register our content on the right time
        EventBuses.registerModEventBus(AdvancementSync.MOD_ID, FMLJavaModLoadingContext.get().getModEventBus());
        AdvancementSync.init();
    }
}
