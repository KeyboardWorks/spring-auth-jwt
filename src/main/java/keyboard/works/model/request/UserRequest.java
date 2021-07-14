package keyboard.works.model.request;

import javax.validation.constraints.NotBlank;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserRequest {

	@NotBlank(message = "Name is mandatory", groups = { CreateUser.class, UpdateUser.class })
	private String name;
	
	@NotBlank(message = "Username is mandatory", groups = { CreateUser.class, UpdateUser.class })
	private String username;
	
	@NotBlank(message = "Password is mandatory", groups = { CreateUser.class })
	private String password;
	
	public static interface CreateUser {
		
	}
	
	public static interface UpdateUser {
		
	}
	
}
