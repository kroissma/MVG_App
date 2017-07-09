package at.ac.tuwien.inso.sepm.ticketline.client.service.implementation;

import at.ac.tuwien.inso.sepm.ticketline.client.exception.DataAccessException;
import at.ac.tuwien.inso.sepm.ticketline.client.exception.ImageProcessingException;
import at.ac.tuwien.inso.sepm.ticketline.client.rest.NewsRestClient;
import at.ac.tuwien.inso.sepm.ticketline.client.service.NewsService;
import at.ac.tuwien.inso.sepm.ticketline.rest.news.DetailedNewsDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.news.SimpleNewsDTO;
import java.io.File;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class SimpleNewsService implements NewsService {

    private static final Logger LOGGER = LoggerFactory.getLogger(SimpleNewsService.class);

    private static final String IMAGE_FOLDER = "client"
        + File.separator
        + "src"
        + File.separator
        + "main"
        + File.separator
        + "resources"
        + File.separator
        + "image"
        + File.separator
        + "news";

    private final NewsRestClient newsRestClient;

    public SimpleNewsService(NewsRestClient newsRestClient) {
        this.newsRestClient = newsRestClient;
    }

    @Override
    public List<SimpleNewsDTO> findAll() throws DataAccessException {
        return newsRestClient.findAll();
    }

    @Override
    public List<SimpleNewsDTO> findAllNotRead() throws DataAccessException {
        return newsRestClient.findAllNotRead();
    }

    @Override
    public List<SimpleNewsDTO> findAllRead() throws DataAccessException {
        return newsRestClient.findAllRead();
    }

    @Override
    public void markNewsAsRead(SimpleNewsDTO simpleNewsDTO) throws DataAccessException {
        newsRestClient.markNewsAsRead(simpleNewsDTO);
    }

    @Override
    public DetailedNewsDTO publishNews(DetailedNewsDTO news)
        throws DataAccessException, ImageProcessingException {
        return newsRestClient.publishNews(news);
    }

    @Override
    public DetailedNewsDTO findDetailedNews(SimpleNewsDTO news) throws DataAccessException {
        return newsRestClient.findDetailedNews(news);
    }
}
