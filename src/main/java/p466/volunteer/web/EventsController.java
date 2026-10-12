package p466.volunteer.web;

import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.ui.Model;

import p466.volunteer.Event;
import p466.volunteer.User;
import p466.volunteer.data.EventRepository;

@Controller
@RequestMapping("/")
public class EventsController {

    private final EventRepository eventRepository;

    public EventsController(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    // iterable of events for events.html
    @ModelAttribute("eventsList")
    public Iterable<Event> getAllEvents(Model model) {
        return eventRepository.findAll();
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

    @GetMapping("/")
    public String getEvents() {
        return "events";
    }

}
