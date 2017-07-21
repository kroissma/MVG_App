package at.mvgeboltskirchen.kroissma.mvgapp.server.repository;

import at.mvgeboltskirchen.kroissma.mvgapp.server.entity.Logo;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LogoRepository extends JpaRepository<Logo, Long> {

    List<Logo> findAllByOrderByTitleAsc();
}
