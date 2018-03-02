package com.fox.Sima.OnlineStory;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.*;
import java.util.regex.Pattern;

public class DB {
    private static HashMap<String, String> map = new HashMap<>();
    private static File file = new File("./db.ini");

    public static void set(String key, String value) {
	if (value == null) {
	    map.remove(key);
	    return;
	}
	map.put(key, value);
    }

    public static String get(String key) {
	return map.getOrDefault(key, "");
    }

    public static void save() {
	if (file.exists())
	    file.delete();
	try {
	    StringBuilder stringBuilder = new StringBuilder();

	    map.forEach((k, v) -> {
		stringBuilder.append(k + ":::" + Base64.getEncoder().encodeToString(v.getBytes()) + "\n");
	    });

	    byte[] bytes = stringBuilder.toString().getBytes();

	    Files.write(file.toPath(), bytes, StandardOpenOption.SPARSE, StandardOpenOption.CREATE);

	} catch (Exception e) {
	    e.printStackTrace();
	}
    }

    public static void load() {
	if (!file.exists())
	    return;
	try {

	    List<String> lines = Files.readAllLines(file.toPath());

	    for (String line : lines) {
		if (line.contains(":::")) {
		    String arr[] = line.split(Pattern.quote(":::"));
		    String key = arr[0];
		    String val = new String(Base64.getDecoder().decode(arr[1]));
		    map.put(key, val);
		}
	    }

	} catch (Exception e) {
	    e.printStackTrace();
	}
    }
}
