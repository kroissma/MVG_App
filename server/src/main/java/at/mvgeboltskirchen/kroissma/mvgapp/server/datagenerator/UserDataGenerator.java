package at.mvgeboltskirchen.kroissma.mvgapp.server.datagenerator;

import at.mvgeboltskirchen.kroissma.mvgapp.server.entity.User;
import at.mvgeboltskirchen.kroissma.mvgapp.server.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Profile("generateData")
@Component
public class UserDataGenerator {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserDataGenerator.class);

    private final UserRepository userRepository;

    public UserDataGenerator(
        UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void generateSingleUser() {
        if (userRepository.count() > 0) {
            LOGGER.info("user already generated");
        } else {
            User user = new User();
            user.setId(1L);
            user.setUsername("user");
            user.setAdmin(false);
            user.setLocked(false);
            user.setPassword("password");
            LOGGER.debug("saving user {}", user);
            userRepository.save(user);
        }
    }

}
