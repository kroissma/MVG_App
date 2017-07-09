package at.ac.tuwien.inso.sepm.ticketline.server.endpoint;

import at.ac.tuwien.inso.sepm.ticketline.rest.news.DetailedNewsDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.news.SimpleNewsDTO;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.News;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.User;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.mapper.news.NewsMapper;
import at.ac.tuwien.inso.sepm.ticketline.server.exception.IllegalNewsException;
import at.ac.tuwien.inso.sepm.ticketline.server.exception.IllegalUserException;
import at.ac.tuwien.inso.sepm.ticketline.server.exception.ImageProcessingException;
import at.ac.tuwien.inso.sepm.ticketline.server.security.AuthenticationConstants;
import at.ac.tuwien.inso.sepm.ticketline.server.service.HeaderTokenAuthenticationService;
import at.ac.tuwien.inso.sepm.ticketline.server.service.NewsService;
import at.ac.tuwien.inso.sepm.ticketline.server.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/news")
@Api(value = "news")
public class NewsEndpoint {

    private static final Logger LOGGER = LoggerFactory.getLogger(NewsEndpoint.class);
    private static final String IMAGE_DIRECTORY = "server/src/main/resources/images/news/";

    private final NewsService newsService;
    private final NewsMapper newsMapper;
    private final UserService userService;
    private final HeaderTokenAuthenticationService authenticationService;

    public NewsEndpoint(NewsService newsService, NewsMapper newsMapper, UserService userService,
        HeaderTokenAuthenticationService headerTokenAuthenticationService) {
        this.newsService = newsService;
        this.newsMapper = newsMapper;
        this.userService = userService;
        this.authenticationService = headerTokenAuthenticationService;
    }

    private String getUsername(String authorizationHeader) {
        return authenticationService.authenticationTokenInfo(authorizationHeader.substring(
            AuthenticationConstants.TOKEN_PREFIX.length()).trim()).getUsername();
    }

    @RequestMapping(method = RequestMethod.GET)
    @ApiOperation(value = "Get list of simple news entries")
    public List<SimpleNewsDTO> findAll() {
        return newsMapper.newsToSimpleNewsDTO(newsService.findAll());
    }

    @RequestMapping(value = "/notread", method = RequestMethod.GET)
    @ApiOperation(value = "Get list of simple news entries not read by the logged in user")
    public List<SimpleNewsDTO> findAllNotRead(
        @RequestHeader(value = HttpHeaders.AUTHORIZATION) String authorizationHeader) {
        return newsMapper
            .newsToSimpleNewsDTO(newsService.findAllNotRead(getUsername(authorizationHeader)));

    }

    @RequestMapping(value = "/read", method = RequestMethod.GET)
    @ApiOperation(value = "Get list of simple news entries read by the logged in user")
    public List<SimpleNewsDTO> findAllRead(
        @RequestHeader(value = HttpHeaders.AUTHORIZATION) String authorizationHeader) {
        return newsMapper
            .newsToSimpleNewsDTO(newsService.findAllRead(getUsername(authorizationHeader)));

    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @ApiOperation(value = "Get detailed information about a specific news entry")
    public DetailedNewsDTO find(@PathVariable Long id) {
        News news = newsService.findOne(id);
        DetailedNewsDTO detailedNewsDTO = newsMapper.newsToDetailedNewsDTO(news);
        if (news.getImage() != null) {
            try {
                detailedNewsDTO.setImageBytes(getImageBytes(news.getImage()));
            } catch (IOException e) {
                LOGGER.warn("Unable to load image {}", news.getImage());
            }
        }
        LOGGER.info("Sending detailed news {}.", detailedNewsDTO);
        return detailedNewsDTO;
    }

    private byte[] getImageBytes(String image) throws IOException {
        File file = new File(IMAGE_DIRECTORY + image);
        return Files.readAllBytes(file.toPath());
    }

    @RequestMapping(method = RequestMethod.POST)
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @ApiResponses(value = {
        @ApiResponse(code = 500, message = "Unable to store image")
    })
    @ApiOperation(value = "Publish a new news entry")
    public DetailedNewsDTO publishNews(@RequestBody DetailedNewsDTO detailedNewsDTO)
        throws ImageProcessingException {
        News news = newsMapper.detailedNewsDTOToNews(detailedNewsDTO);
        news.setImage(storeImage(detailedNewsDTO.getImageBytes()));
        news = newsService.publishNews(news);
        DetailedNewsDTO ret = newsMapper.newsToDetailedNewsDTO(news);
        ret.setImageBytes(detailedNewsDTO.getImageBytes());
        return ret;
    }

    private String storeImage(byte[] imageBytes) throws ImageProcessingException {
        if (imageBytes == null) {
            return null;
        }
        String imageTarget = null;
        FileOutputStream fos = null;
        try {
            imageTarget = getCurrentTimestamp() + ".jpg";
            fos = new FileOutputStream(IMAGE_DIRECTORY + imageTarget);
            fos.write(imageBytes);
        } catch (IOException e) {
            LOGGER.warn("Unable to store image in {}", imageTarget);
            throw new ImageProcessingException("Error storing image");
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    LOGGER.warn("Unable to close FileOutputStream");
                }
            }
        }
        return imageTarget;
    }

    private String getCurrentTimestamp() {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddhhmmss"));
    }

    @RequestMapping(value = "/read/{id}", method = RequestMethod.PUT)
    @ApiOperation(value = "Mark a news as read by logged in user")
    public void markNewsAsRead(@PathVariable("id") long newsID,
        @RequestHeader(value = HttpHeaders.AUTHORIZATION) String authorizationHeader) {
        User user = userService.getUserByUsername(getUsername(authorizationHeader));
        News news = newsService.findOne(newsID);
        try {
            userService.markNewsAsRead(news, user);
        } catch (IllegalUserException e) {
            LOGGER.warn("Mark as read with user {} not possible", user);
        } catch (IllegalNewsException e) {
            LOGGER.warn("Mark as read with news {} not possible", news);
        }
    }
}
