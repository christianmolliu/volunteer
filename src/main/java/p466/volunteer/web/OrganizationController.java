package p466.volunteer.web;

import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import p466.volunteer.data.OrganizationRepository;
import p466.volunteer.domain.Organization;
import p466.volunteer.domain.User;

@Controller
@RequestMapping("/organization")
public class OrganizationController {

    private final OrganizationRepository organizationRepository;

    public OrganizationController(OrganizationRepository organizationRepository) {
        this.organizationRepository = organizationRepository;
    }

    @ModelAttribute("org")
    public Organization createOrganization() {
        return new Organization();
    }

    @ModelAttribute
    public User getUserInfo(Authentication authentication) {
        return (User) authentication.getPrincipal();
    }

    @GetMapping("/create")
    public String getOrganizationCreateForm() {
        return "createOrganization";
    }
    @PostMapping("/create")
    public String processCreateOrganization(@Valid Organization org, Errors errors) {
        if (errors.hasErrors()) {
            return "createOrganization";
        }
        organizationRepository.save(org);
        return "redirect:/";
    }
}
