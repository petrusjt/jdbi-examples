package user;

import com.github.javafaker.Faker;
import org.jdbi.v3.core.Handle;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.sqlobject.SqlObjectPlugin;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Random;

public class Main {
	private static int NUMBER_OF_USERS = 10;
	public static void main(String[] args) {
		Jdbi jdbi = Jdbi.create("jdbc:h2:mem:test");
		jdbi.installPlugin(new SqlObjectPlugin());
		try (Handle handle = jdbi.open()) {
			UserDAO dao = handle.attach(UserDAO.class);
			dao.createTable();
			User JamesBond = User.builder()
					.name("James Bond")
					.username("007")
					.password("astonmartin")
					.email("james.bond@asd.com")
					.gender(User.Gender.MALE)
					.dob(LocalDate.parse("1920-11-11"))
					.enabled(true)
					.build();
			dao.insert(JamesBond);
			User ChuckNorris = User.builder()
					.name("Chuck Norris")
					.username("Walker")
					.password("spinkick")
					.email("chuck.norris@asd.com")
					.gender(User.Gender.MALE)
					.dob(LocalDate.parse("1940-03-10"))
					.enabled(true)
					.build();
			dao.insert(ChuckNorris);
			Random r = new Random();
			for(int i = 3; i <= NUMBER_OF_USERS; i++)
			{
				User u = User.builder()
						.name(Faker.instance().name().fullName())
						.password(Faker.instance().animal().name())
						.username(Faker.instance().name().username())
						.dob(Faker.instance().date().birthday(20,80).toInstant().atZone(ZoneId.systemDefault()).toLocalDate())
						.email(Faker.instance().name().fullName().replaceAll(" ", "_") + "@asd.com")
						.gender(r.nextInt(2) == 0 ? User.Gender.FEMALE : User.Gender.MALE)
						.enabled(r.nextInt(2) == 0 ? false : true)
						.build();
				dao.insert(u);
			}
			dao.list().stream().forEach(System.out::println);
			System.out.println("=============================================================================");
			System.out.println("James Bond és Chuck Norris törlése előtt");
			System.out.println("=============================================================================");
			try {
				dao.delete(dao.findById(Long.valueOf(1)).get());
			}catch(NoSuchElementException e)
			{
				e.printStackTrace();
			}
			try {
				dao.delete(dao.findByUsername("Walker").get());
			}catch(NoSuchElementException e)
			{
				e.printStackTrace();
			}

			dao.list().stream().forEach(System.out::println);
			System.out.println("=============================================================================");
			System.out.println("James Bond és Chuck Norris törlése után");
			System.out.println("=============================================================================");
		}
	}
}
