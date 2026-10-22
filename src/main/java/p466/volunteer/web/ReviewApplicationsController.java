package p466.volunteer.web;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import p466.volunteer.data.ApplicationRepository;
import p466.volunteer.data.EventRepository;
import p466.volunteer.data.NotificationRepository;
import p466.volunteer.data.OrganizationUserRepository;
import p466.volunteer.domain.Application;
import p466.volunteer.domain.Event;
import p466.volunteer.domain.Notification;
import p466.volunteer.domain.User;

import java.nio.file.AccessDeniedException;
import java.util.List;

@Controller

public class ReviewApplicationsController {

    private final NotificationRepository notificationRepository;
    public ApplicationRepository applicationRepository;
    public EventRepository eventRepository;
    public OrganizationUserRepository organizationUserRepository;

    public ReviewApplicationsController(ApplicationRepository applicationRepository, EventRepository eventRepository, OrganizationUserRepository organizationUserRepository, NotificationRepository notificationRepository) {
        this.applicationRepository = applicationRepository;
        this.eventRepository = eventRepository;
        this.organizationUserRepository = organizationUserRepository;
        this.notificationRepository = notificationRepository;
    }

    @GetMapping("/review")
    public String getReviewApps(Authentication authentication, @RequestParam Long eventId, Model model) throws AccessDeniedException {
        Event event = eventRepository.findById(eventId).orElseThrow();
        User user = (User) authentication.getPrincipal();
        boolean belongsToOrganization = organizationUserRepository.existsByOrganizationIdAndUserId(event.getOrganizationId(), user.getId());
        if (!belongsToOrganization) {
            // handle forbidden access
            throw new AccessDeniedException("You do not own this event");
        }
        List<Application> applications = applicationRepository.findAllByEventId(eventId);
        model.addAttribute("EventApplications", applications);
        model.addAttribute("event", event); // for the event info on the page
        model.addAttribute("user", user);
        return "reviewApplications";
    }

    @PatchMapping("/review/{applicationId}")
    public String processReview(@PathVariable Long applicationId, @RequestParam String status) {
        Application application = applicationRepository.findById(applicationId).orElseThrow();
        application.setStatus(status);
        applicationRepository.save(application);

        Event event = eventRepository.findById(application.getEventId()).orElseThrow();

        Notification notification = new Notification();
        notification.setUserId(application.getUserId());
        if (status.equalsIgnoreCase("approved")) {
            notification.setDescription("Application Approved! " + event.getTitle());
        }
        else {
            notification.setDescription("Application Rejected :( " + event.getTitle());
        }
        notificationRepository.save(notification);

        return "redirect:/review?eventId=" + application.getEventId(); // redirect so content reloads
    }

}
