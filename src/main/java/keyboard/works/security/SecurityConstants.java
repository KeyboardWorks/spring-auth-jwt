package keyboard.works.security;

import keyboard.works.SpringAppContext;

public class SecurityConstants {

	public static final long EXPIRATION_TIME = 864000000; //in milisecond 10days
	public static final String TOKEN_PREFIX = "Bearer ";
	public static final String HEADER_STRING = "Authorization";
	public static final String SIGNUP_URL = "/users";
	
	public static String getTokenSecret() {
		AppProperties appProperties = SpringAppContext.getBean(AppProperties.class);
		
		return appProperties.getTokenSecret();
	}
	
}
