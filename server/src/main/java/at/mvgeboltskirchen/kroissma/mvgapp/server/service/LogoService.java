package at.mvgeboltskirchen.kroissma.mvgapp.server.service;

import at.mvgeboltskirchen.kroissma.mvgapp.server.entity.Logo;
import java.util.List;

public interface LogoService {

    /**
     * Find all logo entries ordered by title (ascending).
     *
     * @return ordered list of all logo entries
     */
    List<Logo> findAll();
}
