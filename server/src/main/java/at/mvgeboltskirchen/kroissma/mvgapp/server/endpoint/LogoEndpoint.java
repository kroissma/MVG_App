package at.mvgeboltskirchen.kroissma.mvgapp.server.endpoint;

import at.mvgeboltskirchen.kroissma.mvgapp.rest.logo.LogoDTO;
import at.mvgeboltskirchen.kroissma.mvgapp.server.entity.Logo;
import at.mvgeboltskirchen.kroissma.mvgapp.server.entity.mapper.logo.LogoMapper;
import at.mvgeboltskirchen.kroissma.mvgapp.server.service.HeaderTokenAuthenticationService;
import at.mvgeboltskirchen.kroissma.mvgapp.server.service.LogoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/logo")
@Api(value = "logo")
public class LogoEndpoint {

    private static final Logger LOGGER = LoggerFactory.getLogger(LogoEndpoint.class);

    private final LogoService logoService;
    private final LogoMapper logoMapper;
    private final HeaderTokenAuthenticationService authenticationService;

    public LogoEndpoint(LogoService logoService, LogoMapper logoMapper,
        HeaderTokenAuthenticationService headerTokenAuthenticationService) {
        this.logoService = logoService;
        this.logoMapper = logoMapper;
        this.authenticationService = headerTokenAuthenticationService;
    }

    @RequestMapping(method = RequestMethod.GET)
    @ApiOperation(value = "Get list of logo entries")
    public List<LogoDTO> findAll() {
        List<LogoDTO> logoDTOS = new ArrayList<>();
        List<Logo> logos = logoService.findAll();
        for (Logo logo : logos) {
            logoDTOS.add(logoMapper.logoToLogoDTO(logo));
        }
        return logoDTOS;
    }
}
