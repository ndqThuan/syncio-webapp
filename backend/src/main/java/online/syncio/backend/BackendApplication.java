package online.syncio.backend;

import lombok.RequiredArgsConstructor;
import online.syncio.backend.setting.Setting;
import online.syncio.backend.setting.SettingCategory;
import online.syncio.backend.setting.SettingRepository;
import online.syncio.backend.user.RoleEnum;
import online.syncio.backend.user.StatusEnum;
import online.syncio.backend.user.User;
import online.syncio.backend.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

@SpringBootApplication
@EnableJpaAuditing
@RequiredArgsConstructor
public class BackendApplication {


	private final UserRepository userRepository;
	private final SettingRepository settingRepository;

	@Autowired
	PasswordEncoder passwordEncoder;
	public static void main(String[] args) {
		SpringApplication.run(BackendApplication.class, args);
	}


	@Bean
	public CommandLineRunner commandLineRunner() {
		return args -> {
			try {

//				User admin = User.builder().username("admin")
//						.password(passwordEncoder.encode("admin1"))
//						.role(RoleEnum.ADMIN)
//						.username("admin")
//						.email("admin@gmail.com")
//						.status(StatusEnum.ACTIVE)
//						.build();
//
//				userRepository.save(admin);
				// insert 5 users
				for (int i = 5; i < 8; i++) {
				User user = User.builder().username("user" + i)
						.password(passwordEncoder.encode("user" + i))
						.role(RoleEnum.USER)
						.username("user" + i)
						.password("user" + i)
						.email("user" + i + "@gmail.com")
						.status(StatusEnum.ACTIVE)
						.build();
					userRepository.save(user);
				}


//				Setting settingMAIL_FROM = new Setting();
//				settingMAIL_FROM.setSettingKey("MAIL_FROM");
//				settingMAIL_FROM.setSettingValue("jayce@gmail.com");
//				settingMAIL_FROM.setCategory(SettingCategory.MAIL_SERVER);
//
//
//				Setting MAIL_HOST = new Setting();
//				MAIL_HOST.setSettingKey("MAIL_HOST");
//				MAIL_HOST.setSettingValue("smtp.gmail.com");
//				MAIL_HOST.setCategory(SettingCategory.MAIL_SERVER);
//
//				Setting MAIL_FROM = new Setting();
//				MAIL_FROM.setSettingKey("MAIL_FROM");
//				MAIL_FROM.setSettingValue("jaycedtp@gmail.com");
//				MAIL_FROM.setCategory(SettingCategory.MAIL_SERVER);
//
//				Setting MAIL_PASSWORD = new Setting();
//				MAIL_PASSWORD.setSettingKey("MAIL_PASSWORD");
//				MAIL_PASSWORD.setSettingValue("mclx ewav qpsg dvys");
//				MAIL_PASSWORD.setCategory(SettingCategory.MAIL_SERVER);
//
//				Setting MAIL_PORT = new Setting();
//				MAIL_PORT.setSettingKey("MAIL_PORT");
//				MAIL_PORT.setSettingValue("587");
//				MAIL_PORT.setCategory(SettingCategory.MAIL_SERVER);
//
//
//				Setting MAIL_USERNAME = new Setting();
//				MAIL_USERNAME.setSettingKey("MAIL_USERNAME");
//				MAIL_USERNAME.setSettingValue("jaycedtp@gmail.com");
//				MAIL_USERNAME.setCategory(SettingCategory.MAIL_SERVER);
//
//				Setting MAIL_SENDER_NAME = new Setting();
//				MAIL_SENDER_NAME.setSettingKey("MAIL_SENDER_NAME");
//				MAIL_SENDER_NAME.setSettingValue("Syncio Social Media");
//				MAIL_SENDER_NAME.setCategory(SettingCategory.MAIL_SERVER);
//
//				Setting SMTP_AUTH = new Setting();
//				SMTP_AUTH.setSettingKey("SMTP_AUTH");
//				SMTP_AUTH.setSettingValue(String.valueOf(true));
//				SMTP_AUTH.setCategory(SettingCategory.MAIL_SERVER);
//
//				Setting SMTP_SECURED = new Setting();
//				SMTP_SECURED.setSettingKey("SMTP_SECURED");
//				SMTP_SECURED.setSettingValue(String.valueOf(true));
//				SMTP_SECURED.setCategory(SettingCategory.MAIL_SERVER);
//
//				settingRepository.saveAll(List.of(settingMAIL_FROM,SMTP_SECURED,SMTP_AUTH,MAIL_USERNAME,MAIL_SENDER_NAME,
//						MAIL_PORT,MAIL_PASSWORD,MAIL_FROM,MAIL_HOST));

			} catch (Exception e) {
				e.getStackTrace();
			}

		};
	}
}
