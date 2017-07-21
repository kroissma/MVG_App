package at.mvgeboltskirchen.kroissma.mvgapp.server.entity.mapper.logo;

import java.io.File;
import java.io.IOException;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.nio.file.Files;
import org.mapstruct.Qualifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class LogoSummaryMapper {

    private static final Logger LOGGER = LoggerFactory.getLogger(LogoSummaryMapper.class);
    private static final String IMAGE_DIRECTORY = "server/src/main/resources/images/logos/";

    @LogoSummary
    public byte[] imagePathToImage(String imagePath) {
        byte[] image = null;
        try {
            image = getImageBytes(imagePath);
        } catch (IOException e) {
            LOGGER.warn("Unable to load logo {}", imagePath);
        }
        return image;
    }

    private byte[] getImageBytes(String imagePath) throws IOException {
        File file = new File(IMAGE_DIRECTORY + imagePath);
        return Files.readAllBytes(file.toPath());
    }

    @Qualifier
    @Target(ElementType.METHOD)
    @Retention(RetentionPolicy.SOURCE)
    public @interface LogoSummary {

    }

}
