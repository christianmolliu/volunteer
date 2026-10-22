package p466.volunteer.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import p466.volunteer.domain.User;
import p466.volunteer.domain.UserRole;
import p466.volunteer.data.UserRepository;
import p466.volunteer.data.UserRolesRepository;

import java.util.List;


@Configuration
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService(UserRepository userRepo, UserRolesRepository userRolesRepo) {
        return username -> {
            User user = userRepo.findByUsername(username);
            if (user == null) {
                throw new UsernameNotFoundException(
                        "User '" + username + "' not found"
                );
            }
            List<UserRole> roles = userRolesRepo.findAllByUserId(user.getId());

            user.setRoles(roles);

            return user;
        };
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/create", "/create/**").hasRole("ORGANIZER")
                        .requestMatchers("/apply", "/apply/**").hasRole("USER")
                        .requestMatchers("/review", "/review/**").hasRole("ORGANIZER")
                        .requestMatchers("/organization/invite/**").hasRole("ORGANIZER")
                        .requestMatchers("/organization/create").hasRole("USER")
                        .requestMatchers("/organizationHub").hasRole("ORGANIZER")
                        .anyRequest().permitAll()
                )
                .formLogin(form -> form
                        .loginPage("/login")
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutSuccessUrl("/")
                );
        return http.build();
    }
}
