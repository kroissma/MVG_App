package at.mvgeboltskirchen.kroissma.mvgapp.server.datagenerator;

import at.mvgeboltskirchen.kroissma.mvgapp.server.entity.News;
import at.mvgeboltskirchen.kroissma.mvgapp.server.repository.NewsRepository;
import com.github.javafaker.Faker;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.concurrent.TimeUnit;
import javax.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Profile("generateData")
@Component
public class NewsDataGenerator {

    private static final Logger LOGGER = LoggerFactory.getLogger(NewsDataGenerator.class);
    private static final int NUMBER_OF_NEWS_TO_GENERATE = 2;

    private final NewsRepository newsRepository;
    private final Faker faker;

    public NewsDataGenerator(NewsRepository newsRepository) {
        this.newsRepository = newsRepository;
        faker = new Faker();
    }

    @PostConstruct
    private void generateNews() {
        if (newsRepository.count() > 0) {
            LOGGER.info("news already generated");
        } else {
            LOGGER.info("generating {} news entries", NUMBER_OF_NEWS_TO_GENERATE);
            for (int i = 0; i < NUMBER_OF_NEWS_TO_GENERATE; i++) {
                News news = News.builder()
                    .title(faker.lorem().characters(25, 100))
                    .text(faker.lorem().paragraph(faker.number().numberBetween(5, 10)))
                    .publishedAt(
                        LocalDateTime.ofInstant(
                            faker.date()
                                .past(365 * 3, TimeUnit.DAYS).
                                toInstant(),
                            ZoneId.systemDefault()
                        ))
                    .build();
                LOGGER.debug("saving news {}", news);
                newsRepository.save(news);
            }
        }
    }

}
