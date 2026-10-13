package p466.volunteer.web;

import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import p466.volunteer.data.OrganizationRepository;
import p466.volunteer.domain.Organization;

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
