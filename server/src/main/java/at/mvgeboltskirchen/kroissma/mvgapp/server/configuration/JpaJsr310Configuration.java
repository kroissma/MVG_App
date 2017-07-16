package at.mvgeboltskirchen.kroissma.mvgapp.server.configuration;

import at.mvgeboltskirchen.kroissma.mvgapp.server.MvgAppServerApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;

@Configuration
@EntityScan(basePackageClasses = {MvgAppServerApplication.class, Jsr310JpaConverters.class})
public class JpaJsr310Configuration {

}
