package core.qa.demo.enums;

public enum ContentType {

	XML("application/xml", "xml"), 
	JSON("application/json", "json"), 
	HTML("text/html", "html");

	String mime;
	String extension;

	ContentType(String mime, String extension) {
		this.mime = mime;
		this.extension = extension;
	}

	public String getMime() {
		return mime;
	}

	public String getExtension() {
		return extension;
	}
}
