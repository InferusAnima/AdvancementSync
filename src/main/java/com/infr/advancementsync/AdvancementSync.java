package com.infr.advancementsync;

import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementProgress;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.players.PlayerList;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.player.AdvancementEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.event.lifecycle.InterModProcessEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.event.server.ServerStartingEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.rmi.registry.Registry;
import java.util.List;
import java.util.stream.Collectors;

// The value here should match an entry in the META-INF/mods.toml file
@Mod("advancementsync")
public class AdvancementSync
{
    // Directly reference a log4j logger.
    private static final Logger LOGGER = LogManager.getLogger();

    public AdvancementSync() {
        // Register the setup method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
        // Register the enqueueIMC method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::enqueueIMC);
        // Register the processIMC method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::processIMC);
        // Register the doClientStuff method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::doClientStuff);

        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);
    }

    private void setup(final FMLCommonSetupEvent event)
    {
        // some preinit code
        //LOGGER.info("HELLO FROM PREINIT");
        //LOGGER.info("DIRT BLOCK >> {}", Blocks.DIRT.getRegistryName());
    }

    private void doClientStuff(final FMLClientSetupEvent event) {
        // do something that can only be done on the client
        //LOGGER.info("Got game settings {}", event.getMinecraftSupplier().get().gameSettings);
    }

    private void enqueueIMC(final InterModEnqueueEvent event)
    {
        // some example code to dispatch IMC to another mod
        //InterModComms.sendTo("examplemod", "helloworld", () -> { LOGGER.info("Hello world from the MDK"); return "Hello world";});
    }

    private void processIMC(final InterModProcessEvent event)
    {
        // some example code to receive and process InterModComms from other mods
        /*LOGGER.info("Got IMC {}", event.getIMCStream().
                map(m->m.getMessageSupplier().get()).
                collect(Collectors.toList()));*/
    }
    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
        // do something when the server starts
        //LOGGER.info("HELLO from server starting");
    }

    @SubscribeEvent
    public void onAdvancement(AdvancementEvent event) {
        Advancement advancement = event.getAdvancement();
        PlayerList players =  event.getPlayer().getServer().getPlayerList();
        for (ServerPlayer i : players.getPlayers()) {
            AdvancementProgress advancementprogress = i.getAdvancements().getOrStartProgress(advancement);
            if (!advancementprogress.isDone()) {
                for(String s : advancementprogress.getRemainingCriteria()) {
                    i.getAdvancements().award(advancement, s);
                }
            }
        }

    }

    @SubscribeEvent
    public void onPlayerJoined(PlayerEvent.PlayerLoggedInEvent event) {
        PlayerList players = event.getPlayer().getServer().getPlayerList();
        ServerPlayer player = null;
        for (ServerPlayer i : players.getPlayers()) {
            if  (i.getUUID() == event.getPlayer().getUUID()) {
                player = i;
            }
        }
        for (ServerPlayer i : players.getPlayers()) {
            for (Advancement advancement : i.getServer().getAdvancements().getAllAdvancements()) {
                AdvancementProgress advancementprogress = i.getAdvancements().getOrStartProgress(advancement);
                if (advancementprogress.isDone()) {
                    AdvancementProgress advancementprogresss = player.getAdvancements().getOrStartProgress(advancement);
                    if (!advancementprogresss.isDone()) {
                        for(String s : advancementprogresss.getRemainingCriteria()) {
                            player.getAdvancements().award(advancement, s);
                        }
                    }

                }
            }
        }
        for (ServerPlayer i : players.getPlayers()) {
            for (Advancement advancement : i.getServer().getAdvancements().getAllAdvancements()) {
                AdvancementProgress advancementprogress = player.getAdvancements().getOrStartProgress(advancement);
                if (advancementprogress.isDone()) {
                    AdvancementProgress advancementprogresss = i.getAdvancements().getOrStartProgress(advancement);
                    if (!advancementprogresss.isDone()) {
                        for(String s : advancementprogresss.getRemainingCriteria()) {
                            i.getAdvancements().award(advancement, s);
                        }
                    }

                }
            }
        }
    }
}
