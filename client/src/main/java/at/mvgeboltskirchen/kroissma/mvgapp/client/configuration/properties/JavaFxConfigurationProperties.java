package at.mvgeboltskirchen.kroissma.mvgapp.client.configuration.properties;

import javax.validation.constraints.NotNull;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;

@Validated
@Configuration
@ConfigurationProperties("java-fx")
public class JavaFxConfigurationProperties {

    @NotNull
    private Integer initialHeight = 540;

    @NotNull
    private Integer initialWidth = 960;

    @NotNull
    private String title;

    public Integer getInitialHeight() {
        return initialHeight;
    }

    public void setInitialHeight(Integer initialHeight) {
        this.initialHeight = initialHeight;
    }

    public Integer getInitialWidth() {
        return initialWidth;
    }

    public void setInitialWidth(Integer initialWidth) {
        this.initialWidth = initialWidth;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
