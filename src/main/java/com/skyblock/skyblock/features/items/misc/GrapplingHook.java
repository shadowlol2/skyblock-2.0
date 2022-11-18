package com.skyblock.skyblock.features.items.misc;

import com.skyblock.skyblock.Skyblock;
import com.skyblock.skyblock.SkyblockPlayer;
import com.skyblock.skyblock.features.items.ListeningItem;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

public class GrapplingHook extends ListeningItem {

    public GrapplingHook(Skyblock plugin) {
        super(plugin.getItemHandler().getItem("GRAPPLING_HOOK.json"), "grappling_hook");
    }

    @EventHandler
    public void onFish(PlayerFishEvent e){
        if (e.getState().equals(PlayerFishEvent.State.CAUGHT_ENTITY)
            || e.getState().equals(PlayerFishEvent.State.FAILED_ATTEMPT)
            || e.getState().equals(PlayerFishEvent.State.IN_GROUND)){
            Player player = e.getPlayer();
            if (player.getItemInHand() != null) {
                if (player.getItemInHand().equals(getItem())) {
                    SkyblockPlayer skyblockPlayer = SkyblockPlayer.getPlayer(player);
                    if (skyblockPlayer.getCooldown(getInternalName())) {
                        skyblockPlayer.setCooldown(getInternalName(), 2);
                        Vector vector = player.getLocation().getDirection().multiply(4);
                        vector.setY(1);

                        try {
                            player.setVelocity(vector);
                        } catch (Exception ex) { }
                    }else{
                        player.sendMessage(ChatColor.RED + "Whow! Slow down there!");
                    }
                }
            }
        }
    }
}
