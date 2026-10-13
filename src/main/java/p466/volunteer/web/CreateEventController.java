package p466.volunteer.web;

import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import p466.volunteer.domain.Event;
import p466.volunteer.domain.User;
import p466.volunteer.data.EventRepository;

@Controller
@RequestMapping("/create")
public class CreateEventController {

    private final EventRepository eventRepository;

    public CreateEventController(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }
    // add user to model for use in HTML forms
    @ModelAttribute("user")
    public User getUser(Model model, Authentication authentication) {
        if (authentication != null &&
                authentication.getPrincipal() instanceof User user) {
            return user;
        }
        return null;
    }

    // event for new post in createEvent.html
    @ModelAttribute("event")
    public Event getEvent(Model model) {
        return new Event();
    }

    @GetMapping()
    public String getCreateEvent() {
        return "createEvent";
    }

    @PostMapping()
    public String processCreateEvent(@Valid Event event, Errors errors, @AuthenticationPrincipal User user) {
        if(errors.hasErrors()) {
            return "createEvent";
        }
        event.setUserId(user.getId());
        event.setOrganizationId(1L); // until I link organizations and users
        eventRepository.save(event);
        return "redirect:/";
    }

}
