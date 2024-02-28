package org.dev.babeltower.managers;

import java.util.HashMap;
import java.util.Map;
import org.bukkit.inventory.Inventory;

public class InventoryManager {

    private static InventoryManager instance;
    private final Map<Inventory, String> inventories;

    private InventoryManager() {
        instance = this;
        inventories = new HashMap<>();
    }

    public static synchronized InventoryManager getInstance() {
        if (instance == null) {
            instance = new InventoryManager();
        }
        return instance;
    }

    public void register(Inventory inventory, String title) {
        inventories.put(inventory, title);
    }

    public void unregister(Inventory inventory) {
        inventories.remove(inventory);
    }

    public String findTitle(Inventory inventory) {
        return inventories.get(inventory);
    }
    public boolean containInventory(Inventory inventory){
        return inventories.containsKey(inventory);
    }
}

