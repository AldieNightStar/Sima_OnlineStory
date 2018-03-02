package com.fox.Sima.OnlineStory;

import com.google.common.io.Files;
import com.google.gson.annotations.SerializedName;
import spark.Service;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static com.fox.Sima.OnlineStory.Main.*;

public class ResFileManager {

    public static String resFolder = "./res/";

    public static File[] getFilesInResFolder() {
	File folder = new File(resFolder);
	try {
	    File[] files = folder.listFiles();
	    return files;
	} catch (Exception e) {
	    e.printStackTrace();
	    return new File[0];
	}
    }

    public static void initResFileManager(Service service) {

	// -=-=-=-=-=-=-=-=-=-=-=-=-=
	// JSON /json/resfiles
	// -=-=-=-=-=-=-=-=-=-=-=-=-=
	service.get("/json/resfiles", (request, response) -> {
	    File[] files = getFilesInResFolder();
	    List<String> fileNames = new ArrayList<>();

	    for (File f : files) {
		if (!f.isDirectory()) {
		    fileNames.add(f.getName());
		}
	    }

	    return fileNames;
	});

	// <<< --- DELETE FILE --- >>> \\
	service.post("/json/resfiles/delete", (request, response) -> {
	    DeleterJSON cmdJson = new DeleterJSON("", "");

	    try {
		    cmdJson = gson.fromJson(request.body(), DeleterJSON.class);
	    } catch (Exception e) {
		    e.printStackTrace();
	    }

	    if (Security.isPasswordValid(cmdJson.password)) {
            if (!new File(resFolder + cmdJson.fileName).delete()) { // If not deleted
                return "fail";
            }
		    return "success";
	    }

	    return "fail";
	});

    }

    /*
     * JSON Pattern: { "name": "01.jpg", "pass": "foxfoxfox" }
     */

    /*
     * === JavaScript === var xhr = new XMLHttpRequest(); xhr.open("POST",
     * "/json/resfiles/delete", false); xhr.send("{json}"); return xhr.response;
     */
    private static class DeleterJSON {
	@SerializedName("name")
	private String fileName;

	@SerializedName("pass")
	private String password;

	public DeleterJSON() {
	};

	public DeleterJSON(String fileName, String password) {
	    this.fileName = fileName;
	    this.password = password;
	}
    }
}
