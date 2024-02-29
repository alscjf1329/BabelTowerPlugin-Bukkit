package org.dev.babeltower.event.handler;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.dev.coordinateplugin.dto.CoordinateDTO;
import org.dev.coordinateplugin.holders.CoordinateHolder;

public class SetPositionEventHandler implements Listener {

    @EventHandler
    public void onClick(PlayerInteractEvent event) {
        if (event.getHand() == EquipmentSlot.HAND) {
            Player player = event.getPlayer();
            Block clickedBlock = event.getClickedBlock();
            if (!CoordinateHolder.getInstance().validateRegisteredPlayer(player)) {
                return;
            }
            if (clickedBlock == null) {
                return;
            }
            CoordinateDTO coordinate = CoordinateHolder.getInstance().findBy(player);
            if (event.getAction() == Action.LEFT_CLICK_BLOCK) {
                event.setCancelled(true);
                coordinate.setPos1(clickedBlock.getLocation());
                player.sendMessage("포지션1 이 선택되었습니다.",
                    clickedBlock.getLocation().toString());
            } else if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
                event.setCancelled(true);
                coordinate.setPos2(clickedBlock.getLocation());
                player.sendMessage("포지션2 이 선택되었습니다.",
                    clickedBlock.getLocation().toString());
            }
            CoordinateHolder.getInstance().put(player, coordinate);
        }
    }
}
