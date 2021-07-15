package keyboard.works.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import keyboard.works.model.GenericResponse;
import keyboard.works.model.request.LoginRequest;
import keyboard.works.model.request.UserRequest;
import keyboard.works.model.response.LoginResponse;
import keyboard.works.model.response.UserResponse;
import keyboard.works.service.UserService;
import keyboard.works.utils.GenericResponseHelper;
import keyboard.works.utils.JwtHelper;

@RestController
@RequestMapping(
	path = "/users",
	produces = MediaType.APPLICATION_JSON_VALUE
)
public class UserController {

	@Autowired
	private UserService userService;
	
	@Autowired(required = true)
	private AuthenticationManager authenticationManager;
	
	@GetMapping
	public GenericResponse<List<UserResponse>> getUsers() {
		return GenericResponseHelper.ok(userService.getUsers());
	}
	
	@GetMapping(path = "{id}")
	public GenericResponse<UserResponse> getUser(@PathVariable("id") String id) {
		return GenericResponseHelper.ok(userService.getUser(id));
	}
	
	@PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
	public GenericResponse<UserResponse> createUser(@Validated(UserRequest.CreateUser.class) @RequestBody UserRequest userRequest) {
		return GenericResponseHelper.ok(userService.createUser(userRequest));
	}
	
	@PutMapping(path = "{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
	public GenericResponse<UserResponse> updateUser(@PathVariable("id") String id, @Validated(UserRequest.UpdateUser.class) @RequestBody UserRequest userRequest) {
		return GenericResponseHelper.ok(userService.updateUser(null, userRequest));
	}
	
	@DeleteMapping(path = "{id}")
	public GenericResponse<?> deleteUser(@PathVariable("id") String id) {
		userService.deleteUser(id);
		return GenericResponseHelper.ok();
	}
	
	@PostMapping(path = "/login", consumes = MediaType.APPLICATION_JSON_VALUE)
	public GenericResponse<LoginResponse> loginUser(@Validated @RequestBody LoginRequest loginRequest) {
		
		authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword(), new ArrayList<>()));
		
		String token = JwtHelper.createToken(loginRequest.getUsername());
		LoginResponse loginResponse = new LoginResponse();
		loginResponse.setToken(token);
		
		return GenericResponseHelper.ok(loginResponse);
	}
	
}
