package org.dev.babeltower.event;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.dev.babeltower.dto.RaidResultDTO;
import org.jetbrains.annotations.NotNull;

@Getter
@AllArgsConstructor
public class RaidIsOverEvent extends Event {

    private static final HandlerList handlers = new HandlerList();
    private final RaidResultDTO raidResult;

    @Override
    public @NotNull HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
