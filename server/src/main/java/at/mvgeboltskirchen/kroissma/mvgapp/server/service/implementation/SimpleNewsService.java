package at.mvgeboltskirchen.kroissma.mvgapp.server.service.implementation;

import at.mvgeboltskirchen.kroissma.mvgapp.server.entity.News;
import at.mvgeboltskirchen.kroissma.mvgapp.server.exception.NotFoundException;
import at.mvgeboltskirchen.kroissma.mvgapp.server.repository.NewsRepository;
import at.mvgeboltskirchen.kroissma.mvgapp.server.service.NewsService;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class SimpleNewsService implements NewsService {

    private final NewsRepository newsRepository;

    public SimpleNewsService(NewsRepository newsRepository) {
        this.newsRepository = newsRepository;
    }

    @Override
    public List<News> findAll() {
        return newsRepository.findAllByOrderByPublishedAtDesc();
    }

    @Override
    public List<News> findAllNotRead(String username) {
        return newsRepository.findNotReadByOrderByPublishedAtDesc(username);
    }

    @Override
    public News findOne(Long id) {
        return newsRepository.findOneById(id).orElseThrow(NotFoundException::new);
    }

    @Override
    public News publishNews(News news) {
        news.setPublishedAt(LocalDateTime.now());
        return newsRepository.save(news);
    }

    @Override
    public List<News> findAllRead(String username) {
        return newsRepository.findReadByOrderByPublishedAtDesc(username);
    }

}
