package com.fox.Sima.OnlineStory;

public class SimaPage {
    public static final String cssLocation = "/simastyle.css";

    private static String simaHTMLHead(String title, String content) {
		// Buffer String for appending
		StringBuffer buffer = new StringBuffer();

		// Tabulation
		buffer.append("<html><head><title>" + title + "</title>\n");
		buffer.append("<link rel=\"stylesheet\" href=\"" + cssLocation + "\">\n");
		buffer.append("<meta charset=\"UTF-8\">\n");
		buffer.append("</head>");
		buffer.append("<body>");
		buffer.append(content); // Here is our content
		buffer.append("\n\n");
		buffer.append("</body>");

		// Done
		return buffer.toString();
    }

    public static String getOkPage(String message, String okButtonLink) {
		// Buffer String for appending
		StringBuffer buffer = new StringBuffer();

		// Onclick location change <--
		String onclickFunction = "window.location = '" + okButtonLink + "';";

		// Appending
		buffer.append("<div id=\"background\">\n");
		buffer.append("<div id=\"text\">" + message + "</div>\n"); // Message here
		buffer.append("<div id=\"buttons\">\n  <button onclick=\"" + onclickFunction + "\">OK</button></div>"); // Here
															// is
															// location
		buffer.append("</div>");

		// To String from Buffer
		String content = buffer.toString();

		// Done
		return simaHTMLHead("Sima Message", content);
    }
}
