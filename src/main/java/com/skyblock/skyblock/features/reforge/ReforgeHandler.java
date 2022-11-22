package com.skyblock.skyblock.features.reforge;

import com.skyblock.skyblock.Skyblock;
import com.skyblock.skyblock.enums.Item;
import com.skyblock.skyblock.enums.Rarity;
import com.skyblock.skyblock.enums.Reforge;
import com.skyblock.skyblock.enums.SkyblockStat;
import org.bukkit.Bukkit;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.*;

public class ReforgeHandler {

    private final Skyblock skyblock;

    private final HashMap<Reforge, ReforgeData> reforges;

    public ReforgeHandler(Skyblock skyblock) {
        this.skyblock = skyblock;

        this.reforges = new HashMap<>();

        this.reforges.put(Reforge.NONE, new ReforgeData(Reforge.NONE, Item.NONE, new HashMap<Rarity, ReforgeStat>() {{
            put(Rarity.COMMON, new ReforgeStat(Rarity.COMMON, new HashMap<>()));
            put(Rarity.UNCOMMON, new ReforgeStat(Rarity.UNCOMMON, new HashMap<>()));
            put(Rarity.RARE, new ReforgeStat(Rarity.RARE, new HashMap<>()));
            put(Rarity.EPIC, new ReforgeStat(Rarity.EPIC, new HashMap<>()));
            put(Rarity.LEGENDARY, new ReforgeStat(Rarity.LEGENDARY, new HashMap<>()));
            put(Rarity.MYTHIC, new ReforgeStat(Rarity.MYTHIC, new HashMap<>()));
        }}));

        this.index();
    }

    public void index() {
        File file = new File(this.skyblock.getDataFolder(), "reforges.json");

        if (!file.exists()) {
            this.skyblock.sendMessage("&cCould not load dependency file &8reforges.json&c! Make sure you copy all files from &8dependencies/skyblock&c into the Skyblock plugin folder.");
            Bukkit.getPluginManager().disablePlugin(this.skyblock);
            return;
        }

        try {
            JSONArray reforges = (JSONArray) ((JSONObject) new JSONParser().parse(new BufferedReader(new InputStreamReader(Files.newInputStream(file.toPath()), StandardCharsets.UTF_8)))).get("reforges");

            for (Object object : reforges) {
                JSONObject reforge = (JSONObject) object;

                String name = (String) reforge.get("name");
                String applicable = (String) reforge.get("applicable");

                JSONArray data = (JSONArray) reforge.get("data");
                HashMap<Rarity, ReforgeStat> stats = new HashMap<>();

                for (Object obj : data) {
                    JSONObject stat = (JSONObject) obj;

                    String rarity = (String) stat.get("rarity");
                    JSONObject statsArray = (JSONObject) stat.get("stats");

                    HashMap<SkyblockStat, Integer> statsMap = new HashMap<>();

                    for (Object key : statsArray.keySet()) {
                        String statName = (String) key;
                        int statValue = ((Long) statsArray.get(statName)).intValue();

                        statsMap.put(SkyblockStat.valueOf(statName), statValue);
                    }

                    Rarity rarityEnum = Rarity.valueOf(rarity);

                    stats.put(rarityEnum, new ReforgeStat(rarityEnum, statsMap));
                }

                Reforge reforgeEnum = Reforge.valueOf(name.toUpperCase().replace(" ", "_"));
                Item item = Item.valueOf(applicable);

                this.reforges.put(reforgeEnum, new ReforgeData(reforgeEnum, item, stats));
            }
        } catch (IOException | ParseException ex) {
            throw new RuntimeException(ex);
        }
    }

    public void registerReforge(Reforge reforge, ReforgeData data) {
        this.reforges.put(reforge, data);
    }

    public ReforgeData getReforge(Reforge reforge) {
        return this.reforges.get(reforge);
    }

    public HashMap<Reforge, ReforgeData> getReforges() {
        return this.reforges;
    }

}
