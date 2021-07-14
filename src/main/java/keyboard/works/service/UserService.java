package keyboard.works.service;

import java.util.List;

import org.springframework.security.core.userdetails.UserDetailsService;

import keyboard.works.model.request.UserRequest;
import keyboard.works.model.response.UserResponse;

public interface UserService extends UserDetailsService {

	List<UserResponse> getUsers();
	
	UserResponse getUser(String id);
	
	UserResponse createUser(UserRequest userRequest);
	
	UserResponse updateUser(String id, UserRequest userRequest);
	
	void deleteUser(String id);
	
}
