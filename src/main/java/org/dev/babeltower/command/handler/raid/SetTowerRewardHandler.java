package org.dev.babeltower.command.handler.raid;

import com.binggre.rpgsharpguildraid.objects.RaidReward;
import com.hj.rpgsharp.rpg.apis.rpgsharp.utils.Serializer;
import io.lumine.mythic.bukkit.utils.storage.sql.hikari.util.FastList;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.dev.babeltower.command.handler.CommandHandler;
import org.dev.babeltower.dto.TowerDTO;
import org.dev.babeltower.factory.BabelTowerRewardFactory;
import org.dev.babeltower.managers.InventoryManager;
import org.dev.babeltower.managers.TowerManager;
import org.dev.babeltower.views.ErrorChatView;
import org.jetbrains.annotations.NotNull;

public class SetTowerRewardHandler implements CommandHandler {

    @Override
    public boolean handle(@NotNull CommandSender commandSender, @NotNull String[] strings) {
        Player player = (Player) commandSender;
        if (!commandSender.isOp()) {
            ErrorChatView.IS_NOT_OP_ERROR.sendTo(player);
            return false;
        }
        int floor = Integer.parseInt(strings[1]);
        TowerDTO towerInfo = TowerManager.getInstance().findTowerInfo(floor);
        if (towerInfo == null) {
            ErrorChatView.NOT_FOUND_TOWER_INFO.sendTo(player, floor);
            return false;
        }

        String title = BabelTowerRewardFactory.getInstance().createTitle(floor);
        String serializedReward = towerInfo.getSerializedReward();
        Inventory tempInventory;
        if (serializedReward == null) {
            tempInventory = BabelTowerRewardFactory.getInstance().createInventory(floor);
        } else {
            tempInventory = Serializer.deserializeInventory(serializedReward);
        }
        InventoryManager.getInstance().register(tempInventory, title);
        ((Player) commandSender).openInventory(tempInventory);
        return true;
    }
}
