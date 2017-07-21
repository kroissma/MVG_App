package at.mvgeboltskirchen.kroissma.mvgapp.server.datagenerator;

import at.mvgeboltskirchen.kroissma.mvgapp.server.entity.Logo;
import at.mvgeboltskirchen.kroissma.mvgapp.server.repository.LogoRepository;
import com.github.javafaker.Faker;
import javax.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Profile("generateData")
@Component
public class LogoDataGenerator {

    private static final Logger LOGGER = LoggerFactory.getLogger(LogoDataGenerator.class);

    private final LogoRepository logoRepository;
    private final Faker faker;

    public LogoDataGenerator(LogoRepository logoRepository) {
        this.logoRepository = logoRepository;
        faker = new Faker();
    }

    @PostConstruct
    private void generateLogos() {
        if (logoRepository.count() > 0) {
            LOGGER.info("logos already generated");
        } else {
            LOGGER.info("generating logo entries");

            Logo logo = Logo.builder()
                .title("Aichinger Bau")
                .imagePath("aichinger_bau.jpg")
                .build();

            LOGGER.debug("saving logo {}", logo);
            logoRepository.save(logo);

            logo = Logo.builder()
                .title("Allianz")
                .imagePath("allianz.jpg")
                .build();

            LOGGER.debug("saving logo {}", logo);
            logoRepository.save(logo);
        }
    }
}
