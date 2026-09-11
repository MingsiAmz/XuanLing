package com.mingsiamz.xuanlingmod.data;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mingsiamz.xuanlingmod.XuanLing;

public class DataManager {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().serializeNulls().create();
    private static final Path CONFIG_DIR = Paths.get(".", "config");
    private static Path MOD_CONFIG_DIR = CONFIG_DIR.resolve(XuanLing.MODID);

    public static void saveData(Object obj, String data) {
	try {
	    if (!Files.exists(MOD_CONFIG_DIR)) {
		Files.createDirectories(MOD_CONFIG_DIR);
	    }
	    Path configFile = MOD_CONFIG_DIR.resolve(data + ".json");
	    Writer writer = new FileWriter(configFile.toFile());
	    GSON.toJson(obj, writer);
	    writer.flush();
	    writer.close();
	} catch (IOException e) {
	    throw new Error(e);
	}
    }

    public static void loadData(Object result, String fileName) {
	try (Reader reader = new FileReader(MOD_CONFIG_DIR.resolve(fileName + ".json").toFile())){
	    result = GSON.fromJson(reader, result.getClass());
	} catch (IOException e) {
	    throw new Error(e);
	}
    }
}
