package keyboard.works.model;

import java.util.List;
import java.util.Map;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class GenericResponse<T> {

	private Integer code;
	
	private String status;
	
	private T data;
	
	private List<Map<String, String>> errors;
	
}
