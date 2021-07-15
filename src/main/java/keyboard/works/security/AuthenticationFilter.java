package keyboard.works.security;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Set;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.fasterxml.jackson.databind.ObjectMapper;

import keyboard.works.SpringAppContext;
import keyboard.works.model.request.LoginRequest;
import keyboard.works.model.response.LoginResponse;
import keyboard.works.utils.GenericResponseHelper;
import keyboard.works.utils.JwtHelper;

public class AuthenticationFilter extends UsernamePasswordAuthenticationFilter {
	
	private final AuthenticationManager authenticationManager;
	private final Validator validator;
	
	public AuthenticationFilter(AuthenticationManager authenticationManager, Validator validator) {
		this.authenticationManager = authenticationManager;
		this.validator = validator;
	}
	
	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
		
		LoginRequest loginRequest = null;
		
		try {
			
			if(request.getInputStream().available() == 0)
				throw new AuthenticationServiceException("Username and Password is mandatory!");
			
			ObjectMapper objectMapper = (ObjectMapper) SpringAppContext.getBean("objectMapper");
			loginRequest = objectMapper.readValue(request.getInputStream(), LoginRequest.class);
			
			Set<ConstraintViolation<LoginRequest>> constraintViolations = validator.validate(loginRequest);
			
			for(ConstraintViolation<?> constraintViolation : constraintViolations) {
				throw new AuthenticationServiceException(constraintViolation.getMessage());
			}
			
		} catch (IOException e) {
			throw new InsufficientAuthenticationException(e.getMessage());
		}
		
		return authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
				loginRequest.getUsername(), 
				loginRequest.getPassword(), 
				new ArrayList<>()));
	}
	
	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) 
			throws IOException, ServletException {
		
		String username = ((User) authResult.getPrincipal()).getUsername();
		
		String token = JwtHelper.createToken(username);
		
		PrintWriter out = response.getWriter();
		
		response.setContentType(MediaType.APPLICATION_JSON_VALUE);
		response.setCharacterEncoding(StandardCharsets.UTF_8.name());
		
		LoginResponse loginResponse = new LoginResponse();
		loginResponse.setToken(token);
		
		ObjectMapper objectMapper = (ObjectMapper) SpringAppContext.getBean("objectMapper");
		out.print(objectMapper.writeValueAsString(GenericResponseHelper.ok(loginResponse)));
		out.flush();
	}
	
	@Override
	protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) 
			throws IOException, ServletException {
		PrintWriter out = response.getWriter();
		
		response.setContentType(MediaType.APPLICATION_JSON_VALUE);
		response.setCharacterEncoding(StandardCharsets.UTF_8.name());
		response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

		ObjectMapper objectMapper = (ObjectMapper) SpringAppContext.getBean("objectMapper");
		
		out.print(objectMapper.writeValueAsString(GenericResponseHelper.unauthorized(failed.getMessage())));
		out.flush();
	}

}
