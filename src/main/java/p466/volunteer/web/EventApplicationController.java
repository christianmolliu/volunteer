package p466.volunteer.web;

import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import p466.volunteer.data.*;
import p466.volunteer.domain.*;
import org.springframework.ui.Model;

import java.util.List;

@Controller
@RequestMapping
public class EventApplicationController {

    private final ApplicationRepository applicationRepository;
    private final EventRepository eventRepository;
    private final OrganizationRepository organizationRepository;
    private final OrganizationUserRepository organizationUserRepository;
    private final NotificationRepository notificationRepository;

    public EventApplicationController(ApplicationRepository applicationRepository, EventRepository eventRepository, OrganizationRepository organizationRepository, OrganizationUserRepository organizationUserRepository, NotificationRepository notificationRepository) {
        this.applicationRepository = applicationRepository;
        this.eventRepository = eventRepository;
        this.organizationRepository = organizationRepository;
        this.organizationUserRepository = organizationUserRepository;
        this.notificationRepository = notificationRepository;
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

        // send notification to all members of org
        Event event = eventRepository.findById(application.getEventId()).orElseThrow();
        long orgId = event.getOrganizationId();
        List<OrganizationUser> orgUsers = organizationUserRepository.findAllByOrganizationId(orgId);
        for (OrganizationUser orgUser : orgUsers) {
            Notification notification = new Notification();
            notification.setUserId(orgUser.getUserId());
            notification.setDescription("There's a new application for " + event.getTitle());
            notificationRepository.save(notification);
        }

        return "redirect:/";
    }
}
