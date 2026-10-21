package p466.volunteer.web;

import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import p466.volunteer.data.ApplicationRepository;
import p466.volunteer.data.EventRepository;
import p466.volunteer.domain.Application;
import p466.volunteer.domain.Event;
import p466.volunteer.domain.User;
import org.springframework.ui.Model;

@Controller
@RequestMapping
public class EventApplicationController {

    private final ApplicationRepository applicationRepository;
    private final EventRepository eventRepository;

    public EventApplicationController(ApplicationRepository applicationRepository, EventRepository eventRepository) {
        this.applicationRepository = applicationRepository;
        this.eventRepository = eventRepository;
    }

    @ModelAttribute("user")
    public User getUser(Model model, Authentication authentication) {
        if (authentication != null &&
                authentication.getPrincipal() instanceof User user) {
            return user;
        }
        return null;
    }

    @GetMapping("/apply")
    public String getApplicationForm(Authentication authentication, Model model, @RequestParam Long eventId) {
        Application application = new Application();
        User user = (User) authentication.getPrincipal();
        application.setUserId(user.getId());
        application.setEventId(eventId);
        model.addAttribute("application", application);
        Event event = eventRepository.findById(eventId).orElseThrow();
        model.addAttribute("event", event); // for event info in the applicationForm.html
        return "applicationForm";
    }

    @PostMapping("/apply")
    public String processEventApp(@Valid Application application, Errors errors, Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        application.setUserId(user.getId());
        application.setStatus("pending");
        if (errors.hasErrors()) {
            return "applicationForm";
        }
        applicationRepository.save(application);
        return "redirect:/";
    }
}
