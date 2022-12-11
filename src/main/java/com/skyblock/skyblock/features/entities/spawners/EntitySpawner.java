package com.skyblock.skyblock.features.entities.spawners;

import com.skyblock.skyblock.Skyblock;
import com.skyblock.skyblock.features.entities.SkyblockEntity;
import com.skyblock.skyblock.features.entities.SkyblockEntityType;
import com.skyblock.skyblock.utilities.Util;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;

public class EntitySpawner {

    private final List<SkyblockEntity> spawned;
    private final List<Location> locations;
    private final long delay;
    private final int limit;
    private final int range;
    private final int amount;
    private final SkyblockEntityType type;
    private final String subType;
    private int locationRequests = 0;

    public EntitySpawner(ConfigurationSection section) {
        this.locations = (List<Location>) section.getList("locations");
        this.delay = section.getLong("delay");
        this.limit = section.getInt("limit");
        this.amount = section.getInt("amount");
        this.range = section.getInt("range");
        this.subType = section.getString("subType");
        this.type = SkyblockEntityType.valueOf(section.getString("type"));

        this.spawned = new ArrayList<>();
    }

    public void start() {
        new BukkitRunnable() {
            @Override
            public void run() {
                spawned.removeIf((entity) -> entity.getVanilla().isDead() || entity.getLifeSpan() <= 0);

                if (spawned.size() >= limit) return;

                for (Location loc : locations) {
                    if (!loc.getChunk().isLoaded()) continue;

                    for (int i = 0; i < amount; i++) {
                        Location rand = random(loc);

                        SkyblockEntity entity = type.getNewInstance(subType);

                        if (entity == null) continue;

                        entity.spawn(rand);
                        spawned.add(entity);
                    }
                }
            }
        }.runTaskTimer(Skyblock.getPlugin(), 5L, delay);
    }

    public Location random(Location loc) {
        if (locationRequests > 120) {
            locationRequests = 0;
            return loc;
        }

        int randX = Util.random(-1 * range, range);
        int randZ = Util.random(-1 * range, range);

        Location rand = loc.clone();
        rand.add(randX, 0, randZ);

        Block block = rand.getBlock();

        if (block.getType() != Material.AIR && block.getLocation().clone().add(0, 1, 0).getBlock().getType() == Material.AIR &&
                block.getLocation().clone().add(0, 2, 0).getBlock().getType() == Material.AIR) {
            return rand;
        }

        locationRequests++;
        return random(loc);
    }
}
