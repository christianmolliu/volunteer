package p466.volunteer.web;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import p466.volunteer.domain.User;
import p466.volunteer.domain.UserRole;
import p466.volunteer.data.EventRepository;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(EventsController.class)
public class EventsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    EventRepository eventRepository;

    @Test
    public void shouldDisplayEvents() throws Exception {
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

        UserRole role = new UserRole();
        role.setRole("ROLE_USER");

        Authentication authentication =
                new UsernamePasswordAuthenticationToken(
                        user,
                        user.getPassword(),
                        user.getAuthorities()
                );

        mockMvc.perform(get("/")
                        .with(authentication(authentication)))
                .andExpect(status().isOk());
    }
}
