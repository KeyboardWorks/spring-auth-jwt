package keyboard.works.security;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import keyboard.works.utils.JwtHelper;

public class AuthorizationFilter extends OncePerRequestFilter {

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		
		String token = getTokenHeader(request);
		
		if(token.isBlank()) {
			filterChain.doFilter(request, response);
			return;
		}
		
		UsernamePasswordAuthenticationToken authentication = getAuthentication(token);
		SecurityContextHolder.getContext().setAuthentication(authentication);
		
		filterChain.doFilter(request, response);
		
	}
	
	private UsernamePasswordAuthenticationToken getAuthentication(String token) {

		String user = JwtHelper.getSubject(token);
		return new UsernamePasswordAuthenticationToken(user, null, new ArrayList<>());
	}
	
	private String getTokenHeader(HttpServletRequest request) {
		
		String header = request.getHeader(SecurityConstants.HEADER_STRING);
		
		if(header != null && header.startsWith(SecurityConstants.TOKEN_PREFIX))
			return header.replace(SecurityConstants.TOKEN_PREFIX, "");
		
		return "";
	}

}
