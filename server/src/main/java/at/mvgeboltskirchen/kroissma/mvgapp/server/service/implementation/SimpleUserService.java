package at.mvgeboltskirchen.kroissma.mvgapp.server.service.implementation;

import at.mvgeboltskirchen.kroissma.mvgapp.server.entity.News;
import at.mvgeboltskirchen.kroissma.mvgapp.server.entity.User;
import at.mvgeboltskirchen.kroissma.mvgapp.server.exception.IllegalNewsException;
import at.mvgeboltskirchen.kroissma.mvgapp.server.exception.IllegalUserException;
import at.mvgeboltskirchen.kroissma.mvgapp.server.exception.NotFoundException;
import at.mvgeboltskirchen.kroissma.mvgapp.server.repository.NewsRepository;
import at.mvgeboltskirchen.kroissma.mvgapp.server.repository.UserRepository;
import at.mvgeboltskirchen.kroissma.mvgapp.server.service.UserService;
import java.util.List;
import java.util.Optional;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class SimpleUserService implements UserService {

    private final UserRepository userRepository;
    private final NewsRepository newsRepository;

    public SimpleUserService(UserRepository userRepository, NewsRepository newsRepository) {
        this.userRepository = userRepository;
        this.newsRepository = newsRepository;
    }

    @Override
    public User findOne(Long id) {
        return userRepository.findOneById(id).orElseThrow(NotFoundException::new);
    }

    @Override
    public void addUser(User user) throws IllegalUserException {
        if (user == null) {
            throw new IllegalUserException();
        }
        try {
            userRepository.save(user);
        } catch (DataIntegrityViolationException e) {
            throw new IllegalUserException();
        }
    }

    @Override
    public User getUserById(Long id) {
        return userRepository.findOneById(id).orElseThrow(NotFoundException::new);
    }

    @Override
    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username).orElseThrow(NotFoundException::new);
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public void deleteUser(long id) {
        userRepository.delete(id);
    }

    @Override
    public void markNewsAsRead(News news, User user)
        throws IllegalUserException, IllegalNewsException {
        if (!userRepository.findOneById(user.getId()).isPresent()) {
            throw new IllegalUserException();
        }
        if (!newsRepository.findOneById(news.getId()).isPresent()) {
            throw new IllegalNewsException();
        }
        user.getReadNews().add(news);
        userRepository.save(user);
        news.getUsers().add(user);
        newsRepository.save(news);
    }


    @Override
    public Optional<User> getLoggedInUser() {
        Optional<User> r = Optional.empty();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth.isAuthenticated()) {
            org.springframework.security.core.userdetails.User user = (org.springframework.security.core.userdetails.User) auth
                .getPrincipal();
            r = userRepository.findByUsername(user.getUsername());
        }
        return r;
    }
}
