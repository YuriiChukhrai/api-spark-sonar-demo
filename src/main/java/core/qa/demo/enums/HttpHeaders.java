package core.qa.demo.enums;

public enum HttpHeaders {

	ACCEPT("Accept"), 
	ACCEPT_ENCODING("Accept-Encoding"), 
	CONTENT_TYPE("Content-Type"),
	
	USER_AGENT("User-Agent"),
	CONNECTION("Connection"),
	KEEP_ALIVE("Keep-Alive"),
	
	X_POWERED_BY("X-Powered-By");

	String header;

	HttpHeaders(String header) {
		this.header = header;
	}

	public String getHeader() {
		return header;
	}
}
