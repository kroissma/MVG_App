package at.mvgeboltskirchen.kroissma.mvgapp.server.service.implementation;

import at.mvgeboltskirchen.kroissma.mvgapp.server.entity.Logo;
import at.mvgeboltskirchen.kroissma.mvgapp.server.repository.LogoRepository;
import at.mvgeboltskirchen.kroissma.mvgapp.server.service.LogoService;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class SimpleLogoService implements LogoService {

    private final LogoRepository logoRepository;

    public SimpleLogoService(LogoRepository logoRepository) {
        this.logoRepository = logoRepository;
    }

    @Override
    public List<Logo> findAll() {
        return logoRepository.findAllByOrderByTitleAsc();
    }

}
