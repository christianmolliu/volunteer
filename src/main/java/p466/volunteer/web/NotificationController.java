package p466.volunteer.web;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import p466.volunteer.data.NotificationRepository;
import p466.volunteer.domain.Notification;
import p466.volunteer.domain.User;

import java.util.List;

@Controller
public class NotificationController {

    private final NotificationRepository notificationRepository;

    public NotificationController(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    @ModelAttribute
    public User getUserInfo(Authentication authentication) {
        return (User) authentication.getPrincipal();
    }

    @GetMapping("/notifications")
    public String getNotifications(Model model, Authentication authentication) {
        // get the notifications
        User user = (User) authentication.getPrincipal();
        List<Notification> notifications = notificationRepository.findAllByUserId(user.getId());
        model.addAttribute("notifications", notifications);

        // now assume the user has read those

        for (Notification notification : notifications) {
            notification.setRead(true);
        }
        notificationRepository.saveAll(notifications);

        return "notifications";
    }
}
