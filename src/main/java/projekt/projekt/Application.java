package projekt.projekt;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import projekt.projekt.entities.Login;
import projekt.projekt.repositories.LoginRepository;

@SpringBootApplication
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	//@Autowired LoginRepository loginRepository;
	//@Override
	//public void run(String... args) throws Exception {
	//	Login login1 = new Login("admin", "$2a$10$VN1sz1AA7qjHfrRK911PEO65PKigyR.mx3kFt..8r1CHMZh1Qu.Hm", "ROLE_ADMIN");
	//	Login login2 = new Login("staff", "$2a$10$LmH4.Tlvb3szzl3Tq0f.AOknMpdvPQVkqkQCGXm0nlyndV4RN1oZW", "ROLE_STAFF");
	//	Login login3 = new Login("client", "$2a$10$iI7v/E0sDa6MpN.nmkJeG.3vkbB.CJwhRtXj7OKF.u40xa4/pQP6i", "ROLE_CLIENT");

	//	loginRepository.save(login1);
	//	loginRepository.save(login2);
	//	loginRepository.save(login3);
	//}
}
