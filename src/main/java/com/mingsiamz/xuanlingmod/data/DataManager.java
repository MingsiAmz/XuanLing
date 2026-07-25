package com.mingsiamz.xuanlingmod.data;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.spongepowered.include.com.google.gson.Gson;
import org.spongepowered.include.com.google.gson.GsonBuilder;

import com.mingsiamz.xuanlingmod.XuanLing;

public class DataManager {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final Path CONFIG_DIR = Paths.get(".", "config");

    public static void saveData(String data) {
	try {
	    Path modConfigDir = CONFIG_DIR.resolve(XuanLing.MODID);
	    if (!Files.exists(modConfigDir)) {
		Files.createDirectories(modConfigDir);
	    }
	    Path configFile = modConfigDir.resolve("");
	    Writer writer = new FileWriter(configFile.toFile());
	    GSON.toJson(data, writer);
	} catch (IOException e) {
	    throw new Error(e);
	}
    }

    // public static void loadData(String test) {
    // 	Path configFile = CONFIG_DIR.resolve(XuanLing.MODID).resolve("data_name.json");
    // 	if (Files.exists(configFile)) {
    // 	    try (Reader reader = new FileReader(configFile.toFile())) {
    // 		return GSON.fromJson(reader, DATA.class);
    // 	    } catch (IOException e) {
    // 		throw new Error(e);
    // 	    }
    // 	}
    // }
}
