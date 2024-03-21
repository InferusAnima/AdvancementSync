package com.infr.advancementsync;

import dev.architectury.event.events.common.PlayerEvent;
import net.minecraft.advancements.AdvancementProgress;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.advancements.Advancement;
import net.minecraft.server.players.PlayerList;

public class AdvancementSync {
    public static final String MOD_ID = "advancementsync";
    
    public static void init() {
        PlayerEvent.PLAYER_ADVANCEMENT.register((ServerPlayer player, Advancement advancement) -> {
            PlayerList players = player.server.getPlayerList();
            for (ServerPlayer i : players.getPlayers()) {
                AdvancementProgress advancementprogress = i.getAdvancements().getOrStartProgress(advancement);
                if (!advancementprogress.isDone()) {
                    for(String s : advancementprogress.getRemainingCriteria()) {
                        i.getAdvancements().award(advancement, s);
                    }
                }
            }
        });

        PlayerEvent.PLAYER_JOIN.register((ServerPlayer player) -> {
            PlayerList players = player.server.getPlayerList();
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
        });
    }
}
