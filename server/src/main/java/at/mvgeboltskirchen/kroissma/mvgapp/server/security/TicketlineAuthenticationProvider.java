package at.mvgeboltskirchen.kroissma.mvgapp.server.security;

import at.mvgeboltskirchen.kroissma.mvgapp.server.entity.User;
import at.mvgeboltskirchen.kroissma.mvgapp.server.repository.UserRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class TicketlineAuthenticationProvider extends DaoAuthenticationProvider {

    private static final Logger LOGGER = LoggerFactory
        .getLogger(TicketlineAuthenticationProvider.class);
    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;

    public TicketlineAuthenticationProvider(TicketlineUserDetailsService userDetailsService,
        PasswordEncoder passwordEncoder,
        UserRepository userRepository) {
        super();
        setUserDetailsService(userDetailsService);
        setPasswordEncoder(passwordEncoder);
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Authenticates the user.
     * Checks if the authentication is valid. If so an authentication token will be returned.
     * If not a corresponding exception will be thrown.
     *
     * @param authentication authentication which should be checked
     * @return authentication token
     * @throws AuthenticationException thrown if the authentication is not valid
     */
    @Override
    public Authentication authenticate(Authentication authentication)
        throws AuthenticationException {
        String username = authentication.getPrincipal().toString();

        Optional<User> userOpt = userRepository.findByUsername(username);
        if (userOpt == null || !userOpt.isPresent()) {
            LOGGER.error("user not found");
            throw new UsernameNotFoundException("Wrong username");
        }
        User user = userOpt.get();
        if (user.getLocked()) {
            LOGGER.error("user is locked");
            throw new LockedException("Account is locked");
        }

        if (!passwordEncoder.matches(authentication.getCredentials().toString(),
            user.getPassword())) {
            LOGGER.info("User {} tried to log in with wrong password", username);
            if (user.getFailedLoginAttempts() >= 4) {
                user.setLocked(true);
                LOGGER.info("User {} locked because of too many failed login attempts", username);
            }
            user.setFailedLoginAttempts(user.getFailedLoginAttempts() + 1);
            userRepository.save(user);
            throw new BadCredentialsException("Wrong password!");
        }
        if (user.getFailedLoginAttempts() != 0) {
            user.setFailedLoginAttempts(0);
            userRepository.save(user);
        }
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
        if (user.getAdmin()) {
            authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
        }
        return new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword(),
            authorities);
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}
