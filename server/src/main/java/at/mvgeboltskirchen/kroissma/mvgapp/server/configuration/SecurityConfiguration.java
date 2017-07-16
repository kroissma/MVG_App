package at.mvgeboltskirchen.kroissma.mvgapp.server.configuration;

import at.mvgeboltskirchen.kroissma.mvgapp.server.configuration.properties.H2ConsoleConfigurationProperties;
import at.mvgeboltskirchen.kroissma.mvgapp.server.entity.User;
import at.mvgeboltskirchen.kroissma.mvgapp.server.repository.UserRepository;
import at.mvgeboltskirchen.kroissma.mvgapp.server.security.HeaderTokenAuthenticationFilter;
import at.mvgeboltskirchen.kroissma.mvgapp.server.security.TicketlineAuthenticationProvider;
import at.mvgeboltskirchen.kroissma.mvgapp.server.security.TicketlineUserDetailsService;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.boot.autoconfigure.web.DefaultErrorAttributes;
import org.springframework.boot.autoconfigure.web.ErrorAttributes;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true)
public class SecurityConfiguration {

    private static final Logger LOGGER = LoggerFactory.getLogger(SecurityConfiguration.class);
    private final PasswordEncoder passwordEncoder;
    private final TicketlineUserDetailsService userDetailsService;
    private final UserRepository userRepository;

    public SecurityConfiguration(PasswordEncoder passwordEncoder,
        TicketlineUserDetailsService userDetailsService,
        UserRepository userRepository) {
        this.passwordEncoder = passwordEncoder;
        this.userDetailsService = userDetailsService;
        this.userRepository = userRepository;
    }

    @Bean
    public static PasswordEncoder configureDefaultPasswordEncoder() {
        return new BCryptPasswordEncoder(10);
    }

    @Bean
    public ErrorAttributes errorAttributes() {
        return new DefaultErrorAttributes() {
            @Override
            public Map<String, Object> getErrorAttributes(RequestAttributes requestAttributes,
                boolean includeStackTrace) {
                Map<String, Object> errorAttributes = super
                    .getErrorAttributes(requestAttributes, includeStackTrace);
                errorAttributes.remove("exception");
                return errorAttributes;
            }
        };
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth,
        List<AuthenticationProvider> providerList) throws Exception {
/*
        new InMemoryUserDetailsManagerConfigurer<AuthenticationManagerBuilder>()
            .withUser("user").password(passwordEncoder.encode("password")).authorities("USER").and()
            .withUser("admin").password(passwordEncoder.encode("password")).authorities("ADMIN", "USER").and()
            .passwordEncoder(passwordEncoder)
            .configure(auth);
        providerList.forEach(auth::authenticationProvider);
*/
        providerList.forEach(auth::authenticationProvider);

        if (userRepository.count() > 0) {
            LOGGER.debug("Users already created..");
        } else {
            User user = User.builder()
                .username("user")
                .password(passwordEncoder.encode("password"))
                .isLocked(false)
                .isAdmin(false)
                .failedLoginAttempts(0)
                .build();

            User admin = User.builder()
                .username("admin")
                .password(passwordEncoder.encode("password"))
                .isLocked(false)
                .isAdmin(true)
                .failedLoginAttempts(0)
                .build();

            userRepository.save(user);
            userRepository.save(admin);

            LOGGER.debug("users stored in Database...");
        }
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        return new TicketlineAuthenticationProvider(userDetailsService, passwordEncoder,
            userRepository);
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return new TicketlineUserDetailsService(userRepository);
    }

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurerAdapter() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry
                    .addMapping("/**")
                    .allowedOrigins("*");
            }
        };
    }

    @Configuration
    @Order(SecurityProperties.BASIC_AUTH_ORDER)
    private static class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {

        private final AuthenticationManager authenticationManager;
        private final String h2ConsolePath;
        private final String h2AccessMatcher;

        public WebSecurityConfiguration(
            AuthenticationManager authenticationManager,
            H2ConsoleConfigurationProperties h2ConsoleConfigurationProperties
        ) {
            this.authenticationManager = authenticationManager;
            h2ConsolePath = h2ConsoleConfigurationProperties.getPath();
            h2AccessMatcher = h2ConsoleConfigurationProperties.getAccessMatcher();
        }

        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http
                .csrf().disable()
                .headers().frameOptions().sameOrigin().and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
                .exceptionHandling().authenticationEntryPoint(
                (req, res, aE) -> res.sendError(HttpServletResponse.SC_UNAUTHORIZED)).and()
                .authorizeRequests()
                .antMatchers(HttpMethod.OPTIONS).permitAll()
                .antMatchers(HttpMethod.POST, "/authentication").permitAll()
                .antMatchers(HttpMethod.GET,
                    "/v2/api-docs",
                    "/swagger-resources/**",
                    "/webjars/springfox-swagger-ui/**",
                    "/swagger-ui.html")
                .permitAll()
            ;
            if (h2ConsolePath != null && h2AccessMatcher != null) {
                http
                    .authorizeRequests()
                    .antMatchers(h2ConsolePath + "/**").access(h2AccessMatcher);
            }
            http
                .authorizeRequests()
                .anyRequest().fullyAuthenticated()
                .and()
                .addFilterBefore(new HeaderTokenAuthenticationFilter(authenticationManager),
                    UsernamePasswordAuthenticationFilter.class);
        }

    }
}
