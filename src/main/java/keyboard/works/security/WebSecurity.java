package keyboard.works.security;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Validator;

import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import keyboard.works.SpringAppContext;
import keyboard.works.service.UserService;
import keyboard.works.utils.GenericResponseHelper;

@EnableWebSecurity
public class WebSecurity extends WebSecurityConfigurerAdapter {

	private final UserService userService;
	
	private final BCryptPasswordEncoder bCryptPasswordEncoder;
	
	private final Validator validator;
	
	public WebSecurity(UserService userService, BCryptPasswordEncoder bCryptPasswordEncoder, Validator validator) {
		this.userService = userService;
		this.bCryptPasswordEncoder = bCryptPasswordEncoder;
		this.validator = validator;
	}
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
		.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
		.and()
			.cors()
		.and()
			.csrf().disable()
			.authorizeRequests()
			.antMatchers(HttpMethod.POST, SecurityConstants.SIGNUP_URL).permitAll()
			.anyRequest().authenticated()
		.and()
			.addFilter(getAuthenticationFilter())
			.addFilterAfter(new AuthorizationFilter(), AuthenticationFilter.class)
		.exceptionHandling().authenticationEntryPoint(this::unauthorizedEntryPoint);
	}
	
	private AuthenticationFilter getAuthenticationFilter() throws Exception {
		final AuthenticationFilter filter = new AuthenticationFilter(authenticationManager(), validator);
		filter.setRequiresAuthenticationRequestMatcher(new AntPathRequestMatcher("/users/login", "POST"));
		
		return filter;
	}
	
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userService).passwordEncoder(bCryptPasswordEncoder);
	}
	
	private void unauthorizedEntryPoint(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws JsonProcessingException, IOException {
		
		response.setContentType(MediaType.APPLICATION_JSON_VALUE);
		response.setCharacterEncoding(StandardCharsets.UTF_8.name());
		response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

		ObjectMapper objectMapper = (ObjectMapper) SpringAppContext.getBean("objectMapper");
		
		response.getWriter().write(objectMapper.writeValueAsString(GenericResponseHelper.unauthorized(authException.getMessage())));
		
	}
	
}
