package user;

import lombok.*;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@ToString
public class User {

	public static enum Gender {
		FEMALE,
		MALE
	}

	private Long id;
	private String username;
	private String password;
	private String name;
	private String email;
	private Gender gender;
	private LocalDate dob;
	private boolean enabled;

}
