package p466.volunteer.security;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {

    private final UserDetailsService userDetailsService;

    private final SecurityContextRepository securityContextRepository =
            new HttpSessionSecurityContextRepository();

    public AuthenticationService(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    public void refreshAuthentication(
            HttpServletRequest request,
            HttpServletResponse response) {

        Authentication currentAuth =
                SecurityContextHolder.getContext().getAuthentication();

        UserDetails updatedUser = userDetailsService
                .loadUserByUsername(currentAuth.getName());

        Authentication newAuth =
                UsernamePasswordAuthenticationToken.authenticated(
                        updatedUser,
                        currentAuth.getCredentials(),
                        updatedUser.getAuthorities()
                );

        SecurityContext newContext =
                SecurityContextHolder.createEmptyContext();

        newContext.setAuthentication(newAuth);

        SecurityContextHolder.setContext(newContext);

        securityContextRepository.saveContext(
                newContext, request, response
        );
    }
}