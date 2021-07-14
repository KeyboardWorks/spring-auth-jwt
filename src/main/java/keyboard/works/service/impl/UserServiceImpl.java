package keyboard.works.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import keyboard.works.entity.User;
import keyboard.works.model.request.UserRequest;
import keyboard.works.model.response.UserResponse;
import keyboard.works.repository.UserRepository;
import keyboard.works.service.UserService;
import keyboard.works.utils.ResponseHelper;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserRepository userRepository;
	
	@Override
	public List<UserResponse> getUsers() {
		return ResponseHelper.createResponses(UserResponse.class, userRepository.findAll());
	}

	@Override
	public UserResponse getUser(String id) {
		
		User user = userRepository.findById(id).orElseThrow(RuntimeException::new);
		
		return ResponseHelper.createResponse(UserResponse.class, user);
	}

	@Override
	public UserResponse createUser(UserRequest userRequest) {
		
		User user = new User();
		BeanUtils.copyProperties(userRequest, user);
		user = userRepository.save(user);
		
		return ResponseHelper.createResponse(UserResponse.class, user);
	}

	@Override
	public UserResponse updateUser(String id, UserRequest userRequest) {
		
		User user = userRepository.findById(id).orElseThrow(RuntimeException::new);
		BeanUtils.copyProperties(userRequest, user, "password");
		
		return ResponseHelper.createResponse(UserResponse.class, user);
	}

	@Override
	public void deleteUser(String id) {
		
		User user = userRepository.findById(id).orElseThrow(RuntimeException::new);
		
		userRepository.delete(user);
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		User user = userRepository.findByUsername(username).orElseThrow(() -> {
			throw new UsernameNotFoundException("Username " + username + " not found !");
		});
		
		return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), new ArrayList<>());
	}

}
