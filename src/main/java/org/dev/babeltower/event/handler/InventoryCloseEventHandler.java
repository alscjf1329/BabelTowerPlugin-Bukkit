package org.dev.babeltower.event.handler;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.dev.babeltower.factory.BabelTowerRewardFactory;
import org.dev.babeltower.managers.InventoryManager;
import org.dev.babeltower.managers.TowerManager;
import org.dev.babeltower.views.ChatView;

public class InventoryCloseEventHandler implements Listener {

    @EventHandler
    public void onInventoryCloseEvent(InventoryCloseEvent inventoryCloseEvent) {
        Inventory inventory = inventoryCloseEvent.getInventory();
        if (!InventoryManager.getInstance().containInventory(inventory)) {
            return;
        }
        Player player = (Player) inventoryCloseEvent.getPlayer();
        String title = InventoryManager.getInstance().findTitle(inventory);
        int floor = BabelTowerRewardFactory.getInstance().extractFloor(title);
        TowerManager.getInstance().updateReward(floor, inventory);

        ChatView.SUCCESS_TO_UPSERT_REWARD.sendTo(player, floor);
        InventoryManager.getInstance().unregister(inventory);
    }
}
