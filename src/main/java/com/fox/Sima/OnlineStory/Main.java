package com.fox.Sima.OnlineStory;

import com.fox.spark.JQuerry.JQuerry;
import com.google.gson.Gson;
import spark.Service;
import spark.utils.IOUtils;
import spark.utils.urldecoding.UrlDecode;

import javax.swing.*;
import java.io.File;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Base64;

public class Main {
    protected static Service service = null;
    public static Gson gson = new Gson();

    public static void main(String[] args) {
	// Create Service
	service = Service.ignite().port(8080);

	DB.load(); // Load DataBase

	// For DB safety
	// Saving files every 10 sec
	Timer timer = new Timer(10000, (q) -> {
	    DB.save();
	});
	timer.start();

	// <<< --- Init JQuerry lib --- >>> \\
	JQuerry.initJQuerryRoute(service, "/jq.js");

	// <<< --- Init ResFileManager --- >>> \\
	ResFileManager.initResFileManager(service);

	// <<< --- Root [GET] --- >>> \\
	service.get("/", (request, response) -> {
	    // Wee need to get index.html near the Main class
	    response.type("text/html");
	    try {
			InputStream stream = Main.class.getResourceAsStream("index.html");
			byte[] bytes = IOUtils.toByteArray(stream);
			stream.close();
			return bytes;
	    } catch (Exception e) {
			e.printStackTrace();
		return SimaPage.getOkPage("Main file [index.html] error!", "/f/edit.html");
	    }
	});

	// =-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-
	// Engine JS
	// =-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-
	service.get("/engineScript", (request, response) -> {

	    // We need to get Engine.js near the Main class
	    try {
			InputStream stream = Main.class.getResourceAsStream("engine.js");
			byte[] bytes = IOUtils.toByteArray(stream);
			stream.close();
			response.type("text/javascript");
			return bytes;
	    } catch (Exception e) {
			e.printStackTrace();
			response.type("text/html");
			return SimaPage.getOkPage("Something wrong with engine.js!", "/f/edit.html");
	    }
	});

	// ======================================
	// Game JavaScript
	// ======================================
	service.get("/gameScript", (request, response) -> {
	    response.type("text/javascript");
	    try {
			byte[] bytes = Files.readAllBytes(Paths.get("game.js"));
			return bytes;
	    } catch (Exception e) {
			return "alert(\"No GameScript! Create game.js in the Root Folder of the SIMA!\");";
	    }
	});

	// ======================================
	// Change Script (POST UPLOADING: /changeScript )
	// ======================================
	// ======================================
	// TODO Parse text, and make it upload
	// ======================================
	// Also protected with [ password ] :)
	service.post("/changeScript", (request, response) -> {
	    String contentString = request.queryParams("text");
	    String enteredPassword = request.queryParams("password");
	    String truePassword = "foxfoxfox";
	    File f = new File("./game.js");

	    // Try to load Password file
	    // -=-=-=-=-=-=-=-=-=-=-=-=-
	    try {
		// Reading Password
			truePassword = String.join("", Files.readAllLines(Paths.get("./password.ini")));
	    } catch (Exception e) {
			response.status(404);
			response.type("text/html");

			return SimaPage.getOkPage("Something wrong with password file /password.ini", "/");
	    }

	    if (enteredPassword.equals(truePassword)) {
		// CORRECT PASS
		// Delete previous file
		if (f.exists())
		    f.delete();
		// Create new game.js
		f.createNewFile();
		// Write data to it
		Files.write(f.toPath(), contentString.getBytes(), StandardOpenOption.CREATE, StandardOpenOption.SPARSE);
		// TODO Before upload here, u must to Parse it and save PARSED as JS file :)
		// TODO LangName: Sima Dragon
	    } else {
		// WRONG PASS
			return SimaPage.getOkPage("Wrong password!", "/");
	    }

	    // Done
	    response.redirect("/");
	    return "";
	});

	// =-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-
	// GET RES: SimaStyle.css
	// =-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-
	service.get("/simastyle.css", ((request, response) -> {
	    response.type("text/css");

	    try {
			byte[] bytes = IOUtils.toByteArray(Main.class.getResourceAsStream("style.css"));
			return bytes;
	    } catch (Exception e) {
			e.printStackTrace();
	    }
	    return " ".getBytes();
	}));

	// =-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-
	// GET RES: ajax.js
	// =-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-
	service.get("/ajax.js", (request, response) -> {
	    response.type("text/javascript");

	    try {
			byte[] bytes = IOUtils.toByteArray(Main.class.getResourceAsStream("ajax.js"));
			return bytes;
	    } catch (Exception e) {
			e.printStackTrace();
	    }
	    return " ".getBytes();
	});

	// ======================================
	// Change Password
	// First we check for old and repeat :)
	// ======================================
	service.post("/changePass", (request, response) -> {
	    String oldpass = request.queryParams("oldpass");
	    String newpass = request.queryParams("newpass");
	    String repeatpass = request.queryParams("repeat");
	    String currentPass = Security.standartPassword;

	    // Get Pass
	    try {
			currentPass = String.join("", Files.readAllLines(Paths.get("password.ini")));
	    } catch (Exception e) {
			System.out.println("Warning! Password is  standart: foxfoxfox");
			System.out.println("Something went wrong with file: password.ini");
	    }

	    // Look for old and Current
	    if (currentPass.equals(oldpass)) {
		// Then changing
		// But only if new and repeat are equals
		if (newpass.equals(repeatpass)) {
		    Files.write(Paths.get("password.ini"), newpass.getBytes(), StandardOpenOption.TRUNCATE_EXISTING,
			    StandardOpenOption.CREATE);
		} else {
		    // If not...
		    return SimaPage.getOkPage("Error! Passwords did not match!", "/f/password.html");
		}
	    } else {
		// If wrong
			return SimaPage.getOkPage("Wrong old password! Please remember it :)", "/f/edit.html");
	    }

	    return SimaPage.getOkPage("Success!", "/f/edit.html");
	});

	// ======================================
	// Resources
	// ======================================
	service.get("/res/*", (q, r) -> {
	    String uri = q.uri().substring("/res/".length());
	    try {
			byte[] bytes = Files.readAllBytes(Paths.get("./res/" + uri));
			return bytes;
	    } catch (Exception e) {
	    }

	    // If not found
	    r.status(404);

	    return SimaPage.getOkPage("(404) -- File not found!", "/");
	});

	// ======================================
	// Static Files
	// ======================================
	service.get("/f/*", (request, response) -> {
	    String uri = request.uri().substring("/f/".length());
	    File file = new File("./files/" + uri);
	    try {
			byte[] b = Files.readAllBytes(file.toPath());

			// By type
			if (uri.endsWith(".css")) {
				response.type("text/css");
			} else if (uri.endsWith(".js")) {
				response.type("text/javascript");
			} else if (uri.endsWith(".html")) {
				response.type("text/html");
			}

			return b;
	    } catch (Exception e) {
			response.status(404);
			return SimaPage.getOkPage("(404) -- File not found", "/");
	    }
	});

	// =-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-
	// DataBase [SET data]
	// =-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-
	service.get("/db/set/:k/:v", (request, response) -> {
	    String key = request.params("k");
	    String val = request.params("v");
	    // Decode url
	    key = UrlDecode.path(key);
	    try {
			val = new String(Base64.getDecoder().decode(val));
	    } catch (Exception e) {
	    }

	    DB.set(key, val);
	    return key + " is now: " + val;
	});

	// =-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-
	// DataBase [GET data]
	// =-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-
	service.get("/db/get/:k", (request, response) -> {
	    String key = request.params("k");

	    // Decode url
	    key = UrlDecode.path(key);

	    return DB.get(key);
	});

    } // end main
}
