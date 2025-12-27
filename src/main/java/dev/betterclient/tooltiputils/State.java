package dev.betterclient.tooltiputils;

import com.google.gson.Gson;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.world.item.ItemStack;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class State {
    public static boolean preRender = true;
    private static ItemStack lastHoveredStack = null;
    public static float scrollAmount = 0f;

    public static Config config = loadConfig();

    private static Config loadConfig() {
        Path resolve = FabricLoader.getInstance().getConfigDir().resolve("tooltiputils.json");
        try(FileInputStream fs = new FileInputStream(resolve.toFile())) {
            byte[] bytes = fs.readAllBytes();
            String json = new String(bytes);
            return new Gson().fromJson(json, Config.class);
        } catch (FileNotFoundException e) {
            return new Config();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void saveConfig() {
        Path resolve = FabricLoader.getInstance().getConfigDir().resolve("tooltiputils.json");
        try {
            String json = new Gson().toJson(config);
            Files.writeString(resolve, json);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void doScroll(ItemStack item, double amount) {
        if (!config.scrollEnabled) return;

        if (lastHoveredStack == null || lastHoveredStack != item) {
            lastHoveredStack = item;
            scrollAmount = 0f;
        } else {
            scrollAmount -= (float) amount * config.scrollMultiplier;
        }
    }

    public static void notifyItem(ItemStack item) {
        if (!config.scrollEnabled) return;

        //reset scroll amount if item changed
        if (lastHoveredStack != item) {
            lastHoveredStack = item;
            scrollAmount = 0f;
        }
    }

    public static class Config {
        public boolean tooltipEnabled = true;
        public float tooltipScale = 1f;

        public boolean scrollEnabled = true;
        public int scrollMultiplier = 15;

        public boolean expandWithShift = false; //if false always expanded
        public int cutOffLines = 10;

        public int tooltipX = -1;
        public int tooltipY = -1;
        public boolean lockedTooltipPosition = false;
    }
}