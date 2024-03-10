package org.dev.babeltower.event;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.dev.babeltower.dto.BabelTowerRaidResultDTO;
import org.jetbrains.annotations.NotNull;

@Getter
@AllArgsConstructor
public class BabelTowerRaidIsOverEvent extends Event {

    private static final HandlerList handlers = new HandlerList();
    private final BabelTowerRaidResultDTO raidResult;

    @Override
    public @NotNull HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
