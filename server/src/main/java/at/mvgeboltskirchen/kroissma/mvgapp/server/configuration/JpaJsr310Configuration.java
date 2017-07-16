package at.mvgeboltskirchen.kroissma.mvgapp.server.configuration;

import at.mvgeboltskirchen.kroissma.mvgapp.server.TicketlineServerApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;

@Configuration
@EntityScan(basePackageClasses = {TicketlineServerApplication.class, Jsr310JpaConverters.class})
public class JpaJsr310Configuration {

}
