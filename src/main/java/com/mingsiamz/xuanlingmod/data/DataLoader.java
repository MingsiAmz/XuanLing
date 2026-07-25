package com.mingsiamz.xuanlingmod.data;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.google.gson.JsonObject;

public class DataLoader {
    private static final Map<String, JsonObject> CACHE = new ConcurrentHashMap<>();
    private static WatchService watcher;

    public static void init(Path configDir) {
	try {
	    watcher = FileSystems.getDefault().newWatchService();
	    configDir.register(watcher, StandardWatchEventKinds.ENTRY_CREATE, StandardWatchEventKinds.ENTRY_MODIFY, StandardWatchEventKinds.ENTRY_DELETE);
	    startWatchLoop();
	}catch (IOException e) {
	    System.out.println("Error " + e.getMessage());
	    e.printStackTrace();
	    throw new Error(e);
	}
    }

    private static void startWatchLoop() {
	Thread watchTd = new Thread(){
	    @Override
	    public void run() {
		while (true) {
		    try {
			WatchKey key = watcher.take();
			for (WatchEvent<?> event : key.pollEvents()) {
			    Path changedFile = (Path) event.context();
			    if (event.kind() == StandardWatchEventKinds.ENTRY_CREATE || event.kind() == StandardWatchEventKinds.ENTRY_MODIFY) {
				// reloadFile : void
			    } else if( event.kind() == StandardWatchEventKinds.ENTRY_DELETE) {
				CACHE.remove(changedFile.getFileName().toString());
			    }
			}
			key.reset();
		    }catch (InterruptedException e) {
			System.out.println("Error " + e.getMessage());
			e.printStackTrace();
		    }
		}
	    }
	};
	watchTd.setName("XuanLing-DataWatcher");
	watchTd.setDaemon(true);
	watchTd.start();
    }
}
