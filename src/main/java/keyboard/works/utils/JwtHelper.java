package keyboard.works.utils;

import java.util.Date;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import keyboard.works.security.SecurityConstants;

public class JwtHelper {

	public static String createToken(String subject) {
		return Jwts.builder()
			.setSubject(subject)
			.setExpiration(new Date(System.currentTimeMillis() + SecurityConstants.EXPIRATION_TIME))
			.signWith(SignatureAlgorithm.HS512, SecurityConstants.getTokenSecret())
			.compact();
	}
	
	public static String getSubject(String token) {
		return Jwts.parser()
				.setSigningKey(SecurityConstants.getTokenSecret())
				.parseClaimsJws(token)
				.getBody()
				.getSubject();
	}
	
}
