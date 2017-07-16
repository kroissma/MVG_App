package at.mvgeboltskirchen.kroissma.mvgapp.server.endpoint;

import at.mvgeboltskirchen.kroissma.mvgapp.rest.authentication.AuthenticationRequest;
import at.mvgeboltskirchen.kroissma.mvgapp.rest.authentication.AuthenticationToken;
import at.mvgeboltskirchen.kroissma.mvgapp.rest.authentication.AuthenticationTokenInfo;
import at.mvgeboltskirchen.kroissma.mvgapp.server.security.AuthenticationConstants;
import at.mvgeboltskirchen.kroissma.mvgapp.server.service.HeaderTokenAuthenticationService;
import at.mvgeboltskirchen.kroissma.mvgapp.server.service.implementation.SimpleHeaderTokenAuthenticationService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

@RestController
@RequestMapping(value = "/authentication")
@Api(value = "authentication")
public class AuthenticationEndpoint {

    private final HeaderTokenAuthenticationService authenticationService;

    public AuthenticationEndpoint(
        SimpleHeaderTokenAuthenticationService simpleHeaderTokenAuthenticationService) {
        authenticationService = simpleHeaderTokenAuthenticationService;
    }

    @RequestMapping(method = RequestMethod.POST)
    @ApiOperation(value = "Get an authentication token with your username and password")
    public AuthenticationToken authenticate(
        @RequestBody final AuthenticationRequest authenticationRequest) {
        return authenticationService
            .authenticate(authenticationRequest.getUsername(), authenticationRequest.getPassword());
    }

    @RequestMapping(method = RequestMethod.GET)
    @ApiOperation(value = "Get some valid authentication tokens")
    public AuthenticationToken authenticate(
        @ApiIgnore @RequestHeader(value = HttpHeaders.AUTHORIZATION) String authorizationHeader) {
        return authenticationService.renewAuthentication(
            authorizationHeader.substring(AuthenticationConstants.TOKEN_PREFIX.length()).trim());
    }

    @RequestMapping(value = "/info/{token}", method = RequestMethod.GET)
    @ApiOperation(value = "Get information about a specific authentication token")
    public AuthenticationTokenInfo tokenInfoAny(@PathVariable String token) {
        return authenticationService.authenticationTokenInfo(token);
    }

    @RequestMapping(value = "/info", method = RequestMethod.GET)
    @ApiOperation(value = "Get information about the current users authentication token")
    public AuthenticationTokenInfo tokenInfoCurrent(
        @ApiIgnore @RequestHeader(value = HttpHeaders.AUTHORIZATION) String authorizationHeader) {
        return authenticationService.authenticationTokenInfo(
            authorizationHeader.substring(AuthenticationConstants.TOKEN_PREFIX.length()).trim());
    }
}
