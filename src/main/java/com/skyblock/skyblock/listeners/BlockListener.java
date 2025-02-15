package com.skyblock.skyblock.listeners;

import com.skyblock.skyblock.SkyblockPlayer;
import com.skyblock.skyblock.enums.MiningMinionType;
import com.skyblock.skyblock.features.minions.CobblestoneMinion;
import com.skyblock.skyblock.features.minions.MiningMinion;
import com.skyblock.skyblock.utilities.Util;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class BlockListener implements Listener {

    @EventHandler(priority=EventPriority.HIGH)
    public void onBlockPlace(BlockPlaceEvent event) {
        Material previous = event.getBlock().getType();

        event.setCancelled(SkyblockPlayer.getPlayer(event.getPlayer()).isNotOnPrivateIsland());

        if (!SkyblockPlayer.getPlayer(event.getPlayer()).isNotOnPrivateIsland()) {
            ItemStack item = event.getItemInHand();

            if (item.getItemMeta().hasDisplayName()) {
                String display = event.getItemInHand().getItemMeta().getDisplayName();

                if (item.getItemMeta().getDisplayName().contains("Minion")) {
                    SkyblockPlayer player = SkyblockPlayer.getPlayer(event.getPlayer());

                    int minionsPlaced = ((List<Object>) player.getValue("island.minions")).size();
                    int minionSlots = (int) player.getValue("island.minion.slots");

                    if (minionsPlaced + 1 > minionSlots) {
                        event.setCancelled(true);
                        event.getBlock().setType(previous);
                        event.getPlayer().sendMessage(ChatColor.RED + "You have reached the maximum amount of minions you can place! (" + minionSlots + ")");
                        return;
                    }

                    new CobblestoneMinion().spawn(player, event.getBlock().getLocation().clone().add(0.5, 0, 0.5), Util.romanToDecimal(display.split(" ")[display.split(" ").length - 1]));
                    event.getPlayer().sendMessage(ChatColor.AQUA + String.format("You placed a minion! (%s/%s)", minionsPlaced + 1, minionSlots));
                    event.getPlayer().getWorld().getBlockAt(event.getBlock().getLocation()).setType(Material.AIR);
                }
            }
        }
    }

}
