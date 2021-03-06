package at.mvgeboltskirchen.kroissma.springfx;

import at.mvgeboltskirchen.kroissma.mvgapp.client.util.BundleManager;
import java.io.IOException;
import javafx.fxml.FXMLLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

/**
 * Bridge between Spring and JavaFX which allows to inject Spring beans in JavaFX controller. <p>
 * Based on <a href="http://www.javacodegeeks.com/2013/03/javafx-2-with-spring.html">http://www.javacodegeeks.com/2013/03/javafx-2-with-spring.html</a>
 */
@Component
public class SpringFxmlLoader {

    private static final Logger LOGGER = LoggerFactory.getLogger(SpringFxmlLoader.class);

    private final ApplicationContext applicationContext;

    public SpringFxmlLoader(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    /**
     * Generate fxml loader based on the given URL.
     *
     * @param url the url
     * @return the FXML loader
     */
    private FXMLLoader generateFXMLLoader(String url) {
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(SpringFxmlLoader.class.getResource(url));
        fxmlLoader.setResources(BundleManager.getBundle());
        fxmlLoader.setControllerFactory(clazz -> {
            LOGGER.debug("Trying to retrieve spring bean for type {}", clazz.getName());
            Object bean = null;
            try {
                bean = applicationContext.getBean(clazz);
            } catch (NoSuchBeanDefinitionException e) {
                LOGGER.warn("No qualifying spring bean of type {} found", clazz.getName());
            }
            if (bean == null) {
                LOGGER.debug("Trying to instantiating class of type {}", clazz.getName());
                try {
                    bean = clazz.newInstance();
                } catch (InstantiationException | IllegalAccessException e) {
                    LOGGER.error("Failed to instantiate bean of type {}", clazz.getName(), e);
                    throw new RuntimeException(
                        "Failed to instantiate bean of type " + clazz.getName(), e);
                }
            }
            return bean;
        });
        return fxmlLoader;
    }

    /**
     * Loads object hierarchy from the FXML document given in the URL.
     *
     * @param url the url
     * @return the object
     */
    public Object load(String url) {
        try {
            return generateFXMLLoader(url).load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Loads and wraps an object hierarchy.
     *
     * @param url the url
     * @return the load wrapper
     */
    public LoadWrapper loadAndWrap(String url) {
        FXMLLoader fxmlLoader = generateFXMLLoader(url);
        try {
            return new LoadWrapper(fxmlLoader.load(), fxmlLoader.getController());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * The Class LoadWrapper.
     */
    public final class LoadWrapper {

        private final Object controller;
        private final Object loadedObject;

        /**
         * Instantiates a new load wrapper.
         *
         * @param loadedObject the loaded object
         * @param controller the controller
         */
        public LoadWrapper(Object loadedObject, Object controller) {
            this.controller = controller;
            this.loadedObject = loadedObject;
        }

        /**
         * Gets the controller.
         *
         * @return the controller
         */
        public Object getController() {
            return controller;
        }

        /**
         * Gets the loaded object.
         *
         * @return the loaded object
         */
        public Object getLoadedObject() {
            return loadedObject;
        }

    }

}