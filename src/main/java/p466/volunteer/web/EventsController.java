package p466.volunteer.web;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.ui.Model;

import org.springframework.web.bind.annotation.RequestParam;
import p466.volunteer.data.OrganizationRepository;
import p466.volunteer.domain.Event;
import p466.volunteer.domain.User;
import p466.volunteer.data.EventRepository;

import java.util.List;

@Controller
@RequestMapping("/")
public class EventsController {

    private final EventRepository eventRepository;
    public EventsController(EventRepository eventRepository, OrganizationRepository organizationRepository) {
        this.eventRepository = eventRepository;
    }

    // add user to model for use in HTML forms
    @ModelAttribute("user")
    public User getUser(Model model, Authentication authentication) {
        if (authentication != null && authentication.getPrincipal() instanceof User user) {
            return user;
        }
        return null;
    }

    @GetMapping("/")
    public String getEvents(Model model) {
        Iterable<Event> events = eventRepository.findAll();
        model.addAttribute("eventsList", events);
        return "events";
    }

    @GetMapping("/events/mine")
    public String getEventsForOrg(@RequestParam Long organizationId, Model model) {
        List<Event> events = eventRepository.findAllByOrganizationId(organizationId);
        model.addAttribute("events", events);
        return "eventsForOrg";
    }

}
