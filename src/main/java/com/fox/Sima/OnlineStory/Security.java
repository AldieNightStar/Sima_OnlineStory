package com.fox.Sima.OnlineStory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public class Security {

    public static String standartPassword = "foxfoxfox";
    public static String passwordFile = "./password.ini";

    private static String getTruePassword() {
		String truePassword = standartPassword;
		try {
			truePassword = new String(Files.readAllBytes(Paths.get(passwordFile)));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return truePassword;
    }

    private static void writeNewPassword(String pass) {
		try {
			Files.write(Paths.get(passwordFile), pass.getBytes());
		} catch (Exception e) {
			e.printStackTrace();
		}
    }

    public static boolean isPasswordValid(String password) {
		String truePassword = getTruePassword();
		return truePassword.equals(password);
    }

    public static boolean changePassword(String oldPass, String newPass, String repeat) {
		String truePassword = getTruePassword();

		boolean isMatching = newPass.equals(repeat);
		boolean isOldPassRight = truePassword.equals(oldPass);

		if (!isMatching)
			return false;
		if (!isOldPassRight)
			return false;

		writeNewPassword(newPass);

		return true;
    }
}
