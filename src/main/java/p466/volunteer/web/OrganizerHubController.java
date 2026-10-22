package p466.volunteer.web;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import p466.volunteer.data.OrganizationRepository;
import p466.volunteer.data.OrganizationUserRepository;
import p466.volunteer.domain.Organization;
import p466.volunteer.domain.OrganizationUser;
import p466.volunteer.domain.User;

@Controller
public class OrganizerHubController {

    private final OrganizationRepository organizationRepository;
    private final OrganizationUserRepository organizationUserRepository;

    public OrganizerHubController(OrganizationRepository organizationRepository, OrganizationUserRepository organizationUserRepository) {
        this.organizationRepository = organizationRepository;
        this.organizationUserRepository = organizationUserRepository;
    }

    @GetMapping("/organizationHub")
    public String getOrganizerHub(Authentication authentication, Model model) {
        // add user to model
        User user = (User) authentication.getPrincipal();
        model.addAttribute("user", user);
        // get organizations for user, add to model
        Iterable<Long> organizationUsers = organizationUserRepository.findAllByUserId(user.getId());
        Iterable<Organization> organizations = organizationRepository.findAllById(organizationUsers);
        model.addAttribute("organizations", organizations);
        // return view
        return "organizationHub";
    }
}
