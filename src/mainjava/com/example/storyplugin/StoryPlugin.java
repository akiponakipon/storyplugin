package com.example.storyplugin;

import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.advancement.Advancement;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class StoryPlugin extends JavaPlugin {

    @Override
    public void onEnable() {
        getLogger().info("StoryPlugin が有効になりました！");

        // advancements フォルダ内の全 JSON をロード
        String[] stories = {
                "end_arrival","end_dragon","end_ship","elytra","netherite",
                "ancient_city","warden_spawn","warden_defeat","wither",
                "first_death","trial_chamber","breeze_rod","woodland","totem",
                "shipwreck","pyramid","jungle_temple","first_village",
                "ocean_monument","elder_guardian","bastion"
        };

        for (String id : stories) {
            loadAdvancement(id);
        }
    }

    private void loadAdvancement(String id) {
        try (InputStream in = getResource("advancements/" + id + ".json")) {
            if (in == null) {
                getLogger().warning("進捗ファイルが見つかりません: " + id);
                return;
            }
            String json = new Scanner(in, StandardCharsets.UTF_8).useDelimiter("\\A").next();
            NamespacedKey key = new NamespacedKey(this, id);
            Advancement adv = Bukkit.getUnsafe().loadAdvancement(key, json);
            if (adv != null) {
                getLogger().info("進捗をロードしました: " + id);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // デバッグ用: プレイヤーに強制的に進捗を与える関数
    public void giveAdvancement(Player player, String id) {
        NamespacedKey key = new NamespacedKey(this, id);
        Advancement adv = Bukkit.getAdvancement(key);
        if (adv != null) {
            player.getAdvancementProgress(adv).awardCriteria("trigger");
        }
    }
}
