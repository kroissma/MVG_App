package at.mvgeboltskirchen.kroissma.mvgapp.client.service.implementation;

import at.mvgeboltskirchen.kroissma.mvgapp.client.exception.DataAccessException;
import at.mvgeboltskirchen.kroissma.mvgapp.client.rest.LogoRestClient;
import at.mvgeboltskirchen.kroissma.mvgapp.client.service.LogoService;
import at.mvgeboltskirchen.kroissma.mvgapp.rest.logo.LogoDTO;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class SimpleLogoService implements LogoService {

    private LogoRestClient logoRestClient;

    public SimpleLogoService(LogoRestClient logoRestClient) {
        this.logoRestClient = logoRestClient;
    }

    @Override
    public List<LogoDTO> findAll() throws DataAccessException {
        return logoRestClient.findAll();
    }
}
