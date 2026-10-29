
package p466.volunteer.web;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import p466.volunteer.data.ApplicationRepository;
import p466.volunteer.data.EventRepository;
import p466.volunteer.data.NotificationRepository;
import p466.volunteer.data.OrganizationUserRepository;
import p466.volunteer.data.UserRepository;
import p466.volunteer.data.UserRolesRepository;
import p466.volunteer.domain.User;
import p466.volunteer.domain.UserRole;
import p466.volunteer.security.SecurityConfig;

import java.util.List;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ReviewApplicationsController.class)
@Import(SecurityConfig.class)
public class ReviewApplicationsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ApplicationRepository applicationRepository;

    @MockitoBean
    private EventRepository eventRepository;

    @MockitoBean
    private OrganizationUserRepository organizationUserRepository;

    @MockitoBean
    private NotificationRepository notificationRepository;

    @MockitoBean
    private UserRepository userRepository;

    @MockitoBean
    private UserRolesRepository userRolesRepository;

    @Test
    public void canReviewApplications_withOrganizerRole() throws Exception {
        User user = new User(
                "testuser",
                "password",
                "Test User",
                "123 Main St",
                "Bloomington",
                "IN",
                "47401",
                "5555555555"
        );

        UserRole user_role = new UserRole();
        user_role.setRole("ROLE_USER");

        UserRole organizer_role = new UserRole();
        organizer_role.setRole("ROLE_ORGANIZER");

        user.setRoles(List.of(user_role, organizer_role));

        Authentication authentication =
                new UsernamePasswordAuthenticationToken(
                        user,
                        user.getPassword(),
                        user.getAuthorities()
                );

        mockMvc.perform(get("/review")
                        .with(authentication(authentication)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void cannotReviewApplications_withoutOrganizerRole() throws Exception {
        User user = new User(
                "testuser",
                "password",
                "Test User",
                "123 Main St",
                "Bloomington",
                "IN",
                "47401",
                "5555555555"
        );

        UserRole user_role = new UserRole();
        user_role.setRole("ROLE_USER");

        user.setRoles(List.of(user_role));

        Authentication authentication =
                new UsernamePasswordAuthenticationToken(
                        user,
                        user.getPassword(),
                        user.getAuthorities()
                );

        mockMvc.perform(get("/review")
                        .param("eventId", "1")
                        .with(authentication(authentication)))
                .andExpect(status().isForbidden());
    }

    @Test
    public void cannotReviewApplications_withoutLogin() throws Exception {
        mockMvc.perform(get("/review")
                        .param("eventId", "1"))
                .andExpect(status().is3xxRedirection());
    }
}
